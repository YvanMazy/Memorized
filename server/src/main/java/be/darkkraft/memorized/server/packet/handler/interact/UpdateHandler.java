package be.darkkraft.memorized.server.packet.handler.interact;

import be.darkkraft.memorized.packet.ClientPacket;
import be.darkkraft.memorized.server.MemorizedServer;
import be.darkkraft.memorized.server.data.container.DataContainer;
import be.darkkraft.memorized.server.session.ClientSession;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Handles the {@link ClientPacket#UPDATE} interaction with data containers.
 */
public final class UpdateHandler extends InteractHandler {

    /**
     * Constructs a new {@link UpdateHandler} with the given server instance.
     *
     * @param server The server instance that provides data repository coordination.
     */
    @Contract(pure = true)
    public UpdateHandler(final @NotNull MemorizedServer server) {
        super(server);
    }

    /**
     * Handles the {@link ClientPacket#UPDATE} interaction by delegating to the {@link DataContainer#handleUpdate} method.
     *
     * @param session   The client session associated with this request.
     * @param buffer    The packet buffer containing data.
     * @param container The found data container.
     */
    @Override
    protected void handle(final @NotNull ClientSession session, final @NotNull ByteBuffer buffer, final @NotNull DataContainer container) {
        container.handleUpdate(session, buffer);
    }

}