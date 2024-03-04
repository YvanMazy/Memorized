package be.darkkraft.memorized.client.packet.handler.registry;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.packet.handler.DisconnectHandler;
import be.darkkraft.memorized.client.packet.handler.NotFoundHandler;
import be.darkkraft.memorized.client.packet.handler.ResultHandler;
import be.darkkraft.memorized.client.packet.handler.auth.AuthenticationFailedHandler;
import be.darkkraft.memorized.client.packet.handler.auth.AuthenticationSuccessHandler;
import be.darkkraft.memorized.client.packet.handler.auth.NotAuthenticatedHandler;
import be.darkkraft.memorized.client.session.ServerSession;
import be.darkkraft.memorized.packet.ServerPacket;
import be.darkkraft.memorized.packet.handler.PacketHandler;
import be.darkkraft.memorized.packet.handler.registry.PacketHandlerRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * Registry for session {@link PacketHandler PacketHandlers}, responsible for associating
 * {@link ServerPacket} enums with specific handlers.
 */
public final class SessionPacketHandlerRegistry extends PacketHandlerRegistry<ServerPacket, MemorizedClient, ServerSession> {

    /**
     * Constructs a new {@link SessionPacketHandlerRegistry} for the {@link ServerPacket} class.
     */
    public SessionPacketHandlerRegistry() {
        super(ServerPacket.class);
    }

    /**
     * Initializes the registry with handlers for specific packet types.
     *
     * @param client The {@link MemorizedClient} associated with this registry.
     */
    @Override
    public void initialize(final @NotNull MemorizedClient client) {
        this.register(ServerPacket.NOT_AUTHENTICATED, new NotAuthenticatedHandler());
        this.register(ServerPacket.AUTH_FAILED, new AuthenticationFailedHandler());
        this.register(ServerPacket.AUTH_SUCCESS, new AuthenticationSuccessHandler(client));
        this.register(ServerPacket.DISCONNECT, new DisconnectHandler());
        this.register(ServerPacket.RESULT, new ResultHandler(client));
        this.register(ServerPacket.NOT_FOUND, new NotFoundHandler(client));
    }

}