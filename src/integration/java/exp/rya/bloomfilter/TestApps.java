package exp.rya.bloomfilter;

public class TestApps {
    // private static final String ACCUMULO_INFO = "thn-acc:test:test100";
    // private static final String ZOOKEEPER_INFO = "localhost:2181";

    private static final String ACCUMULO_INFO = "spear_instance:SPEAR:spear";
    private static final String ZOOKEEPER_INFO = "zoo1:2181,zoo2:2181";

    private static final String RANDOM_INFO = "8:0:1000000000:test";
    private static final String NUMBER_OF_RECORDS = "1000";

    public static void main(String[] args) throws Exception {
        // loadData("nbf_1k_", "1000");
        // loadData("nbf_100k_", "100000");
        // loadData("nbf_10m_", "10000000");

        queryRandom("nbf_1k_", 3);

        // queryRandom("nbf_10m_:nbf_100k_:nbf_1k_", 100);
        // queryRandom("nbf_1k_:nbf_100k_:nbf_10m_", 100);

        // loadData("wbf_1k_", "1000");
        // loadData("wbf_100k_", "100000");
        // loadData("wbf_10m_", "10000000");

        // show();
        // saveRandom();

        // queryFull();
        // queryBySubject();
        // queryBySubjectPredicate();
        // queryByPredicateObject();
    }

    private static void loadData(final String tablePrefix, final String numberOfRecords) {
        final String[] inputParams = { Command.LOAD_RANDOM, ACCUMULO_INFO, ZOOKEEPER_INFO, tablePrefix, RANDOM_INFO, numberOfRecords };
        run(inputParams);
    }

    private static void queryRandom(final String tablePrefixes, final int numberOfTimes) {
        for (int index = 0; index < numberOfTimes; index++) {
            queryRandom(tablePrefixes);
        }
    }

    private static void queryRandom(final String tablePrefixes) {
        final String[] inputParams = { Command.QUERY_RANDOM, ACCUMULO_INFO, ZOOKEEPER_INFO, tablePrefixes, RANDOM_INFO };
        run(inputParams);
    }

    private static void queryFull(final String tablePrefix) {
        final String query = "U:875b2c37-73a3-388e-8bb3-4d8327ca44ff U:b40e449f-50f7-3ed2-999f-02b40816d05c U:b40e449f-50f7-3ed2-999f-02b40816d05c";
        final String[] inputParams = { Command.QUERY, ACCUMULO_INFO, ZOOKEEPER_INFO, tablePrefix, query };
        run(inputParams);
    }

    private static void queryBySubject(final String tablePrefix) {
        final String query = "U:875b2c37-73a3-388e-8bb3-4d8327ca44ff ?p ?o";
        final String[] inputParams = { Command.QUERY, ACCUMULO_INFO, ZOOKEEPER_INFO, tablePrefix, query };
        run(inputParams);
    }

    private static void queryBySubjectPredicate(final String tablePrefix) {
        final String query = "U:875b2c37-73a3-388e-8bb3-4d8327ca44ff U:b40e449f-50f7-3ed2-999f-02b40816d05c ?o";
        final String[] inputParams = { Command.QUERY, ACCUMULO_INFO, ZOOKEEPER_INFO, tablePrefix, query };
        run(inputParams);
    }

    private static void queryByPredicateObject(final String tablePrefix) {
        final String query = "U:b40e449f-50f7-3ed2-999f-02b40816d05c U:b40e449f-50f7-3ed2-999f-02b40816d05c ?s";
        final String[] inputParams = { Command.QUERY, ACCUMULO_INFO, ZOOKEEPER_INFO, tablePrefix, query };
        run(inputParams);
    }

    private static void show() {
        final String[] inputParams = { Command.SHOW_RANDOM, RANDOM_INFO, NUMBER_OF_RECORDS };
        run(inputParams);
    }

    private static void saveRandom() {
        final String[] inputParams = { Command.SAVE_RANDOM, RANDOM_INFO, NUMBER_OF_RECORDS, String.format("random-triples-%s.txt", NUMBER_OF_RECORDS) };
        run(inputParams);
    }

    private static void run(final String[] args) {
        final Apps apps = new Apps();
        apps.process(args);
    }

}
