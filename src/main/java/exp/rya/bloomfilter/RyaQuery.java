package exp.rya.bloomfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;
import org.apache.rya.api.persist.RyaDAOException;
import org.apache.rya.rdftriplestore.inference.InferenceEngineException;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.QueryResultHandlerException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.SailException;

import com.google.common.base.Stopwatch;

import exp.rya.bloomfilter.utils.RandomUUID;
import exp.rya.bloomfilter.utils.Utils;

public class RyaQuery extends Query {
    private static final Logger log = Logger.getLogger(RyaQuery.class);

    private static ValueFactory VF = new ValueFactoryImpl();

    public SailRepositoryConnection createConnection(final Configuration conf) {
        try {
            return Utils.getInMemoryAccConn(conf, true);
        }
        catch (final InferenceEngineException | RyaDAOException | AccumuloSecurityException | AccumuloException | SailException | RepositoryException e) {
            log.error("Unable to create a connection", e);
        }
        return null;
    }

    public void closeConnection(final SailRepositoryConnection sailConn) {
        if (sailConn != null) {
            try {
                final SailRepository sailRepo = (SailRepository) sailConn.getRepository();
                if (sailRepo != null) {
                    sailRepo.shutDown();
                }

                sailConn.close();
            }
            catch (final RepositoryException e) {
                log.error("Unable to close the connection", e);
            }
        }
    }

    public void performQuery(final SailRepositoryConnection sailConn, final String tablePrefix, final String sparql, final int numberOfValues, final String ratio, final int numberOfIterations) {
        for (int iteration = 0; iteration < numberOfIterations; iteration++) {
            final Stopwatch watch = Stopwatch.createStarted();
            try {
                executeQuery(sailConn, sparql);
            }
            catch (final MalformedQueryException | RepositoryException e) {
                log.error("Failed to prepare query", e);
            }
            catch (final QueryEvaluationException | QueryResultHandlerException e) {
                log.error("Failed to evaluate and count", e);
            }
            finally {
                if (ratio != null) {
                    log.info(String.format("csv(ms), %d, %s, %,d, %s", (iteration + 1), tablePrefix, watch.elapsed(TimeUnit.MILLISECONDS), ratio));
                }
                else if (numberOfValues > 0) {
                    log.info(String.format("csv(ms), %d, %s, %,d, %,d", (iteration + 1), tablePrefix, watch.elapsed(TimeUnit.MILLISECONDS), numberOfValues));
                }
                else {
                    log.info(String.format("csv(ms), %d, %s, %,d", (iteration + 1), tablePrefix, watch.elapsed(TimeUnit.MILLISECONDS)));
                }
            }
        }
    }

