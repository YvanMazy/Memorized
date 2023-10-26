package be.darkkraft.memorized.client.channel;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.net.session.Session;
import be.darkkraft.memorized.packet.ByteBuf;
import be.darkkraft.memorized.packet.ClientPacket;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Represents the thread responsible for managing the client's communication channel.
 */
public final class ClientChannelThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientChannelThread.class);

    private final MemorizedClient client;
    private final CompletableFuture<Void> connectFuture;
    private final CountDownLatch connectLock;
    private SocketChannel channel;

    /**
     * Constructs a new {@link ClientChannelThread}.
     *
     * @param client      The {@link MemorizedClient} associated with the thread.
     * @param connectLock The lock used to synchronize the connection process.
     */
    public ClientChannelThread(final @NotNull MemorizedClient client, final CountDownLatch connectLock) {
        super("MemorizedClient Channel Thread");
        this.client = Objects.requireNonNull(client, "Client cannot be null");
        this.connectLock = connectLock;
        this.connectFuture = new CompletableFuture<>();
    }

    /**
     * Thread execution logic. Handles the life cycle of the communication channel
     * between the client and the server.
     */
    @Override
    public void run() {
        try (final SocketChannel socketChannel = SocketChannel.open(); final Selector selector = Selector.open()) {

            socketChannel.configureBlocking(false);
            socketChannel.connect(this.client.getServerAddress());

            socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);

            //noinspection StatementWithEmptyBody
            while (!socketChannel.finishConnect()) ;

            this.channel = socketChannel;

            this.connectFuture.complete(null);
            this.connectLock.await();

            final ByteBuf buffer = new ByteBuf().put(ClientPacket.AUTH.getId());
            this.client.getAuthenticationInput().write(buffer);

            Session.send(socketChannel, buffer);

            while (this.client.isRunning()) {
                this.selectKey(selector);
            }

            if (this.channel != null) {
                this.channel.close();
            }
        } catch (final Exception exception) {
            if (this.connectFuture.isDone()) {
                LOGGER.error("An error occurred while connecting to the server", exception);
                return;
            }
            this.connectFuture.completeExceptionally(exception);
        }
    }

    /**
     * Selects the available keys from the {@link Selector} and handles them.
     *
     * @param selector The {@link Selector} containing the keys to be processed.
     */
    private void selectKey(final @NotNull Selector selector) {
        try {
            selector.select();
            final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                this.handle(iterator.next());
                iterator.remove();
            }
        } catch (final Exception exception) {
            LOGGER.error("An error occurred while key selecting", exception);
        }
    }

    /**
     * Handles an individual {@link SelectionKey}, taking appropriate actions based on its state.
     *
     * @param key The {@link SelectionKey} to be processed.
     * @throws Exception Any exception that may occur during the key handling.
     */
    private void handle(final @NotNull SelectionKey key) throws Exception {
        try {
            if (key.isReadable()) {
                final Session session = this.client.getSession();
                ByteBuffer buffer;
                int read;

                do {
                    buffer = session.computeBuffer();
                    read = this.channel.read(buffer);

                    if (read == -1) {
                        LOGGER.warn("Server connection closed by the remote side.");
                        this.channel.close();
                        this.channel = null;
                        return;
                    } else if (read == 0) {
                        session.removeBuffer();
                        return;
                    }

                    this.read(buffer, session);
                } while (read > 0);
            }
        } catch (final Exception exception) {
            this.channel.close();
            LOGGER.error("An error occurred while reading packet", exception);
        }
    }

    /**
     * Reads packet data from a {@link ByteBuffer} and passes it to the session for processing.
     *
     * @param buffer  The {@link ByteBuffer} containing the packet data.
     * @param session The {@link Session} responsible for handling the packet.
     * @throws IOException If an I/O error occurs while reading the packet.
     */
    private void read(ByteBuffer buffer, final Session session) throws IOException {
        if (buffer.position() >= 4 && buffer.limit() == 4) {
            buffer.flip();
            final int size = buffer.getInt();
            final int limit = session.isAuthenticated() ? 1048576 : 320;
            if (size > limit) {
                LOGGER.warn("Failed to handle packet from server. Packet is too big {}/{}", size, limit);
                this.channel.close();
                return;
            }
            session.updateBuffer(buffer = ByteBuffer.allocate(size));
        }

        if (buffer.position() == buffer.limit()) {
            buffer.flip();

            if (!this.client.handlePacket(buffer)) {
                LOGGER.warn("Failed to handle packet from server. Removing session and closing connection.");
                this.channel.close();
                this.channel = null;
                return;
            }

            session.removeBuffer();
        }
    }

    /**
     * Waits for the client to be connected.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ExecutionException   If an exception occurs while waiting for the connection.
     */
    public void awaitConnected() throws InterruptedException, ExecutionException {
        this.connectFuture.get();
    }

    /**
     * Returns the client's channel.
     *
     * @return The client's {@link SocketChannel}.
     */
    @Contract(pure = true)
    public SocketChannel getChannel() {
        return this.channel;
    }

    /**
     * Closes the client's channel.
     */
    public void close() {
        try {
            this.channel.close();
        } catch (final Exception exception) {
            LOGGER.error("An error occurred during shutdown", exception);
        }
    }

}