package be.darkkraft.memorized.server.packet.handler;

import be.darkkraft.memorized.server.MemorizedServer;
import be.darkkraft.memorized.server.session.ClientSession;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Handles disconnect packets by removing the associated client session from the server.
 */
public final class DisconnectHandler extends SessionPacketHandler {

    private final MemorizedServer server;

    /**
     * Constructs a new {@link DisconnectHandler} with the associated server instance.
     *
     * @param server The server instance responsible for managing sessions.
     */
    @Contract(pure = true)
    public DisconnectHandler(final @NotNull MemorizedServer server) {
        this.server = server;
    }

    /**
     * Handles the disconnect packet by removing the session associated with the given channel.
     *
     * @param session The client session that is disconnecting.
     * @param buffer  The packet buffer containing the disconnect packet data.
     */
    @Override
    public void handle(final @NotNull ClientSession session, final @NotNull ByteBuffer buffer) {
        this.server.handleRemoveSession(session.getChannel());
    }

}