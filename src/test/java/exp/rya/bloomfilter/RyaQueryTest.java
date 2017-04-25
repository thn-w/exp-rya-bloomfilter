package exp.rya.bloomfilter;

import org.apache.log4j.Logger;
import org.junit.Test;

import exp.rya.bloomfilter.utils.RandomUUID;

public class RyaQueryTest {
    private static final Logger log = Logger.getLogger(RyaQueryTest.class);

    @Test
    public void createSelectQueryWithMultipleValues() {
        final RyaQuery ryaQuery = new RyaQuery();
        final RandomUUID randomUUID = RandomUUID.createInstance("5:0:10:test");
        log.info(ryaQuery.createSelectQueryWithMultipleValues(3, randomUUID));
    }

}
