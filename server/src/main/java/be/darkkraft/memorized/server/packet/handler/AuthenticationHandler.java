package be.darkkraft.memorized.server.packet.handler;

import be.darkkraft.memorized.packet.ServerPacket;
import be.darkkraft.memorized.server.auth.Authenticator;
import be.darkkraft.memorized.server.session.ClientSession;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Handles authentication packets and manages client authentication.
 */
public final class AuthenticationHandler extends SessionPacketHandler {

    @NotNull
    private final Authenticator authenticator;

    /**
     * Constructs a new {@link AuthenticationHandler} with the given {@link Authenticator}.
     *
     * @param authenticator The authenticator responsible for client authentication.
     */
    @Contract(pure = true)
    public AuthenticationHandler(final @NotNull Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    /**
     * Handles the authentication packet by authenticating the session.
     * If the authentication is successful, an {@link ServerPacket#AUTH_SUCCESS} command is sent.
     * If the authentication fails, an {@link ServerPacket#AUTH_FAILED} command is sent.
     *
     * @param session The client session to be authenticated.
     * @param buffer  The packet buffer containing the authentication data.
     *
     * @throws IOException If an I/O error occurs while reading from the channel.
     */
    @Override
    public void handle(final @NotNull ClientSession session, final @NotNull ByteBuffer buffer) throws IOException {
        if (this.authenticator.auth(session.getChannel(), buffer)) {
            session.setAuthenticated(true);
            session.send(ByteBuffer.allocate(1).put(ServerPacket.AUTH_SUCCESS.getId()));
        } else {
            session.send(ByteBuffer.allocate(1).put(ServerPacket.AUTH_FAILED.getId()));
        }
    }

}