package be.darkkraft.memorized.client.packet.handler;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.net.TransactionQueue;
import be.darkkraft.memorized.client.session.ServerSession;
import be.darkkraft.memorized.packet.ServerPacket;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Handles {@link ServerPacket#RESULT} packets for the {@link MemorizedClient}.
 */
public final class ResultHandler extends SessionPacketHandler {

    private final MemorizedClient client;

    /**
     * Initializes a new instance of the {@link ResultHandler} with the provided client.
     *
     * @param client The {@link MemorizedClient} to which this handler is associated.
     */
    @Contract(pure = true)
    public ResultHandler(final @NotNull MemorizedClient client) {
        this.client = client;
    }

    /**
     * Handles the incoming result packets and completes the transaction in the {@link TransactionQueue}.
     *
     * @param session The {@link ServerSession} from which the packet is received.
     * @param buffer  The {@link ByteBuffer} containing packet data.
     */
    @Override
    public void handle(final @NotNull ServerSession session, final @NotNull ByteBuffer buffer) {
        this.client.getTransactionQueue().complete(buffer);
    }

}