    private void executeQuery(final SailRepositoryConnection sailRepo, final String sparqlQuery) throws MalformedQueryException, RepositoryException, QueryEvaluationException, QueryResultHandlerException {
        sailRepo.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery);
        Utils.evaluateAndCount(sparqlQuery, sailRepo);
    }

    private String formatValues(final String value) {
        final String[] values = value.split(" ");
        if (values.length == 3) {
            final String subject = formatValue(values[0]);
            final String predicate = formatValue(values[1]);
            final String object = formatValue(values[2]);

            return String.format("%s %s %s", subject, predicate, object);
        }
        return null;
    }

    private String formatValue(final String value) {
        return value.startsWith("?") ? value : String.format("<%s>", value);
    }

    public String createSelectQueryUsingRandom(final RandomUUID randomUUID) {
        return createSelectQuery(createRandomValues(randomUUID));
    }

    public String createSelectQuery(final String condition) {
        final String sparql = String.format("SELECT * WHERE { %s }", formatValues(condition));
        return sparql;
    }

    public String createSelectQueryWithMultipleValues(final String ratio, final RandomUUID hitRandomUUID, final RandomUUID missRandomUUID) {
        final String[] ratioList = ratio.split(":");
        if (ratioList.length != 2) {
            log.error("Invalid ratio value");
        }

        final int numberOfHits = Integer.parseInt(ratioList[0]);
        final int numberOfMisses = Integer.parseInt(ratioList[1]);

        final List<String> queryValues = new ArrayList<>();

        for (int index = 0; index < numberOfHits; index++) {
            queryValues.add(formatValues(createRandomValues(hitRandomUUID)));
        }

        for (int index = 0; index < numberOfMisses; index++) {
            queryValues.add(formatValues(createRandomValues(missRandomUUID)));
        }

        return String.format("SELECT * WHERE { %s ?s ?p ?o }", createValuesSyntax(queryValues));
    }

    public String createSelectQueryWithMultipleValues(final int numberOfValues, final RandomUUID randomUUID) {
        final List<String> queryValues = new ArrayList<>();
        for (int index = 0; index < numberOfValues; index++) {
            queryValues.add(formatValues(createRandomValues(randomUUID)));
        }

        return String.format("SELECT * WHERE { %s ?s ?p ?o }", createValuesSyntax(queryValues));
    }

    public String createValuesSyntax(final List<String> values) {
        final String format = "VALUES (?s ?p ?o)  { \n%s\n }";

        final String valueString = values.stream().map(a -> String.format(" ( %s ) ", a)).collect(Collectors.joining("\n"));

        return String.format(format, valueString);
    }

    private URI getRandomURI(final RandomUUID randomUUID) {
        return VF.createURI(randomUUID.getRandom());
    }

    private Statement getRandomStatement(final RandomUUID randomUUID) {
        final Statement statement = VF.createStatement(getRandomURI(randomUUID), getRandomURI(randomUUID), getRandomURI(randomUUID));
        return statement;
    }

    @Override
    public void processLoadRandom(final String accumuloInfo, final String zookeeperInfo, final String tablePrefix, final String randomInfo, final int numberOfRecords) {
        final RandomUUID randomUUID = RandomUUID.createInstance(randomInfo);

        final Stopwatch watch = Stopwatch.createStarted();

        final SailRepositoryConnection sailConn = createConnection(getConfiguration(accumuloInfo, zookeeperInfo, tablePrefix));

        log.info(String.format("# loading: %,d records into tables(%s)", numberOfRecords, tablePrefix));
        try {
            for (int i = 0; i < numberOfRecords; i++) {
                if ((i > 0) && ((i % 100000) == 0)) {
                    log.info(String.format("- loaded(%s): %,d records (%,d seconds)", tablePrefix, i, watch.elapsed(TimeUnit.SECONDS)));
                }
                sailConn.add(getRandomStatement(randomUUID));
            }
        }
        catch (final RepositoryException e) {
            log.error("Error working with the repository (stop the loop)", e);
        }
        finally {
            log.info(String.format("# completed loading(%s): %,d records (%,d seconds)", tablePrefix, numberOfRecords, watch.elapsed(TimeUnit.SECONDS)));

            Utils.flushConnection(sailConn);
            closeConnection(sailConn);
        }
    }

    @Override
    public void processQueryRandom(final String accumuloInfo, final String zookeeperInfo, final String tablePrefixes, final String randomInfo, final int numberOfIterations) {
        final RandomUUID randomUUID = RandomUUID.createInstance(randomInfo);

        final String sparql = createSelectQueryUsingRandom(randomUUID);

        final String[] prefixes = tablePrefixes.split(":");
        for (final String prefix : prefixes) {

            final SailRepositoryConnection sailConn = createConnection(getConfiguration(accumuloInfo, zookeeperInfo, prefix));

            log.info(String.format("\n# query: tables(%s) random(%s)", prefix, randomInfo));
            try {
                performQuery(sailConn, prefix, sparql, 0, null, numberOfIterations);
            }
            finally {
                closeConnection(sailConn);
            }
        }
    }

    @Override
    public void processQueryRandomValues(String accumuloInfo, String zookeeperInfo, String tablePrefixes, String randomInfo, int numberOfValues, int numberOfIterations) {
        final RandomUUID randomUUID = RandomUUID.createInstance(randomInfo);

        final String sparql = createSelectQueryWithMultipleValues(numberOfValues, randomUUID);

        final String[] prefixes = tablePrefixes.split(":");
        for (final String prefix : prefixes) {
            log.info(String.format("\n# query: tables(%s) random(%s)", prefix, randomInfo));

            final SailRepositoryConnection sailConn = createConnection(getConfiguration(accumuloInfo, zookeeperInfo, prefix));
            try {
                performQuery(sailConn, prefix, sparql, numberOfValues, null, numberOfIterations);
            }
            finally {
                closeConnection(sailConn);
            }
        }
    }

    @Override
    public void processQueryRandomValuesRatio(final String accumuloInfo, final String zookeeperInfo, final String tablePrefixes, final String randomInfoHit, final String randomInfoMiss, final String ratio, final int numberOfIterations) {
        final RandomUUID hitRandomUUID = RandomUUID.createInstance(randomInfoHit);
        final RandomUUID missRandomUUID = RandomUUID.createInstance(randomInfoMiss);

        final String sparql = createSelectQueryWithMultipleValues(ratio, hitRandomUUID, missRandomUUID);

        final String[] prefixes = tablePrefixes.split(":");
        for (final String prefix : prefixes) {
            log.info(String.format("\n# query: tables(%s) randomH(%s) randomM(%s) ratio(%s)", prefix, randomInfoHit, randomInfoMiss, ratio));

            final SailRepositoryConnection sailConn = createConnection(getConfiguration(accumuloInfo, zookeeperInfo, prefix));
            try {
                performQuery(sailConn, prefix, sparql, 0, ratio, numberOfIterations);
            }
            finally {
                closeConnection(sailConn);
            }
        }
    }

    @Override
    public void processQuery(final String accumuloInfo, final String zookeeperInfo, final String tablePrefixes, final String condition) {
        final Stopwatch watch = Stopwatch.createStarted();

        final String sparql = createSelectQuery(condition);

        final String[] prefixes = tablePrefixes.split(":");
        for (final String prefix : prefixes) {
            final SailRepositoryConnection sailConn = createConnection(getConfiguration(accumuloInfo, zookeeperInfo, prefix));

            log.info(String.format("\n# query: tables(%s)", prefix));
            try {
                log.info(sailConn.prepareTupleQuery(QueryLanguage.SPARQL, sparql));
                Utils.evaluateAndCount(sparql, sailConn);
            }
            catch (final MalformedQueryException | RepositoryException e) {
                log.error("Failed to prepare query", e);
            }
            catch (final QueryEvaluationException | QueryResultHandlerException e) {
                log.error("Failed to evaluate and count", e);
            }
            finally {
                log.info(String.format("csv(ms), %,d, %s)", watch.elapsed(TimeUnit.MILLISECONDS), condition));

                closeConnection(sailConn);
            }
        }
    }
}
