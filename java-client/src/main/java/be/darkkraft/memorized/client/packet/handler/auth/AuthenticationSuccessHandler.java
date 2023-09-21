package be.darkkraft.memorized.client.packet.handler.auth;

import be.darkkraft.memorized.client.packet.handler.SessionPacketHandler;
import be.darkkraft.memorized.client.session.ServerSession;
import be.darkkraft.memorized.packet.ServerPacket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Handles {@link ServerPacket#AUTH_SUCCESS} packets, setting the session's authenticated flag and logging a debug
 * message.
 */
public final class AuthenticationSuccessHandler extends SessionPacketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    /**
     * Handles the incoming {@link ServerPacket#AUTH_SUCCESS} packets by setting the session as authenticated and logging a debug message.
     *
     * @param session The {@link ServerSession} from which the packet is received.
     * @param buffer  The {@link ByteBuffer} containing packet data.
     */
    @Override
    public void handle(final @NotNull ServerSession session, final @NotNull ByteBuffer buffer) {
        session.setAuthenticated(true);
        LOGGER.debug("Authentication success.");
    }

}