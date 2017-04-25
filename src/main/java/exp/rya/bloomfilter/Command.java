package exp.rya.bloomfilter;

public interface Command {
    // random dataset using UUID
    public static final String LOAD_RANDOM = "load.random";
    public static final String SHOW_RANDOM = "show.random";
    public static final String SAVE_RANDOM = "save.random";

    public static final String LOAD_FILE = "load.file";

    public static final String QUERY = "query";
    public static final String QUERY_RANDOM = "query.random";
    public static final String QUERY_RANDOM_VALUES = "query.random.values";
    public static final String QUERY_RANDOM_VALUES_DIRECT = "query.random.values.direct";

    // <% hits>:<% no hits>
    public static final String QUERY_RANDOM_VALUES_RATIO = "query.random.values.ratio";
}
