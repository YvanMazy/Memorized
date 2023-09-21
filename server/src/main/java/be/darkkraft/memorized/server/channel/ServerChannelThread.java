package be.darkkraft.memorized.server.channel;

import be.darkkraft.memorized.server.MemorizedServer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;

/**
 * Thread responsible for managing the server's communication channel.
 * It handles incoming connections, and dispatches them to worker threads.
 */
public final class ServerChannelThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerChannelThread.class);

    private final MemorizedServer server;
    private final CompletableFuture<Void> bindFuture;
    private final ChannelWorkerThread[] workerThreads;
    private final CyclicBarrier barrier;

    private int nextWorker;

    private ServerSocketChannel channel;

    /**
     * Creates a new instance of {@link ServerChannelThread}.
     *
     * @param server The {@link MemorizedServer} associated with the thread.
     */
    @Contract(pure = true)
    public ServerChannelThread(final @NotNull MemorizedServer server) {
        super("MemorizedServer Channel Thread");
        this.server = Objects.requireNonNull(server, "Server cannot be null");
        this.bindFuture = new CompletableFuture<>();

        final int threads = server.getWorkerThreads();
        this.workerThreads = new ChannelWorkerThread[threads];
        this.barrier = new CyclicBarrier(threads + 1);
    }

    /**
     * The main loop for accepting incoming connections and assigning them to worker threads.
     */
    @Override
    public void run() {
        try (final ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
            final InetSocketAddress address = this.server.getAddress();
            serverSocket.bind(address);
            serverSocket.configureBlocking(false);

            this.channel = serverSocket;

            for (int i = 0; i < this.workerThreads.length; i++) {
                (this.workerThreads[i] = new ChannelWorkerThread(this.server, this.barrier, i)).start();
            }

            this.barrier.await();
            this.barrier.reset();
            this.bindFuture.complete(null);

            LOGGER.info("MemorizedServer bind server on port {}", address.getPort());

            while (this.server.isRunning()) {
                final SocketChannel client = serverSocket.accept();
                if (client != null) {
                    this.handleClient(client);
                }
            }

        } catch (final Exception exception) {
            if (this.bindFuture.isDone()) {
                LOGGER.error("An error occurred while connecting to the server", exception);
            } else {
                this.bindFuture.completeExceptionally(exception);
            }
        }
    }

    /**
     * Waits for the server to be bound to an address/port.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ExecutionException   If an execution exception occurs while waiting.
     */
    public void awaitBind() throws InterruptedException, ExecutionException {
        this.bindFuture.get();
    }

    /**
     * Closes the server channel.
     */
    public void close() {
        try {
            for (final ChannelWorkerThread thread : this.workerThreads) {
                thread.wakeup();
            }
            this.barrier.await();
            if (this.channel != null) {
                this.channel.close();
            }
        } catch (final Exception exception) {
            LOGGER.error("An error occurred during shutdown", exception);
        }
    }

    private void handleClient(final @NotNull SocketChannel client) throws Exception {
        client.configureBlocking(false);
        this.server.handleNewSession(client);

        final ChannelWorkerThread thread = this.workerThreads[this.nextWorker];
        client.register(thread.getSelector(), SelectionKey.OP_READ);
        thread.wakeup();

        this.nextWorker = (this.nextWorker + 1) % this.workerThreads.length;
    }

}