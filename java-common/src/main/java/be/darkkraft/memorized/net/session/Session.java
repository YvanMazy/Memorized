package be.darkkraft.memorized.net.session;

import be.darkkraft.memorized.exception.PacketWritingException;
import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Interface representing a network session.
 *
 * @see SocketChannel
 * @see ByteBuf
 */
public interface Session {

    /**
     * Sends a {@link ByteBuf} through a {@link SocketChannel} and clear it after write.
     *
     * @param channel the {@link SocketChannel} to send data through.
     * @param byteBuf the {@link ByteBuf} containing the data to send.
     *
     * @throws IOException if an I/O error occurs.
     */
    static void sendAndClear(final @NotNull SocketChannel channel, final @NotNull ByteBuf byteBuf) throws IOException {
        try {
            send(channel, byteBuf);
        } finally {
            byteBuf.getBuffer().clear();
        }
    }

    /**
     * Sends a {@link ByteBuf} through a {@link SocketChannel}.
     *
     * @param channel the {@link SocketChannel} to send data through.
     * @param byteBuf the {@link ByteBuf} containing the data to send.
     *
     * @throws IOException if an I/O error occurs.
     */
    static void send(final @NotNull SocketChannel channel, final @NotNull ByteBuf byteBuf) throws IOException {
        final int size = byteBuf.position();
        final ByteBuffer buffer = byteBuf.getBuffer();
        buffer.limit(size).position(0);

        // Create a buffer with a size prefix
        final ByteBuffer newBuffer = ByteBuffer.allocate(4 + size).putInt(size).put(buffer).flip();

        while (newBuffer.hasRemaining()) {
            channel.write(newBuffer);
        }
    }

    /**
     * Sends a {@link ByteBuffer} through a {@link SocketChannel}.
     *
     * @param channel the {@link SocketChannel} to send data through.
     * @param buffer  the {@link ByteBuffer} containing the data to send.
     *
     * @throws IOException if an I/O error occurs.
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
     * @return the associated {@link SocketChannel}.
     */
    @NotNull SocketChannel getChannel();

    /**
     * Checks if the session is authenticated.
     *
     * @return {@code true} if authenticated, {@code false} otherwise.
     */
    boolean isAuthenticated();

    /**
     * Computes a new {@link ByteBuf} for this session.
     *
     * @return the computed {@link ByteBuf}.
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
     * @return the current {@link ByteBuf}
     */
    @Nullable ByteBuffer getBuffer();

    /**
     * Sends a {@link ByteBuf} without safety checks, throwing a {@link PacketWritingException} on failure.
     *
     * @param buffer the {@link ByteBuf} to send.
     *
     * @throws PacketWritingException on failure.
     */
    default void unsafeSend(final @NotNull ByteBuf buffer) {
        try {
            this.send(buffer);
        } catch (final IOException exception) {
            throw new PacketWritingException(exception);
        }
    }

    /**
     * Sends a {@link ByteBuffer} through this session's {@link SocketChannel}.
     *
     * @param buffer the {@link ByteBuffer} to send.
     *
     * @throws IOException if an I/O error occurs.
     */
    default void send(final @NotNull ByteBuffer buffer) throws IOException {
        send(this.getChannel(), buffer);
    }

    /**
     * Sends a {@link ByteBuffer} without safety checks, throwing a {@link PacketWritingException} on failure.
     *
     * @param buffer the {@link ByteBuffer} to send.
     *
     * @throws PacketWritingException on failure.
     */
    default void unsafeSend(final @NotNull ByteBuffer buffer) {
        try {
            this.send(buffer);
        } catch (final IOException exception) {
            throw new PacketWritingException(exception);
        }
    }

    /**
     * Sends a {@link ByteBuf} through this session's {@link SocketChannel}.
     *
     * @param buffer the {@link ByteBuf} to send.
     *
     * @throws IOException if an I/O error occurs.
     */
    default void send(final @NotNull ByteBuf buffer) throws IOException {
        sendAndClear(this.getChannel(), buffer);
    }

}