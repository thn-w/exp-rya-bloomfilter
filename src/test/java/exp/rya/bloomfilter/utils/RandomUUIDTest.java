package exp.rya.bloomfilter.utils;

import org.apache.log4j.Logger;
import org.junit.Test;

import exp.rya.bloomfilter.utils.RandomUUID;

public class RandomUUIDTest {
    private static final Logger log = Logger.getLogger(RandomUUIDTest.class);

    private RandomUUID create(final long seed, final int min, final int max, final String prefix) {
        return new RandomUUID(seed, min, max, prefix);
    }

    @Test
    public void testRange() {
        displayRange(5, 5, 0, 100, "thn");
        displayRange(5, 5, 0, 100, "thn");

        displayRange(6, 6, 0, 10000, "test2");
        displayRange(6, 6, 0, 10000, "test2");

        displayRange(10, 10, 0, 10000, "test2");
        displayRange(10, 10, 0, 10000, "test2");
    }

    private void displayRange(final int size, final long seed, final int min, final int max, final String prefix) {
        log.info(String.format("\n--- size(%d)", size));

        final RandomUUID randomUUID = create(seed, min, max, prefix);

        for (int index = 0; index < size; index++) {
            log.info(String.format("[%d] uuid(%s) i(%d)", index, randomUUID.nextString(), randomUUID.nextInt()));
        }
    }
}
