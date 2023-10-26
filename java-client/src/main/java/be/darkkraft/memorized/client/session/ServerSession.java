package be.darkkraft.memorized.client.session;

import be.darkkraft.memorized.net.session.Session;
import be.darkkraft.memorized.packet.ByteBuf;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Represents a session between the client and the server.
 */
public class ServerSession implements Session {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSession.class);

    @NotNull
    private final SocketChannel channel;

    private boolean authenticated;
    private ByteBuffer currentBuffer;

    /**
     * Constructs a new ServerSession instance with the provided {@link SocketChannel}.
     *
     * @param channel The {@link SocketChannel} associated with this session.
     */
    @Contract(pure = true)
    public ServerSession(final @NotNull SocketChannel channel) {
        this.channel = channel;
    }

    /**
     * Closes the session's socket channel.
     */
    public void close() {
        try {
            this.channel.close();
        } catch (final Exception e) {
            LOGGER.error("Channel cannot be closed", e);
        }
    }

    /**
     * Gets the {@link SocketChannel} associated with this session.
     *
     * @return The {@link SocketChannel} associated with this session.
     */
    @Override
    @NotNull
    @Contract(pure = true)
    public SocketChannel getChannel() {
        return this.channel;
    }

    /**
     * Checks if the session is authenticated.
     *
     * @return True if authenticated, false otherwise.
     */
    @Override
    @Contract(pure = true)
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    /**
     * Sets the authentication status of the session.
     *
     * @param authenticated True to mark the session as authenticated, false otherwise.
     */
    public void setAuthenticated(final boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**
     * Computes a {@link ByteBuf} to be used for network operations.
     *
     * @return The computed {@link ByteBuf}.
     */
    @Override
    @NotNull
    @Contract(pure = true)
    public ByteBuffer computeBuffer() {
        if (this.currentBuffer == null) {
            this.currentBuffer = ByteBuffer.allocate(Integer.BYTES);
        }
        return this.currentBuffer;
    }

    /**
     * Updates the current buffer of the session.
     *
     * @param buffer The new {@link ByteBuf} to set.
     */
    @Override
    public void updateBuffer(final @NotNull ByteBuffer buffer) {
        this.currentBuffer = buffer;
    }

    /**
     * Removes the current buffer of the session.
     */
    @Override
    public void removeBuffer() {
        this.currentBuffer = null;
    }

    /**
     * Gets the current buffer of the session.
     *
     * @return The current {@link ByteBuf}.
     */
    @Override
    @Nullable
    @Contract(pure = true)
    public ByteBuffer getBuffer() {
        return this.currentBuffer;
    }

}