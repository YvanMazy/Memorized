package be.darkkraft.memorized.server.session;

import be.darkkraft.memorized.net.session.Session;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Represents a client session.
 * Manages the client's socket channel, authentication state, and buffer.
 */
public class ClientSession implements Session {

    @NotNull
    private final SocketChannel channel;
    private boolean authenticated;
    @Nullable
    private ByteBuffer currentBuffer;

    /**
     * Initializes a new client session using the provided {@link SocketChannel}.
     *
     * @param channel The {@link SocketChannel} to associate with this client session.
     */
    @Contract(pure = true)
    public ClientSession(final @NotNull SocketChannel channel) {
        this.channel = channel;
    }

    /**
     * Gets the {@link SocketChannel} associated with this client session.
     *
     * @return The {@link SocketChannel} for this client session.
     */
    @Override
    @NotNull
    @Contract(pure = true)
    public SocketChannel getChannel() {
        return this.channel;
    }

    /**
     * Checks if the client session is authenticated.
     *
     * @return true if the session is authenticated, false otherwise.
     */
    @Override
    @Contract(pure = true)
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    /**
     * Sets the authentication state for this client session.
     *
     * @param authenticated The new authentication state.
     */
    public void setAuthenticated(final boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**
     * Computes and returns the current buffer. If no buffer exists, a new one is allocated.
     *
     * @return The current buffer.
     */
    @Override
    @NotNull
    public ByteBuffer computeBuffer() {
        if (this.currentBuffer == null) {
            this.currentBuffer = ByteBuffer.allocate(4);
        }
        return this.currentBuffer;
    }

    /**
     * Updates the current buffer with a new one.
     *
     * @param buffer The new buffer to set.
     */
    @Override
    public void updateBuffer(final @NotNull ByteBuffer buffer) {
        this.currentBuffer = buffer;
    }

    /**
     * Removes the current buffer, setting it to null.
     */
    @Override
    public void removeBuffer() {
        this.currentBuffer = null;
    }

    /**
     * Gets the current buffer.
     *
     * @return The current buffer, or null if no buffer is set.
     */
    @Override
    @Nullable
    @Contract(pure = true)
    public ByteBuffer getBuffer() {
        return this.currentBuffer;
    }

}