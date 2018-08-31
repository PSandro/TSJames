package eu.psandro.tsjames.io.protocol;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import lombok.NonNull;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ResponseManager implements AutoCloseable {


    private Set<Integer> recycledIds = new HashSet<>();
    private AtomicInteger idCounter = new AtomicInteger(Integer.MIN_VALUE);
    private final ExecutorService workerService = Executors.newCachedThreadPool();

    private final Cache<Integer, ResponseCall> bindings = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .removalListener((RemovalListener<Integer, ResponseCall>) removalNotification -> {
                recycledIds.add(removalNotification.getKey());
                ResponseManager.this.tryCleanup();
            })
            .build();


    public int register(@NonNull ResponseCall responseCall) {
        final int id = this.nexId();
        this.bindings.put(id, responseCall);
        return id;
    }

    public RespondIdReservationHook getReservationHook() {
        return ResponseManager.this::register;
    }

    public boolean call(@NonNull Integer integer, @NonNull NetPacket netPacket) {
        final ResponseCall responseCall = this.bindings.getIfPresent(integer);
        if (responseCall == null) return false;
        this.workerService.submit(() -> responseCall.onCall(netPacket));
        return true;
    }

    private int nexId() {
        if (!recycledIds.isEmpty()) {
            return recycledIds.iterator().next();
        } else {
            return idCounter.getAndIncrement();
        }
    }

    private void tryCleanup() {
        if ((this.recycledIds.size() + Integer.MIN_VALUE) == this.idCounter.get()) {
            this.recycledIds.clear();
            this.idCounter.set(Integer.MIN_VALUE);
        }
    }

    @Override
    public void close() {
        this.recycledIds.clear();
        this.workerService.shutdown();
        this.bindings.invalidateAll();
        this.bindings.cleanUp();
        this.idCounter.set(Integer.MIN_VALUE);
    }
}
