package edu.java.bot.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {
    private Cache<String, Bucket> buckets;

    @Value("${bucket.capacity}")
    private int capacity;
    @Value("${bucket.refill.size}")
    private int refillSize;
    @Value("${bucket.refill.interval}")
    private int refillInterval;
    @Value("${bucket.cache.max-size}")
    private long maxSize;
    @Value("${bucket.cache.expiration-time}")
    private long expirationTime;

    @PostConstruct
    void createCache() {
        buckets = Caffeine.newBuilder()
            .maximumSize(maxSize)
            .expireAfterAccess(expirationTime, TimeUnit.MINUTES)
            .build();
    }

    public Bucket catchBucket(String ip) {
        Bucket bucket = buckets.getIfPresent(ip);
        if (bucket == null) {
            Refill refill = Refill.intervally(refillSize, Duration.ofMinutes(refillInterval));
            Bandwidth limit = Bandwidth.classic(capacity, refill);
            bucket = Bucket.builder()
                .addLimit(limit)
                .build();
            buckets.put(ip, bucket);
        }
        return bucket;
    }
}
