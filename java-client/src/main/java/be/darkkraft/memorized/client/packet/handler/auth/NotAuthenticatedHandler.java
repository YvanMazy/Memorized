package be.darkkraft.memorized.client.packet.handler.auth;

import be.darkkraft.memorized.client.packet.handler.SessionPacketHandler;
import be.darkkraft.memorized.client.session.ServerSession;
import be.darkkraft.memorized.packet.ServerPacket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Handles {@link ServerPacket#NOT_AUTHENTICATED} packets, logging an error message when such packets are received.
 */
public final class NotAuthenticatedHandler extends SessionPacketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotAuthenticatedHandler.class);

    /**
     * Handles the incoming {@link ServerPacket#NOT_AUTHENTICATED} packets by logging an error message.
     *
     * @param session The {@link ServerSession} from which the packet is received.
     * @param buffer  The {@link ByteBuffer} containing packet data.
     */
    @Override
    public void handle(final @NotNull ServerSession session, final @NotNull ByteBuffer buffer) {
        LOGGER.error("A command could not be executed because the connection was not authenticated.");
    }

}