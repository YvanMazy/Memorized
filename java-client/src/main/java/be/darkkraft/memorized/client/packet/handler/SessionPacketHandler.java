package be.darkkraft.memorized.client.packet.handler;

import be.darkkraft.memorized.client.session.ServerSession;
import be.darkkraft.memorized.packet.handler.PacketHandler;

/**
 * Abstract class for handling packets within a {@link ServerSession}.
 */
public abstract class SessionPacketHandler implements PacketHandler<ServerSession> {
    // No additional fields or methods; serves as a type constraint for packet handlers.
}