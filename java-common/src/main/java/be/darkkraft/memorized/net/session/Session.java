package be.darkkraft.memorized.net.session;

import be.darkkraft.memorized.exception.PacketWritingException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Interface representing a network session.
 *
 * @see SocketChannel
 * @see ByteBuffer
 */
public interface Session {

    /**
     * Sends a {@link ByteBuffer} through a {@link SocketChannel}.
     *
     * @param channel the {@link SocketChannel} to send data through
     * @param buffer  the {@link ByteBuffer} containing the data to send
     * @throws IOException if an I/O error occurs
     */
    static void send(final @NotNull SocketChannel channel, final @NotNull ByteBuffer buffer) throws IOException {
        final int size = buffer.position();
        buffer.limit(size).position(0);

        // Create a buffer with a size prefix
        final ByteBuffer newBuffer = ByteBuffer.allocate(4 + size).putInt(size).put(buffer).flip();

        buffer.clear();
        while (newBuffer.hasRemaining()) {
            channel.write(newBuffer);
        }
    }

    /**
     * Retrieves the {@link SocketChannel} associated with this session.
     *
     * @return the associated {@link SocketChannel}
     */
    @NotNull SocketChannel getChannel();

    /**
     * Checks if the session is authenticated.
     *
     * @return {@code true} if authenticated, {@code false} otherwise
     */
    boolean isAuthenticated();

    /**
     * Computes a new {@link ByteBuffer} for this session.
     *
     * @return the computed {@link ByteBuffer}
     */
    @NotNull ByteBuffer computeBuffer();

    /**
     * Updates the buffer for this session.
     *
     * @param buffer the new {@link ByteBuffer}
     */
    void updateBuffer(final @NotNull ByteBuffer buffer);

    /**
     * Removes the current buffer from this session.
     */
    void removeBuffer();

    /**
     * Retrieves the current buffer of this session.
     *
     * @return the current {@link ByteBuffer}
     */
    @Nullable ByteBuffer getBuffer();

    /**
     * Sends a {@link ByteBuffer} without safety checks, throwing a {@link PacketWritingException} on failure.
     *
     * @param buffer the {@link ByteBuffer} to send
     * @throws PacketWritingException  on failure
     */
    default void unsafeSend(final @NotNull ByteBuffer buffer) {
        try {
            this.send(buffer);
        } catch (final IOException exception) {
            throw new PacketWritingException(exception);
        }
    }

    /**
     * Sends a {@link ByteBuffer} through this session's {@link SocketChannel}.
     *
     * @param buffer the {@link ByteBuffer} to send
     * @throws IOException if an I/O error occurs
     */
    default void send(final @NotNull ByteBuffer buffer) throws IOException {
        send(this.getChannel(), buffer);
    }

}