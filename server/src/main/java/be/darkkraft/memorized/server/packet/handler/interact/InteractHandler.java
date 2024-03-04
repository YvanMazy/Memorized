package be.darkkraft.memorized.server.packet.handler.interact;

import be.darkkraft.memorized.packet.ServerPacket;
import be.darkkraft.memorized.server.MemorizedServer;
import be.darkkraft.memorized.server.data.DataRepository;
import be.darkkraft.memorized.server.data.container.DataContainer;
import be.darkkraft.memorized.server.packet.handler.SessionPacketHandler;
import be.darkkraft.memorized.server.session.ClientSession;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Sealed abstract class for handlers that interact with data repositories.
 * Permitted subclasses are {@link ShowHandler} and {@link UpdateHandler}.
 */
public abstract sealed class InteractHandler extends SessionPacketHandler permits ShowHandler, UpdateHandler {

    @NotNull
    private final MemorizedServer server;

    /**
     * Constructs a new {@link InteractHandler} with the given server instance.
     *
     * @param server The server instance that provides data repository coordination.
     */
    @Contract(pure = true)
    protected InteractHandler(final @NotNull MemorizedServer server) {
        this.server = server;
    }

    /**
     * Handles interaction with data repositories.
     * Sends a {@link ServerPacket#NOT_FOUND} command if the repository, key, or container are not found.
     *
     * @param session The client session that sent the packet.
     * @param buffer  The packet buffer containing data.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void handle(final @NotNull ClientSession session, final @NotNull ByteBuffer buffer) {
        final DataRepository repository = this.server.getDataRepositoryCoordinator().getRepository(buffer.getInt());
        final Object key;
        final DataContainer container;
        if (repository == null || (key = repository.readKey(buffer)) == null || (container = repository.getContainer(key)) == null) {
            session.unsafeSend(ByteBuffer.allocate(1).put(ServerPacket.NOT_FOUND.getId()));
            return;
        }
        this.handle(session, buffer, container);
    }

    /**
     * Abstract method for handling interactions with found data containers.
     *
     * @param session   The client session associated with this request.
     * @param buffer    The packet buffer containing data.
     * @param container The found data container.
     */
    protected abstract void handle(final @NotNull ClientSession session, final @NotNull ByteBuffer buffer, final @NotNull DataContainer container);

}