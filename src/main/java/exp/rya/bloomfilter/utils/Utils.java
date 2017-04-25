package exp.rya.bloomfilter.utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchScanner;
import org.apache.accumulo.core.data.Range;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;
import org.apache.rya.accumulo.AccumuloRdfConfiguration;
import org.apache.rya.accumulo.AccumuloRyaDAO;
import org.apache.rya.api.RdfCloudTripleStoreConfiguration;
import org.apache.rya.api.persist.RyaDAO;
import org.apache.rya.api.persist.RyaDAOException;
import org.apache.rya.indexing.accumulo.ConfigUtils;
import org.apache.rya.rdftriplestore.RdfCloudTripleStore;
import org.apache.rya.rdftriplestore.inference.InferenceEngineException;
import org.apache.rya.sail.config.RyaSailFactory;
import org.openrdf.model.Statement;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.QueryResultHandlerException;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.resultio.helpers.QueryResultCollector;
import org.openrdf.query.resultio.text.csv.SPARQLResultsCSVWriter;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.repository.sail.SailRepositoryConnection;
import org.openrdf.sail.Sail;
import org.openrdf.sail.SailException;
import org.openrdf.sail.memory.MemoryStore;

public class Utils {
    private static final Logger log = Logger.getLogger(Utils.class);

    public static SailRepositoryConnection getInMemoryConn() throws RepositoryException {
        final SailRepository s = new SailRepository(new MemoryStore());
        s.initialize();

        final SailRepositoryConnection conn = s.getConnection();
        return conn;
    }

    public static void flushConnection(SailRepositoryConnection conn) {
        final Sail sail = ((SailRepository) conn.getRepository()).getSail();

        if (sail instanceof RdfCloudTripleStore) {
            final RdfCloudTripleStore ryaSail = (RdfCloudTripleStore) sail;

            @SuppressWarnings("rawtypes")
            final RyaDAO ryaDAO = ryaSail.getRyaDAO();
            if (ryaDAO instanceof AccumuloRyaDAO) {
                final AccumuloRyaDAO accRyaDAO = (AccumuloRyaDAO) ryaDAO;
                try {
                    accRyaDAO.flush();
                }
                catch (final RyaDAOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static SailRepositoryConnection getInMemoryAccConn(boolean disableAutoFlushing) throws RepositoryException, SailException, AccumuloException, AccumuloSecurityException, RyaDAOException, InferenceEngineException {
        return getInMemoryAccConn(getConf(), disableAutoFlushing);
    }

    public static SailRepositoryConnection getInMemoryAccConn(final Configuration conf, final boolean disableAutoFlushing) throws RepositoryException, SailException, AccumuloException, AccumuloSecurityException, RyaDAOException, InferenceEngineException {
        if (disableAutoFlushing) {
            ((AccumuloRdfConfiguration) conf).setFlush(false);
        }
        conf.setBoolean(ConfigUtils.DISPLAY_QUERY_PLAN, true);

        final Sail extSail = RyaSailFactory.getInstance(conf);
        final SailRepository repository = new SailRepository(extSail);

        final SailRepositoryConnection conn = repository.getConnection();
        return conn;
    }

    public static void evaluateAndPrint(String sparqlQuery, SailRepositoryConnection conn) throws TupleQueryResultHandlerException, QueryEvaluationException, MalformedQueryException, RepositoryException {
        final SPARQLResultsCSVWriter writer = new SPARQLResultsCSVWriter(System.out);
        conn.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery).evaluate(writer);
    }

    public static void evaluateAndCount(String sparqlQuery, SailRepositoryConnection conn) throws QueryEvaluationException, MalformedQueryException, RepositoryException, QueryResultHandlerException {
        final QueryResultCollector c = new QueryResultCollector();
        conn.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery).evaluate(c);

        log.info("Result Count :: " + c.getBindingSets().size());
    }

    public static void evaluateAndCount(final BatchScanner scanner, final List<Range> ranges) {
        scanner.setRanges(ranges);

        final AtomicInteger i = new AtomicInteger();
        scanner.forEach(e -> i.incrementAndGet());

        log.info("Result Count :: " + i.get());
    }

    public static void printRepo(SailRepositoryConnection conn) throws RepositoryException {
        final RepositoryResult<Statement> rr = conn.getStatements(null, null, null, false);
        while (rr.hasNext()) {
            System.out.println(rr.next());
        }

        log.info("Repo size :: " + conn.size());
    }

    private static Configuration getConf() {
        final AccumuloRdfConfiguration conf = new AccumuloRdfConfiguration();
        conf.set(RdfCloudTripleStoreConfiguration.CONF_TBL_PREFIX, "THN_");
        conf.set(ConfigUtils.CLOUDBASE_USER, "test");
        conf.set(ConfigUtils.CLOUDBASE_PASSWORD, "test100");
        conf.set(ConfigUtils.CLOUDBASE_INSTANCE, "thn-acc");
        conf.set(ConfigUtils.CLOUDBASE_ZOOKEEPERS, "localhost");
        return conf;
    }
}
