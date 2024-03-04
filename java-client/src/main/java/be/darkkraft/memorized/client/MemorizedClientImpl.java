package be.darkkraft.memorized.client;

import be.darkkraft.memorized.client.auth.AuthenticationInput;
import be.darkkraft.memorized.client.channel.ClientChannelThread;
import be.darkkraft.memorized.client.config.ClientConfiguration;
import be.darkkraft.memorized.client.packet.command.TransactionQueueImpl;
import be.darkkraft.memorized.client.packet.handler.registry.SessionPacketHandlerRegistry;
import be.darkkraft.memorized.client.retry.ConnectionRetryManager;
import be.darkkraft.memorized.client.session.ServerSession;
import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.data.key.KeyRegistry;
import be.darkkraft.memorized.packet.ServerPacket;
import be.darkkraft.memorized.packet.handler.PacketHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public final class MemorizedClientImpl implements MemorizedClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemorizedClientImpl.class);

    private final InetSocketAddress serverAddress;
    private final AuthenticationInput authenticationInput;
    private final CodecRegistry codecRegistry;
    private final KeyRegistry<Class<?>> keyRegistry;
    private final ClientConfiguration configuration;
    private ConnectionRetryManager connectionRetryManager;
    private ClientChannelThread channelThread;
    private ServerSession session;
    private SessionPacketHandlerRegistry packetHandlerRegistry;
    private TransactionQueueImpl transactionQueue;

    private volatile boolean running;

    /**
     * Initializes a new instance of the {@link MemorizedClientImpl} class.
     *
     * @param serverAddress       The server's {@link InetSocketAddress}.
     * @param authenticationInput The {@link AuthenticationInput} to use.
     * @param codecRegistry       The {@link CodecRegistry} to use.
     * @param keyRegistry         The {@link KeyRegistry} to use.
     * @param configuration       The {@link ClientConfiguration} to use.
     */
    @Contract(pure = true)
    public MemorizedClientImpl(final @NotNull InetSocketAddress serverAddress, final @NotNull AuthenticationInput authenticationInput, final @NotNull CodecRegistry codecRegistry, final KeyRegistry<Class<?>> keyRegistry, ClientConfiguration configuration) {
        this.serverAddress = Objects.requireNonNull(serverAddress, "Server address cannot be null");
        this.authenticationInput = Objects.requireNonNull(authenticationInput, "Authentication input cannot be null");
        this.codecRegistry = Objects.requireNonNull(codecRegistry, "Codec registry cannot be null");
        this.keyRegistry = Objects.requireNonNull(keyRegistry, "Key registry cannot be null");
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null");
    }

    /**
     * Starts the client, establishing a connection to the server.
     */
    @Override
    public void start() {
        LOGGER.info("Starting MemorizedClient...");
        final long start = System.currentTimeMillis();

        this.running = true;

        (this.packetHandlerRegistry = new SessionPacketHandlerRegistry()).initialize(this);
        this.transactionQueue = new TransactionQueueImpl(this);
        this.connectionRetryManager = new ConnectionRetryManager(this);
        if (this.connect()) {
            this.tryToReconnect();
            return;
        }

        LOGGER.info("MemorizedClient is connected in {}ms!", System.currentTimeMillis() - start);
    }

    public boolean connect() {
        final CountDownLatch countDown = new CountDownLatch(1);
        (this.channelThread = new ClientChannelThread(this, countDown)).start();

        try {
            this.channelThread.awaitConnected();
        } catch (final Exception exception) {
            LOGGER.error("Server are not reachable", exception);
            return true;
        }

        this.session = new ServerSession(this.channelThread.getChannel());
        countDown.countDown();
        return false;
    }

    /**
     * Shuts down the client, closing any established connections.
     */
    @Override
    public void shutdown() {
        if (!this.running) {
            return;
        }

        LOGGER.info("Shutting down MemorizedClient...");
        final long start = System.currentTimeMillis();

        this.running = false;

        if (this.channelThread != null) {
            this.channelThread.close();
        }

        LOGGER.info("MemorizedClient stopped in {}ms!", System.currentTimeMillis() - start);
    }

    @Override
    public boolean handlePacket(final @NotNull ByteBuffer buffer) throws IOException {
        final ServerPacket command = ServerPacket.fromId(buffer.get());
        if (command == null) {
            return false;
        }
        final PacketHandler<ServerSession> handler = this.packetHandlerRegistry.getHandler(command);
        if (handler != null) {
            handler.handle(this.session, buffer);
            return true;
        }
        LOGGER.error("Handler not found for {}", command);
        return true;
    }

    @Override
    @Contract(pure = true)
    public @NotNull InetSocketAddress getServerAddress() {
        return this.serverAddress;
    }

    @Override
    @Contract(pure = true)
    public @NotNull AuthenticationInput getAuthenticationInput() {
        return this.authenticationInput;
    }

    @Override
    public @NotNull CodecRegistry getCodecRegistry() {
        return this.codecRegistry;
    }

    @Override
    public @NotNull KeyRegistry<Class<?>> getKeyRegistry() {
        return this.keyRegistry;
    }

    @Override
    public TransactionQueueImpl getTransactionQueue() {
        return this.transactionQueue;
    }

    @Override
    public ServerSession getSession() {
        return this.session;
    }

    @Override
    @Contract(pure = true)
    public boolean isRunning() {
        return this.running;
    }

    @NotNull
    @Override
    public ClientConfiguration getConfiguration() {
        return this.configuration;
    }

    @Override
    public void tryToReconnect() {
        this.connectionRetryManager.start();
    }

}