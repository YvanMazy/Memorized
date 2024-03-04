package be.darkkraft.memorized.server;

import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.net.session.Session;
import be.darkkraft.memorized.packet.ClientPacket;
import be.darkkraft.memorized.packet.ServerPacket;
import be.darkkraft.memorized.packet.handler.PacketHandler;
import be.darkkraft.memorized.server.auth.Authenticator;
import be.darkkraft.memorized.server.channel.ServerChannelThread;
import be.darkkraft.memorized.server.data.DataRepositoryCoordinator;
import be.darkkraft.memorized.server.packet.handler.registry.SessionPacketHandlerRegistry;
import be.darkkraft.memorized.server.session.ClientSession;
import be.darkkraft.memorized.server.session.SessionManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Objects;

/**
 * Implementation of the {@link MemorizedServer} interface.
 */
public final class MemorizedServerImpl implements MemorizedServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemorizedServerImpl.class);

    private final InetSocketAddress address;
    private final Authenticator authenticator;
    private final CodecRegistry codecRegistry;
    private final DataRepositoryCoordinator dataRepositoryCoordinator;
    private final int workerThreads;

    private SessionManager sessionManager;
    private ServerChannelThread channelThread;
    private SessionPacketHandlerRegistry packetHandlerRegistry;

    private volatile boolean running;

    /**
     * Creates a new instance of MemorizedServerImpl.
     *
     * @param address          The {@link InetSocketAddress} to bind to.
     * @param authenticator    The {@link Authenticator} to use.
     * @param codecRegistry    The {@link CodecRegistry} to use.
     * @param workerThreads    The worker threads amount
     */
    public MemorizedServerImpl(final @NotNull InetSocketAddress address, final @NotNull Authenticator authenticator,
                               final @NotNull CodecRegistry codecRegistry,
                               final @NotNull DataRepositoryCoordinator dataRepositoryCoordinator, final int workerThreads) {
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        this.authenticator = Objects.requireNonNull(authenticator, "Authenticator cannot be null");
        this.codecRegistry = Objects.requireNonNull(codecRegistry, "Codec registry cannot be null");
        this.dataRepositoryCoordinator = Objects.requireNonNull(dataRepositoryCoordinator, "DataRepository coordinator cannot be null");
        this.workerThreads = Math.max(workerThreads, 1);
    }

    /**
     * Starts the MemorizedServer.
     */
    @Override
    public void start() {
        LOGGER.info("Starting MemorizedServer...");
        final long start = System.currentTimeMillis();

        this.running = true;

        this.sessionManager = new SessionManager();
        (this.packetHandlerRegistry = new SessionPacketHandlerRegistry()).initialize(this);
        (this.channelThread = new ServerChannelThread(this)).start();

        try {
            this.channelThread.awaitBind();
        } catch (final Exception exception) {
            LOGGER.error("Server cannot be bind", exception);
            this.running = false;
            return;
        }

        LOGGER.info("MemorizedServer is started in {}ms!", System.currentTimeMillis() - start);
    }

    /**
     * Shuts down the MemorizedServer.
     */
    @Override
    public void shutdown() {
        if (!this.running) return;

        LOGGER.info("Shutting down MemorizedServer...");
        final long start = System.currentTimeMillis();

        this.running = false;

        if (this.sessionManager != null) {
            this.sessionManager.clear();
        }

        if (this.channelThread != null) {
            this.channelThread.close();
        }

        LOGGER.info("MemorizedServer stopped in {}ms!", System.currentTimeMillis() - start);
    }

    /**
     * Handles a new session.
     *
     * @param client The client's socket channel.
     */
    @Override
    public void handleNewSession(final @NotNull SocketChannel client) {
        if (this.isRunning()) {
            this.sessionManager.addSession(client);
        }
    }

    /**
     * Handles the removal of a session.
     *
     * @param client The client's socket channel.
     */
    @Override
    public void handleRemoveSession(final @NotNull SocketChannel client) {
        if (this.isRunning()) {
            this.sessionManager.removeSession(client);
        }
    }

    /**
     * Handles a packet received from a client.
     * <p>Reads the packet buffer and delegates the handling to a registered {@link PacketHandler}.</p>
     *
     * @param client The client's socket channel.
     * @param buffer The packet buffer.
     * @return True if the packet was handled successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public boolean handlePacket(final @NotNull SocketChannel client, final @NotNull ByteBuffer buffer) throws IOException {
        if (!this.isRunning()) {
            return false;
        }
        final ClientSession session = this.sessionManager.getSession(client);
        if (session != null) {
            final byte id = buffer.get();
            final ClientPacket command = ClientPacket.fromId(id);
            if (command == null) {
                return false;
            }
            if (!session.isAuthenticated() && command != ClientPacket.AUTH) {
                session.send(ByteBuffer.allocate(1).put(ServerPacket.NOT_AUTHENTICATED.getId()));
                return false;
            }
            final PacketHandler<ClientSession> handler = this.packetHandlerRegistry.getHandler(command);
            if (handler != null) {
                handler.handle(session, buffer);
                return true;
            }
            LOGGER.error("Handler not found for {}", command);
        }
        return true;
    }

    /**
     * Gets a session based on its {@link SocketChannel}.
     *
     * @param channel The {@link SocketChannel} associated with the session.
     * @return The {@link Session} or null if the session does not exist.
     */
    @Override
    @Contract(pure = true)
    public @Nullable Session getSession(final @NotNull SocketChannel channel) {
        return this.sessionManager.getSession(channel);
    }

    /**
     * Gets all active sessions.
     *
     * @return A collection of all active {@link Session}s.
     */
    @Override
    @Contract(pure = true)
    public @NotNull Collection<Session> getSessions() {
        return this.sessionManager.getSessions();
    }

    /**
     * Gets the server's address.
     *
     * @return The server's {@link InetSocketAddress}.
     */
    @Override
    public @NotNull InetSocketAddress getAddress() {
        return this.address;
    }

    /**
     * Gets the authenticator used by the server.
     *
     * @return The {@link Authenticator} used by the server.
     */
    @Override
    public @NotNull Authenticator getAuthenticator() {
        return this.authenticator;
    }

    /**
     * Gets the codec registry used by the server.
     *
     * @return The {@link CodecRegistry} used by the server.
     */
    @Override
    public @NotNull CodecRegistry getCodecRegistry() {
        return this.codecRegistry;
    }

    /**
     * Gets the data repository coordinator used by the server.
     *
     * @return The {@link DataRepositoryCoordinator} used by the server.
     */
    @NotNull
    @Override
    public DataRepositoryCoordinator getDataRepositoryCoordinator() {
        return this.dataRepositoryCoordinator;
    }

    /**
     * Gets the number of worker threads configured for the server.
     *
     * @return The number of worker threads.
     */
    @Contract(pure = true)
    @Override
    public int getWorkerThreads() {
        return this.workerThreads;
    }

    /**
     * Checks if the server is running.
     *
     * @return True if the server is running, false otherwise.
     */
    @Contract(pure = true)
    @Override
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Gets the size limit of a received packet.
     *
     * @return The packet size
     */
    @Contract(pure = true)
    @Override
    public int getPacketSizeLimit() {
        return 1048576;
    }

    /**
     * Gets the size limit of a received packet when a session is not authenticated.
     *
     * @return The packet size
     */
    @Contract(pure = true)
    @Override
    public int getUnauthenticatedPacketSizeLimit() {
        return 320;
    }

}