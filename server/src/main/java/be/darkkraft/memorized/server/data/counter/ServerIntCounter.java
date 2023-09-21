package be.darkkraft.memorized.server.data.counter;

import be.darkkraft.memorized.data.counter.CounterUpdate;
import be.darkkraft.memorized.data.counter.IntCounter;
import be.darkkraft.memorized.net.session.Session;
import be.darkkraft.memorized.packet.ClientPacket;
import be.darkkraft.memorized.packet.ServerPacket;
import be.darkkraft.memorized.server.data.container.DataContainer;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Server-side implementation of an {@link IntCounter}.
 * Provides methods for updating and showing the counter's value.
 */
public class ServerIntCounter implements DataContainer {

    private final AtomicInteger value = new AtomicInteger();

    /**
     * Handles updates to the counter based on the provided {@link CounterUpdate} and buffer.
     * Updates the counter's value accordingly and sends the result back to the client.
     *
     * @param session The session associated with the client.
     * @param buffer  The buffer containing the update data.
     */
    @Override
    public void handleUpdate(final @NotNull Session session, final @NotNull ByteBuffer buffer) {
        final CounterUpdate update = CounterUpdate.fromId(buffer.get());
        if (update == null) {
            throw new IllegalCounterUpdate();
        }
        final int delta = buffer.getInt();
        final int result;
        switch (update) {
            case INCREMENT_AND_GET -> result = this.value.addAndGet(delta);
            case DECREMENT_AND_GET -> result = this.value.addAndGet(-delta);
            case GET_AND_SET -> result = this.value.getAndSet(delta);
            case GET_AND_INCREMENT -> result = this.value.getAndAdd(delta);
            case GET_AND_DECREMENT -> result = this.value.getAndAdd(-delta);
            case RESET -> {
                this.value.set(0);
                return;
            }
            default -> throw new IllegalCounterUpdate();
        }
        session.unsafeSend(ByteBuffer.allocate(5).put(ServerPacket.RESULT.getId()).putInt(result));
    }

    /**
     * Handles the {@link ClientPacket#SHOW} interaction by sending the current value of the counter
     * to the client.
     *
     * @param session The session associated with the client.
     * @param buffer  The buffer containing the show request.
     */
    @Override
    public void handleShow(final @NotNull Session session, final @NotNull ByteBuffer buffer) {
        session.unsafeSend(ByteBuffer.allocate(5).put(ServerPacket.RESULT.getId()).putInt(this.value.get()));
    }

}