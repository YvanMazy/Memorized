package be.darkkraft.memorized.server.data.container;

import be.darkkraft.memorized.net.session.Session;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Interface for managing a container that holds data.
 *
 * @see Session
 * @see ByteBuffer
 */
public interface DataContainer {

    /**
     * Handles data updates for a given session and packet buffer.
     *
     * @param session The {@link Session} making the update.
     * @param buffer  The {@link ByteBuffer} containing the update data.
     */
    void handleUpdate(final @NotNull Session session, final @NotNull ByteBuffer buffer);

    /**
     * Handles data display for a given session and packet buffer.
     *
     * @param session The {@link Session} requesting the data.
     * @param buffer  The {@link ByteBuffer} containing any additional data for the display.
     */
    void handleShow(final @NotNull Session session, final @NotNull ByteBuffer buffer);

}