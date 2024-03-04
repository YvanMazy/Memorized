package be.darkkraft.memorized.server.session;

import be.darkkraft.memorized.net.session.Session;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the client sessions in the server.
 */
public class SessionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionManager.class);

    private final Map<SocketChannel, ClientSession> sessions = new ConcurrentHashMap<>();

    /**
     * Adds a new client session based on a socket channel.
     *
     * @param client The socket channel representing the client session.
     */
    public void addSession(final @NotNull SocketChannel client) {
        this.sessions.put(client, new ClientSession(client));
        LOGGER.info("New incoming session {}", client.socket().getRemoteSocketAddress());
    }

    /**
     * Removes a client session based on a socket channel.
     *
     * @param client The socket channel representing the client session.
     */
    public void removeSession(final @NotNull SocketChannel client) {
        this.sessions.remove(client);
        LOGGER.info("End of session {}", client.socket().getRemoteSocketAddress());
    }

    /**
     * Retrieves the {@link ClientSession} object associated with a given socket channel.
     *
     * @param client The socket channel representing the client session.
     *
     * @return The {@link ClientSession} object, or null if the session does not exist.
     */
    public @Nullable ClientSession getSession(final SocketChannel client) {
        return this.sessions.get(client);
    }

    /**
     * Gets the total number of active client sessions.
     *
     * @return The number of active client sessions.
     */
    @Contract(pure = true)
    public int getSessionCount() {
        return this.sessions.size();
    }

    /**
     * Clears all active client sessions.
     */
    public void clear() {
        // TODO: Implement a disconnect packet to gracefully close sessions.
        this.sessions.clear();
    }

    /**
     * Retrieves all active sessions as a collection.
     *
     * @return A collection containing all active {@link Session} objects.
     */
    @NotNull
    @Contract(pure = true)
    public Collection<Session> getSessions() {
        return this.sessions.values().stream().map(Session.class::cast).toList();
    }

}