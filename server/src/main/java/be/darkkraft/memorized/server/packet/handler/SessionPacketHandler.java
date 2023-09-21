package be.darkkraft.memorized.server.packet.handler;

import be.darkkraft.memorized.packet.handler.PacketHandler;
import be.darkkraft.memorized.server.session.ClientSession;

/**
 * Abstract base class for packet handlers that operate on {@link ClientSession} objects.
 */
public abstract class SessionPacketHandler implements PacketHandler<ClientSession> {

    /**
     * This class serves as an extension point for implementing packet handlers specific to {@link ClientSession}.
     * Implement the methods from {@link PacketHandler} to handle packets for {@link ClientSession} instances.
     *
     * @see PacketHandler
     * @see ClientSession
     */
    public SessionPacketHandler() {
        // Intentionally left blank for subclass implementation.
    }

}