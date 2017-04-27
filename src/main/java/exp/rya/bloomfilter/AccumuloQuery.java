package exp.rya.bloomfilter;

import static org.apache.rya.api.RdfCloudTripleStoreConstants.DELIM_BYTES;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.BatchScanner;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.data.Range;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;
import org.apache.rya.indexing.accumulo.ConfigUtils;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryResultHandlerException;
import org.openrdf.repository.RepositoryException;

import com.google.common.base.Stopwatch;
import com.google.common.primitives.Bytes;

import exp.rya.bloomfilter.utils.RandomUUID;
import exp.rya.bloomfilter.utils.Utils;

public class AccumuloQuery extends Query {
    private static final Logger log = Logger.getLogger(AccumuloQuery.class);

    private List<Range> createRangesWithMultipleValues(final int numberOfValues, final RandomUUID randomUUID) {
        // byte array containing delimiters appended to the end of the Text(row)
        final byte[] type = new byte[] { 1, 2 };

        final List<Range> queryValues = new ArrayList<>();

        for (int index = 0; index < numberOfValues; index++) {
            final String s = randomUUID.getRandom();
            final String p = randomUUID.getRandom();
            final String o = randomUUID.getRandom();

            // from accumulo table _spo -> <s>\x00<p>\x00<o>\x01\x02 : []
            final byte[] row = Bytes.concat(s.getBytes(), DELIM_BYTES, p.getBytes(), DELIM_BYTES, o.getBytes(), type);
            queryValues.add(Range.exact(new Text(row)));
        }

        return queryValues;
    }

    public List<Range> createRangesWithMultipleValues(final String ratio, final RandomUUID hitRandomUUID, final RandomUUID missRandomUUID) {
        final String[] ratioList = ratio.split(":");
        if (ratioList.length != 2) {
            log.error("Invalid ratio value");
        }

        final int numberOfHits = Integer.parseInt(ratioList[0]);
        final int numberOfMisses = Integer.parseInt(ratioList[1]);

        final List<Range> queryValues = new ArrayList<>();
        queryValues.addAll(createRangesWithMultipleValues(numberOfHits, hitRandomUUID));
        queryValues.addAll(createRangesWithMultipleValues(numberOfMisses, missRandomUUID));

        return queryValues;
    }

    private void performQuery(final BatchScanner scanner, final String tablePrefix, final List<Range> ranges, final int numberOfValues, final String ratio, final int numberOfIterations) {
        for (int iteration = 0; iteration < numberOfIterations; iteration++) {
            final Stopwatch watch = Stopwatch.createStarted();
            try {
                executeQuery(scanner, ranges);
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

    private void executeQuery(final BatchScanner scanner, final List<Range> ranges) throws MalformedQueryException, RepositoryException, QueryEvaluationException, QueryResultHandlerException {
        Utils.evaluateAndCount(scanner, ranges);
    }

    @Override
    public void processQueryRandomValues(String accumuloInfo, String zookeeperInfo, String tablePrefixes, String randomInfo, int numberOfValues, int numberOfIterations) {
        final RandomUUID randomUUID = RandomUUID.createInstance(randomInfo);

        final List<Range> ranges = createRangesWithMultipleValues(numberOfValues, randomUUID);

        final String[] prefixes = tablePrefixes.split(":");
        for (final String prefix : prefixes) {
            log.info(String.format("\n# query(accumulo):: tables(%s) random(%s)", prefix, randomInfo));

            final Configuration conf = getConfiguration(accumuloInfo, zookeeperInfo, prefix);
            final String tablename = prefix + "spo";

            BatchScanner scanner = null;
            try {
                scanner = ConfigUtils.createBatchScanner(tablename, conf);
                performQuery(scanner, prefix, ranges, numberOfValues, null, numberOfIterations);
            }
            catch (final AccumuloException | AccumuloSecurityException | TableNotFoundException e) {
                log.error("Unable to query for information", e);
            }
            finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
        }
    }

    @Override
    public void processQueryRandom(String accumuloInfo, String zookeeperInfo, String tablePrefixes, String randomInfo, int numberOfIterations) {
        final RandomUUID randomUUID = RandomUUID.createInstance(randomInfo);

        final List<Range> ranges = createRangesWithMultipleValues(1, randomUUID);

        final String[] prefixes = tablePrefixes.split(":");
        for (final String prefix : prefixes) {
            log.info(String.format("\n# query(accumulo):: tables(%s) random(%s)", prefix, randomInfo));

            final Configuration conf = getConfiguration(accumuloInfo, zookeeperInfo, prefix);
            final String tablename = prefix + "spo";

            BatchScanner scanner = null;
            try {
                scanner = ConfigUtils.createBatchScanner(tablename, conf);
                performQuery(scanner, prefix, ranges, 1, null, numberOfIterations);
            }
            catch (final AccumuloException | AccumuloSecurityException | TableNotFoundException e) {
                log.error("Unable to query for information", e);
            }
            finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
        }
    }

    @Override
    public void processQueryRandomValuesRatio(String accumuloInfo, String zookeeperInfo, String tablePrefixes, String randomInfoHit, String randomInfoMiss, String ratio, int numberOfIterations) {
        final RandomUUID hitRandomUUID = RandomUUID.createInstance(randomInfoHit);
        final RandomUUID missRandomUUID = RandomUUID.createInstance(randomInfoMiss);

        final List<Range> ranges = createRangesWithMultipleValues(ratio, hitRandomUUID, missRandomUUID);

        final String[] prefixes = tablePrefixes.split(":");
        for (final String prefix : prefixes) {
            log.info(String.format("\n# query(accumulo): tables(%s) randomH(%s) randomM(%s) ratio(%s)", prefix, randomInfoHit, randomInfoMiss, ratio));

            final Configuration conf = getConfiguration(accumuloInfo, zookeeperInfo, prefix);
            final String tablename = prefix + "spo";

            BatchScanner scanner = null;
            try {
                scanner = ConfigUtils.createBatchScanner(tablename, conf);
                performQuery(scanner, prefix, ranges, 0, ratio, numberOfIterations);
            }
            catch (final AccumuloException | AccumuloSecurityException | TableNotFoundException e) {
                log.error("Unable to query for information", e);
            }
            finally {
                if (scanner != null) {
                    scanner.close();
                }
            }
        }
    }

    @Override
    public void processQuery(final String accumuloInfo, final String zookeeperInfo, final String tablePrefixes, final String condition) {
        log.info("Unsupported Operation");
    }

    @Override
    public void processLoadRandom(String accumuloInfo, String zookeeperInfo, String tablePrefixes, String randomInfo, int numberOfRecords) {
        log.info("Unsupported Operation");
    }
}
