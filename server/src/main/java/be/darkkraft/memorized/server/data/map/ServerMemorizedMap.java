package be.darkkraft.memorized.server.data.map;

import be.darkkraft.memorized.codec.registry.CodecRegistry;
import be.darkkraft.memorized.data.map.MapUpdate;
import be.darkkraft.memorized.data.map.MemorizedMap;
import be.darkkraft.memorized.net.session.Session;
import be.darkkraft.memorized.packet.ByteBuf;
import be.darkkraft.memorized.packet.ClientPacket;
import be.darkkraft.memorized.packet.ServerPacket;
import be.darkkraft.memorized.server.MemorizedServer;
import be.darkkraft.memorized.server.data.container.DataContainer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-side implementation of {@link MemorizedMap}.
 * Provides methods for updating and showing the values based on keys.
 *
 * @param <K> the type of keys maintained by this map.
 * @param <V> the type of values maintained by this map.
 */
public class ServerMemorizedMap<K, V> implements DataContainer {

    @NotNull
    private final MemorizedServer server;

    @NotNull
    private final Map<K, V> map = new ConcurrentHashMap<>();

    @NotNull
    private final Class<K> keyClass;

    @NotNull
    private final Class<V> valueClass;

    /**
     * Constructs a new {@link ServerMemorizedMap} with the given server instance and key-value classes.
     *
     * @param server     The server instance that provides the codec registry.
     * @param keyClass   The class type of the keys.
     * @param valueClass The class type of the values.
     */
    @Contract(pure = true)
    public ServerMemorizedMap(final @NotNull MemorizedServer server, final @NotNull Class<K> keyClass, final @NotNull Class<V> valueClass) {
        this.server = server;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    /**
     * Handles updates to the map based on the provided {@link MapUpdate} and buffer.
     *
     * @param session The session associated with the client.
     * @param buffer  The buffer containing the update details.
     */
    @Override
    public void handleUpdate(final @NotNull Session session, final @NotNull ByteBuffer buffer) {
        final MapUpdate update = MapUpdate.fromId(buffer.get());
        if (update == null) {
            throw new IllegalMapUpdate();
        }

        final CodecRegistry registry = this.server.getCodecRegistry();
        final K key = registry.decode(buffer, this.keyClass);

        if (update == MapUpdate.SET) {
            final V value = registry.decode(buffer, this.valueClass);
            this.map.put(key, value);
        } else if (update == MapUpdate.REMOVE) {
            this.map.remove(key);
        }
    }

    /**
     * Handles the {@link ClientPacket#SHOW} interaction by sending the value associated with the
     * provided key to the client.
     *
     * @param session The session associated with the client.
     * @param buffer  The buffer containing the show request.
     */
    @Override
    public void handleShow(final @NotNull Session session, final @NotNull ByteBuffer buffer) {
        final CodecRegistry registry = this.server.getCodecRegistry();
        final K key = registry.decode(buffer, this.keyClass);
        final V value = this.map.get(key);

        if (key == null || value == null) {
            session.unsafeSend(ByteBuffer.allocate(1).put(ServerPacket.NOT_FOUND.getId()));
            return;
        }

        final ByteBuf result = new ByteBuf().put(ServerPacket.RESULT.getId());
        registry.encode(result, value);
        session.unsafeSend(result);
    }

}