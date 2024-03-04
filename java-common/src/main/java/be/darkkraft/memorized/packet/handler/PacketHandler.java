package be.darkkraft.memorized.packet.handler;

import be.darkkraft.memorized.net.session.Session;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Interface representing a handler for network packets.
 *
 * @param <T> the type of session with which this handler interacts
 *
 * @see ByteBuffer
 * @see IOException
 */
public interface PacketHandler<T extends Session> {

    /**
     * Handles the received packet.
     *
     * @param session the session associated with the packet
     * @param buffer  the ByteBuffer containing the packet data
     *
     * @throws IOException if an I/O error occurs while handling the packet
     */
    void handle(final @NotNull T session, final @NotNull ByteBuffer buffer) throws IOException;

}