package be.darkkraft.memorized.data.counter;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Interface representing an integer counter with various asynchronous and blocking operations.
 *
 * @see CompletableFuture
 */
public interface IntCounter {

    /**
     * Retrieves the counter value in a blocking manner.
     *
     * @return the current counter value
     */
    default int blockingGet() {
        return this.asyncGet().join();
    }

    /**
     * Retrieves the counter value asynchronously.
     *
     * @return a {@link CompletableFuture} that will be completed with the counter value
     */
    @NotNull CompletableFuture<Integer> asyncGet();

    /**
     * Sets the counter value.
     *
     * @param value the new counter value
     */
    void set(final int value);

    /**
     * Resets the counter to its initial state.
     */
    void reset();

    /**
     * Retrieves the counter value and sets it to a new value in a blocking manner.
     *
     * @param value the new counter value
     * @return the old counter value
     */
    default int blockingGetAndSet(final int value) {
        return this.asyncGetAndSet(value).join();
    }

    /**
     * Retrieves the counter value and sets it to a new value asynchronously.
     *
     * @param value the new counter value
     * @return a {@link CompletableFuture} that will be completed with the old counter value
     */
    @NotNull CompletableFuture<Integer> asyncGetAndSet(final int value);

    /**
     * Increments the counter value by 1 and retrieves it in a blocking manner.
     *
     * @return the incremented counter value
     */
    default int blockingIncrementAndGet() {
        return this.blockingIncrementAndGet(1);
    }

    /**
     * Increments the counter value by a given value and retrieves it in a blocking manner.
     *
     * @param value the value to increment by
     * @return the incremented counter value
     */
    default int blockingIncrementAndGet(final int value) {
        return this.asyncIncrementAndGet(value).join();
    }

    /**
     * Increments the counter value by 1 and retrieves it asynchronously.
     *
     * @return a {@link CompletableFuture} that will be completed with the incremented counter value
     */
    @NotNull
    default CompletableFuture<Integer> asyncIncrementAndGet() {
        return this.asyncIncrementAndGet(1);
    }

    /**
     * Increments the counter value by a given value and retrieves it asynchronously.
     *
     * @param value the value to increment by
     * @return a {@link CompletableFuture} that will be completed with the incremented counter value
     */
    @NotNull CompletableFuture<Integer> asyncIncrementAndGet(final int value);

    /**
     * Retrieves the counter value and increments it by 1 in a blocking manner.
     *
     * @return the old counter value
     */
    default int blockingGetAndIncrement() {
        return this.blockingGetAndIncrement(1);
    }

    /**
     * Retrieves the counter value and increments it by a given value in a blocking manner.
     *
     * @param value the value to increment by
     * @return the old counter value
     */
    default int blockingGetAndIncrement(final int value) {
        return this.asyncGetAndIncrement(value).join();
    }

    /**
     * Retrieves the counter value and increments it by 1 asynchronously.
     *
     * @return a {@link CompletableFuture} that will be completed with the old counter value
     */
    @NotNull
    default CompletableFuture<Integer> asyncGetAndIncrement() {
        return this.asyncGetAndIncrement(1);
    }

    /**
     * Retrieves the counter value and increments it by a given value asynchronously.
     *
     * @param value the value to increment by
     * @return a {@link CompletableFuture} that will be completed with the old counter value
     */
    @NotNull CompletableFuture<Integer> asyncGetAndIncrement(final int value);

    /**
     * Decrements the counter value by 1 and retrieves it in a blocking manner.
     *
     * @return the decremented counter value
     */
    default int blockingDecrementAndGet() {
        return this.blockingDecrementAndGet(1);
    }

    /**
     * Decrements the counter value by a given value and retrieves it in a blocking manner.
     *
     * @param value the value to decrement by
     * @return the decremented counter value
     */
    default int blockingDecrementAndGet(final int value) {
        return this.asyncDecrementAndGet(value).join();
    }

    /**
     * Decrements the counter value by 1 and retrieves it asynchronously.
     *
     * @return a {@link CompletableFuture} that will be completed with the decremented counter value
     */
    @NotNull
    default CompletableFuture<Integer> asyncDecrementAndGet() {
        return this.asyncDecrementAndGet(1);
    }

    /**
     * Decrements the counter value by a given value and retrieves it asynchronously.
     *
     * @param value the value to decrement by
     * @return a {@link CompletableFuture} that will be completed with the decremented counter value
     */
    @NotNull CompletableFuture<Integer> asyncDecrementAndGet(final int value);

    /**
     * Retrieves the counter value and decrements it by 1 in a blocking manner.
     *
     * @return the old counter value
     */
    default int blockingGetAndDecrement() {
        return this.blockingGetAndDecrement(1);
    }

    /**
     * Retrieves the counter value and decrements it by a given value in a blocking manner.
     *
     * @param value the value to decrement by
     * @return the old counter value
     */
    default int blockingGetAndDecrement(final int value) {
        return this.asyncGetAndDecrement(value).join();
    }

    /**
     * Retrieves the counter value and decrements it by 1 asynchronously.
     *
     * @return a {@link CompletableFuture} that will be completed with the old counter value
     */
    @NotNull
    default CompletableFuture<Integer> asyncGetAndDecrement() {
        return this.asyncGetAndDecrement(1);
    }

    /**
     * Retrieves the counter value and decrements it by a given value asynchronously.
     *
     * @param value the value to decrement by
     * @return a {@link CompletableFuture} that will be completed with the old counter value
     */
    @NotNull CompletableFuture<Integer> asyncGetAndDecrement(final int value);

}