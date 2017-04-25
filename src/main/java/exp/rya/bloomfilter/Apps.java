package exp.rya.bloomfilter;

import org.apache.log4j.Logger;

public class Apps {
	private static final Logger log = Logger.getLogger(Apps.class);
	private Query query;


	public Apps() {
		this.query = null;
	}

	public void process(String[] args) {
		if (args.length > 0) {
			query = Query.createInstance(args[0]);
			if (query != null) {
				final String command = args[1];
				switch (command) {
				case Command.LOAD_RANDOM:
					if (args.length > 6) {
						final String accumuloInfo = args[2];
						final String zookeeperInfo = args[3];
						final String tablePrefix = args[4];
						final String randomInfo = args[5];
						final int numberOfRecords = Integer.parseInt(args[6]);
						query.processLoadRandom(accumuloInfo, zookeeperInfo, tablePrefix, randomInfo, numberOfRecords);
					}
					break;

				case Command.SHOW_RANDOM:
					if (args.length > 3) {
						final String randomInfo = args[2];
						final int numberOfRecords = Integer.parseInt(args[3]);
						query.processShowRandom(randomInfo, numberOfRecords);
					}
					break;

				case Command.SAVE_RANDOM:
					if (args.length > 4) {
						final String randomInfo = args[2];
						final int numberOfRecords = Integer.parseInt(args[3]);
						final String outputFile = args[4];
						query.processSaveRandom(randomInfo, numberOfRecords, outputFile);
					}
					break;

				case Command.QUERY:
					if (args.length > 5) {
						final String accumuloInfo = args[2];
						final String zookeeperInfo = args[3];
						final String tablePrefix = args[4];
						final String condition = args[5];
						query.processQuery(accumuloInfo, zookeeperInfo, tablePrefix, condition);
					}
					break;

				case Command.QUERY_RANDOM:
					if (args.length > 5) {
						final String accumuloInfo = args[2];
						final String zookeeperInfo = args[3];
						final String tablePrefixes = args[4];
						final String randomInfo = args[5];

						int numberOfIterations = 1;
						if (args.length > 6) {
							numberOfIterations = Integer.parseInt(args[6]);
						}

						query.processQueryRandom(accumuloInfo, zookeeperInfo, tablePrefixes, randomInfo, numberOfIterations);
					}
					break;

				case Command.QUERY_RANDOM_VALUES:
					if (args.length > 6) {
						final String accumuloInfo = args[2];
						final String zookeeperInfo = args[3];
						final String tablePrefixes = args[4];
						final String randomInfo = args[5];
						final int numberOfValues = Integer.parseInt(args[6]);

						int numberOfIterations = 1;
						if (args.length > 7) {
							numberOfIterations = Integer.parseInt(args[7]);
						}

						query.processQueryRandomValues(accumuloInfo, zookeeperInfo, tablePrefixes, randomInfo, numberOfValues, numberOfIterations);
					}
					break;

				case Command.QUERY_RANDOM_VALUES_RATIO:
					if (args.length > 7) {
						final String accumuloInfo = args[2];
						final String zookeeperInfo = args[3];
						final String tablePrefixes = args[4];
						final String randomInfoHit = args[5];
						final String randomInfoMiss = args[6];
						final String ratio = args[7];

						int numberOfIterations = 1;
						if (args.length > 8) {
							numberOfIterations = Integer.parseInt(args[7]);
						}

						query.processQueryRandomValuesRatio(accumuloInfo, zookeeperInfo, tablePrefixes, randomInfoHit, randomInfoMiss, ratio, numberOfIterations);
					}
					break;

				default:
					log.info(String.format("Unsupported command: %s", command));
					displayUsage();
				}
			}
		}
		else {
			displayUsage();
		}
	}

	public static void main(String[] args) throws Exception {
		final Apps apps = new Apps();
		apps.process(args);
	}

	public static void displayUsage() {
		final StringBuilder help = new StringBuilder();
		help.append("\nOptions: <query type> <command> <command options>");
		help.append("\n");

		help.append("\n# Query types");
		help.append("\n- rya <command> <command options>");
		help.append("\n- accumulo <command> <command options>");
		help.append("\n");

		help.append("\n# Query related commands");
		help.append("\n- show.random <(random) seed:min:max:prefix> <number of records>");
		help.append("\n- save.random <(random) seed:min:max:prefix> <number of records> <output file>");
		help.append("\n- load.random <(accumulo) instance:username:password> <(zookeepers) host1:port1[,host2:port2]> <table prefix> <(random) seed:min:max:prefix> <number of records>");
		help.append("\n");

		help.append("\n# Query related commands");
		help.append("\n- query <(accumulo) instance:username:password> <(zookeepers) host1:port1[,host2:port2]> <table prefix> <query values>");
		help.append("\n- query.random <(accumulo) instance:username:password> <(zookeepers) host1:port1[,host2:port2]> <table prefix> <(random) seed:min:max:prefix> [number of iterations]");
		help.append("\n- query.random.values <(accumulo) instance:username:password> <(zookeepers) host1:port1[,host2:port2]> <table prefix> <(random) seed:min:max:prefix> <number of values> [number of iterations]");
		help.append("\n- query.random.values.direct <(accumulo) instance:username:password> <(zookeepers) host1:port1[,host2:port2]> <table prefix> <(random) seed:min:max:prefix> <number of values> [number of iterations]");
		help.append("\n- query.random.values.ratio <(accumulo) instance:username:password> <(zookeepers) host1:port1[,host2:port2]> <table prefix> <(random hit) seed:min:max:prefix> <(random miss) seed:min:max:prefix> <(ratio) hits:misses> [number of iterations]");
		help.append("\n");

		System.out.println(help.toString());
	}
}
