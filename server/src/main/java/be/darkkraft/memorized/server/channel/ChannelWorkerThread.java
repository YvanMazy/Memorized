package be.darkkraft.memorized.server.channel;

import be.darkkraft.memorized.net.session.Session;
import be.darkkraft.memorized.server.MemorizedServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;

/**
 * A worker thread responsible for handling multiple client connections for
 * a single selector. Manages reading from channels and passing messages to sessions.
 */
public final class ChannelWorkerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelWorkerThread.class);
    private final MemorizedServer server;
    private final Selector selector;
    private final CyclicBarrier barrier;

    /**
     * Constructs a new worker thread.
     *
     * @param server  The server this worker is part of.
     * @param barrier A barrier for synchronization.
     * @param id      The unique ID of this worker thread.
     *
     * @throws IOException if an I/O error occurs.
     */
    public ChannelWorkerThread(final MemorizedServer server, final CyclicBarrier barrier, final int id) throws IOException {
        super("Channel Worker #" + id);
        this.server = server;
        this.selector = Selector.open();
        this.barrier = barrier;
    }

    /**
     * Main loop for the worker thread.
     */
    @Override
    public void run() {
        try {
            this.barrier.await();
            while (this.server.isRunning()) {
                this.selectKey();
            }
            this.barrier.await();
            this.selector.close();
        } catch (final Exception e) {
            LOGGER.error("An error occurred while running the worker", e);
        }
    }

    /**
     * Selects keys from the selector and processes them.
     */
    private void selectKey() {
        try {
            final int select = this.selector.select();
            if (select == 0) {
                return;
            }
            final Set<SelectionKey> keys = this.selector.selectedKeys();
            if (keys.isEmpty()) {
                return;
            }
            final Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                this.handle(iterator.next());
                iterator.remove();
            }
        } catch (final Exception e) {
            LOGGER.error("An error occurred while selecting keys", e);
        }
    }

    /**
     * Handles a single key by reading from the channel and passing the message to the session.
     *
     * @param key The selection key to handle.
     *
     * @throws IOException if an I/O error occurs.
     */
    private void handle(final SelectionKey key) throws IOException {
        if (!key.isReadable()) {
            return;
        }
        try {
            final SocketChannel client = (SocketChannel) key.channel();
            final Session session = this.server.getSession(client);
            if (session == null) {
                LOGGER.warn("No session found for client {}. Closing connection.", client.socket().getRemoteSocketAddress());
                client.close();
                return;
            }

            ByteBuffer buffer;
            int read;

            do {
                buffer = session.computeBuffer();
                read = client.read(buffer);

                if (read == -1) {
                    LOGGER.warn("Client connection {} closed by the remote side.", client.socket().getRemoteSocketAddress());
                    this.server.handleRemoveSession(client);
                    client.close();
                    return;
                } else if (read == 0) {
                    session.removeBuffer();
                    return;
                }

                this.read(client, buffer, session);
            } while (read > 0);
        } catch (final Exception exception) {
            key.channel().close();
            LOGGER.error("An error occurred while reading packet", exception);
        }
    }

    /**
     * Processes read data from the client.
     *
     * @param client  The client SocketChannel.
     * @param buffer  The ByteBuffer containing the read data.
     * @param session The session associated with the client.
     *
     * @throws IOException if an I/O error occurs.
     */
    private void read(final SocketChannel client, ByteBuffer buffer, final Session session) throws IOException {
        if (buffer.position() >= 4 && buffer.limit() == 4) {
            buffer.flip();
            final int size = buffer.getInt();
            final int limit = this.server.getPacketSizeLimit(session.isAuthenticated());
            if (size > limit) {
                LOGGER.warn("Failed to handle packet from server. Packet is too big {}/{}", size, limit);
                client.close();
                return;
            }
            session.updateBuffer(buffer = ByteBuffer.allocate(size));
        }

        if (buffer.position() == buffer.limit()) {
            buffer.flip();

            if (!this.server.handlePacket(client, buffer)) {
                LOGGER.warn("Failed to handle packet from client. Removing session and closing connection.");
                this.server.handleRemoveSession(client);
                client.close();
                return;
            }

            session.removeBuffer();
        }
    }

    /**
     * Wakes up the selector if it's currently blocking in a selection operation.
     */
    public void wakeup() {
        this.selector.wakeup();
    }

    /**
     * Returns the selector associated with this worker thread.
     *
     * @return The selector.
     */
    public Selector getSelector() {
        return this.selector;
    }

}