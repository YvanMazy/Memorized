package be.darkkraft.memorized.client.packet.handler;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.session.ServerSession;
import be.darkkraft.memorized.codec.Codec;
import be.darkkraft.memorized.packet.ServerPacket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Handles {@link ServerPacket#DISCONNECT} packets for the {@link MemorizedClient}.
 */
public final class DisconnectHandler extends SessionPacketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisconnectHandler.class);

    /**
     * Handles the incoming disconnect packets, logs the reason, and closes the session.
     *
     * @param session The {@link ServerSession} from which the packet is received.
     * @param buffer  The {@link ByteBuffer} containing packet data.
     */
    @Override
    public void handle(final @NotNull ServerSession session, final @NotNull ByteBuffer buffer) {
        final String reason = Codec.getString(buffer);
        session.close();
        LOGGER.error("Disconnected for: {}", reason);
    }

}