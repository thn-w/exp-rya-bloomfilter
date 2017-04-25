package exp.rya.bloomfilter;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;
import org.apache.rya.accumulo.AccumuloRdfConfiguration;
import org.apache.rya.api.RdfCloudTripleStoreConfiguration;
import org.apache.rya.indexing.accumulo.ConfigUtils;

import com.google.common.base.Stopwatch;

import exp.rya.bloomfilter.utils.RandomUUID;

public abstract class Query {
	private static final Logger log = Logger.getLogger(Query.class);

	protected Query() {
	}

	protected Query(final Configuration config) {
	}

	public static Query createInstance(final String queryType) {
		Query query = null;

		switch(queryType) {
		case QueryType.RYA:
			query = new RyaQuery();
			break;

		case QueryType.ACCUMULO:
			query = new AccumuloQuery();
			break;

		default:
			log.error(String.format("Unsupported query type(%s)", queryType));
			break;
		}

		return query;
	}

	protected Configuration getConfiguration(final String accumulo, final String zookeepers, final String tablePrefix) {
		final AccumuloRdfConfiguration conf = new AccumuloRdfConfiguration();
		conf.set(RdfCloudTripleStoreConfiguration.CONF_TBL_PREFIX, tablePrefix);

		// format -> host1:port1[,host2:port2]
		conf.set(ConfigUtils.CLOUDBASE_ZOOKEEPERS, zookeepers);

		// format -> instance:username:password
		final String[] accumuloInfo = accumulo.split(":");
		if (accumuloInfo.length == 3) {
			conf.set(ConfigUtils.CLOUDBASE_INSTANCE, accumuloInfo[0]);
			conf.set(ConfigUtils.CLOUDBASE_USER, accumuloInfo[1]);
			conf.set(ConfigUtils.CLOUDBASE_PASSWORD, accumuloInfo[2]);
		}
		else {
			log.error("Invalid accumulo information");
		}

		return conf;
	}

	// format: U:<value> U:<value> U:<value>
	protected String createRandomValues(final RandomUUID randomUUID) {
		return String.format("%s %s %s", randomUUID.getRandom(), randomUUID.getRandom(), randomUUID.getRandom());
	}

	public void processShowRandom(final String randomInfo, final int numberOfRecords) {
		log.info(String.format("\n# showing (%,d records with %s)", numberOfRecords, randomInfo));

		final RandomUUID randomUUID = RandomUUID.createInstance(randomInfo);

		final Stopwatch watch = Stopwatch.createStarted();

		for (int i = 0; i < numberOfRecords; i++) {
			log.info(String.format("- %s", createRandomValues(randomUUID)));
		}
		log.info(String.format("# generated: %,d records (%d ms)", numberOfRecords, watch.elapsed(TimeUnit.MILLISECONDS)));
	}

	public void processSaveRandom(final String randomInfo, final int numberOfRecords, final String outputFile) {
		log.info(String.format("\n# saving at %s (%,d records based on %s)", outputFile, numberOfRecords, randomInfo));

		final RandomUUID randomUUID = RandomUUID.createInstance(randomInfo);

		final Stopwatch watch = Stopwatch.createStarted();

		final StringBuilder records = new StringBuilder(numberOfRecords);
		for (int i = 0; i < numberOfRecords; i++) {
			if (i == (numberOfRecords - 1)) {
				records.append(String.format("%s", createRandomValues(randomUUID)));
			}
			else {
				records.append(String.format("%s\n", createRandomValues(randomUUID)));
			}
		}

		try {
			FileUtils.writeStringToFile(new File(outputFile), records.toString());
		}
		catch (final IOException e) {
			log.error(String.format("Unable to save to: %s", outputFile), e);
		}

		log.info(String.format("# generated: %,d records (%d ms)", numberOfRecords, watch.elapsed(TimeUnit.MILLISECONDS)));
	}

	public abstract void processQuery(final String accumuloInfo, final String zookeeperInfo, final String tablePrefixes, final String condition);
	public abstract void processLoadRandom(final String accumuloInfo, final String zookeeperInfo, final String tablePrefixes, final String randomInfo, final int numberOfRecords);
	public abstract void processQueryRandom(final String accumuloInfo, final String zookeeperInfo, final String tablePrefixes, final String randomInfo, final int numberOfIterations);
	public abstract void processQueryRandomValues(final String accumuloInfo, final String zookeeperInfo, final String tablePrefixes, final String randomInfo, final int numberOfValues, final int numberOfIterations);
	public abstract void processQueryRandomValuesRatio(final String accumuloInfo, final String zookeeperInfo, final String tablePrefixes, final String randomInfoHit, final String randomInfoMiss, final String ratio, final int numberOfIterations);

	private interface QueryType {
		public static final String RYA = "rya";
		public static final String ACCUMULO = "accumulo";
	}
}
