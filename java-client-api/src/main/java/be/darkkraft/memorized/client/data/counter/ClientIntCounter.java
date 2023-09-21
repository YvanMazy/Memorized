package be.darkkraft.memorized.client.data.counter;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.data.IdentifiableClientAccessor;
import be.darkkraft.memorized.data.counter.CounterUpdate;
import be.darkkraft.memorized.data.counter.IntCounter;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

import static be.darkkraft.memorized.packet.ClientPacket.SHOW;
import static be.darkkraft.memorized.packet.ClientPacket.UPDATE;

/**
 * Provides a client-side implementation of an {@link IntCounter}.
 */
public abstract class ClientIntCounter extends IdentifiableClientAccessor implements IntCounter {

    /**
     * Constructs a new instance of {@link ClientIntCounter}.
     *
     * @param client The {@link MemorizedClient} associated with this counter.
     */
    public ClientIntCounter(final MemorizedClient client) {
        super(client);
    }

    /**
     * Default constructor for {@link ClientIntCounter}.
     */
    public ClientIntCounter() {
        // Default constructor
    }

    @Override
    public @NotNull CompletableFuture<Integer> asyncGet() {
        return this.queue(this.writeId(ByteBuffer.allocate(256).put(SHOW.getId())))
                .thenApply(buffer -> buffer == null ? 0 : buffer.getInt());
    }

    @Override
    public void set(final int value) {
        this.write(this.writeId(ByteBuffer.allocate(256).put(UPDATE.getId()).put(CounterUpdate.SET.getId()).putInt(value)));
    }

    @Override
    public void reset() {
        this.write(this.writeId(ByteBuffer.allocate(256).put(UPDATE.getId()).put(CounterUpdate.RESET.getId())));
    }

    @Override
    public @NotNull CompletableFuture<Integer> asyncGetAndSet(final int value) {
        return this.operate(CounterUpdate.GET_AND_SET, value);
    }

    @Override
    public @NotNull CompletableFuture<Integer> asyncIncrementAndGet(final int value) {
        return this.operate(CounterUpdate.INCREMENT_AND_GET, value);
    }

    @Override
    public @NotNull CompletableFuture<Integer> asyncGetAndIncrement(final int value) {
        return this.operate(CounterUpdate.GET_AND_INCREMENT, value);
    }

    @Override
    public @NotNull CompletableFuture<Integer> asyncDecrementAndGet(final int value) {
        return this.operate(CounterUpdate.DECREMENT_AND_GET, value);
    }

    @Override
    public @NotNull CompletableFuture<Integer> asyncGetAndDecrement(final int value) {
        return this.operate(CounterUpdate.GET_AND_DECREMENT, value);
    }

    private CompletableFuture<Integer> operate(final CounterUpdate update, final int value) {
        return this.queue(this.writeId(ByteBuffer.allocate(256).put(UPDATE.getId())).put(update.getId()).putInt(value))
                .thenApply(buffer -> buffer == null ? 0 : buffer.getInt());
    }

}