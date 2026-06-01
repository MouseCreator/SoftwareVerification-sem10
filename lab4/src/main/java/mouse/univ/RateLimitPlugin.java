package mouse.univ;

import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RateLimitPlugin {

    private final double rate;
    private final int burst;
    private final Clock clock;
    private final AtomicBoolean active;

    private final Map<String, Bucket> buckets = new HashMap<>();

    public RateLimitPlugin(double rate, int burst) {
        this(rate, burst, Clock.systemUTC());
    }

    public RateLimitPlugin(double rate, int burst, Clock clock) {
        if (rate <= 0) {
            throw new IllegalArgumentException("rate must be positive");
        }
        if (burst <= 0) {
            throw new IllegalArgumentException("burst must be positive");
        }

        this.rate = rate;
        this.burst = burst;
        this.clock = clock;
        this.active = new AtomicBoolean(true);
    }

    public synchronized boolean accept(String ip) {
        if (!active.get()) {
            return true;
        }
        Bucket bucket = buckets.computeIfAbsent(ip, key -> new Bucket(burst, clock.instant()));

        Instant now = clock.instant();

        double elapsedSeconds =
                (now.toEpochMilli() - bucket.lastRefill.toEpochMilli()) / 1000.0;

        bucket.tokens = Math.min(burst, bucket.tokens + elapsedSeconds * rate);
        bucket.lastRefill = now;

        if (bucket.tokens < 1.0) {
            return false;
        }

        bucket.tokens -= 1.0;
        return true;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public boolean isActive() {
        return this.active.get();
    }

    public synchronized void reset() {
        this.buckets.clear();
    }

    private static class Bucket {
        double tokens;
        Instant lastRefill;

        Bucket(double tokens, Instant lastRefill) {
            this.tokens = tokens;
            this.lastRefill = lastRefill;
        }
    }

}
