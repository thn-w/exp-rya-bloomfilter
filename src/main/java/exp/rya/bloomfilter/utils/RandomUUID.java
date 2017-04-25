package exp.rya.bloomfilter.utils;

import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;

public class RandomUUID {
    private static final Logger log = Logger.getLogger(RandomUUID.class);

    private final String prefix;

    private final Random random;
    private final int min;
    private final int max;

    public RandomUUID() {
        this(1, 0, 10, "random");
    }

    public RandomUUID(final long seed, final int min, final int max, final String prefix) {
        random = new Random(seed);
        this.min = min;
        this.max = max;

        this.prefix = prefix;
    }

    public String nextString() {
        final String value = String.format("%s - %,d", prefix, nextInt());
        return UUID.nameUUIDFromBytes(value.getBytes()).toString();
    }

    public int nextInt() {
        return random.nextInt((max - min) + 1) + min;
    }

    public String getRandom() {
        return getRandom("U");
    }

    public String getRandom(final String prefix) {
        return String.format("%s:%s", prefix, nextString());
    }

    public static RandomUUID createInstance(final String randomData) {
        // format -> seed:min:max:prefix
        final String[] randomInfo = randomData.split(":");
        if (randomInfo.length == 4) {
            final long seed = Long.parseLong(randomInfo[0]);
            final int min = Integer.parseInt(randomInfo[1]);
            final int max = Integer.parseInt(randomInfo[2]);
            final String prefix = randomInfo[3];

            return new RandomUUID(seed, min, max, prefix);
        }
        else {
            log.error("Unable to create a randomUUID generator");
        }
        return null;
    }
}
