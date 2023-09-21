package be.darkkraft.memorized.server.packet.handler.registry;

import be.darkkraft.memorized.packet.ClientPacket;
import be.darkkraft.memorized.packet.handler.registry.PacketHandlerRegistry;
import be.darkkraft.memorized.server.MemorizedServer;
import be.darkkraft.memorized.server.packet.handler.AuthenticationHandler;
import be.darkkraft.memorized.server.packet.handler.DisconnectHandler;
import be.darkkraft.memorized.server.packet.handler.interact.ShowHandler;
import be.darkkraft.memorized.server.packet.handler.interact.UpdateHandler;
import be.darkkraft.memorized.server.session.ClientSession;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Registry for handling packets associated with {@link ClientSession}.
 * Registers packet handlers for different types of client commands.
 */
public final class SessionPacketHandlerRegistry extends PacketHandlerRegistry<ClientPacket, MemorizedServer, ClientSession> {

    /**
     * Constructs a new {@link SessionPacketHandlerRegistry} and initializes the underlying {@link PacketHandlerRegistry}.
     */
    @Contract(pure = true)
    public SessionPacketHandlerRegistry() {
        super(ClientPacket.class);
    }

    /**
     * Initializes the packet handlers for this registry using the provided {@link MemorizedServer} instance.
     *
     * @param server The server instance that provides resources like authenticators.
     */
    @Override
    public void initialize(final @NotNull MemorizedServer server) {
        this.register(ClientPacket.AUTH, new AuthenticationHandler(server.getAuthenticator()));
        this.register(ClientPacket.SHOW, new ShowHandler(server));
        this.register(ClientPacket.UPDATE, new UpdateHandler(server));
        this.register(ClientPacket.DISCONNECT, new DisconnectHandler(server));
    }

}