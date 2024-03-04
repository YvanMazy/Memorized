package be.darkkraft.memorized.data.map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Interface representing a map with asynchronous and blocking operations.
 *
 * @param <K> the type of key this map holds
 * @param <V> the type of value this map holds
 *
 * @see CompletableFuture
 */
public interface MemorizedMap<K, V> {

    /**
     * Retrieves a value associated with a key in a blocking manner.
     *
     * @param key the key to search for
     *
     * @return the value associated with the key
     */
    default V blockingGet(final @NotNull K key) {
        return this.asyncGet(key).join();
    }

    /**
     * Retrieves a value associated with a key asynchronously.
     *
     * @param key the key to search for
     *
     * @return a {@link CompletableFuture} that will be completed with the value associated with the key
     */
    @NotNull CompletableFuture<V> asyncGet(final @NotNull K key);

    /**
     * Inserts a key-value pair into the map.
     *
     * @param key   the key to insert
     * @param value the value to associate with the key
     */
    void put(final @NotNull K key, final @Nullable V value);

    /**
     * Removes a key-value pair from the map.
     *
     * @param key the key to remove
     */
    void remove(final @NotNull K key);

}