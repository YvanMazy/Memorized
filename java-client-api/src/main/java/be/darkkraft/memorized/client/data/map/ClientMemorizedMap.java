package be.darkkraft.memorized.client.data.map;

import be.darkkraft.memorized.client.MemorizedClient;
import be.darkkraft.memorized.client.data.IdentifiableClientAccessor;
import be.darkkraft.memorized.data.map.MapUpdate;
import be.darkkraft.memorized.data.map.MemorizedMap;
import be.darkkraft.memorized.packet.ByteBuf;
import be.darkkraft.memorized.packet.ClientPacket;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Provides a client-side implementation of the {@link MemorizedMap} interface for key-value storage.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of values that can be stored in this map.
 */
public abstract class ClientMemorizedMap<K, V> extends IdentifiableClientAccessor implements MemorizedMap<K, V> {

    private final Class<V> valueClass;

    /**
     * Constructs a new instance of {@link ClientMemorizedMap}.
     *
     * @param client     The {@link MemorizedClient} associated with this map.
     * @param valueClass The class type of the values.
     */
    protected ClientMemorizedMap(final @NotNull MemorizedClient client, final @NotNull Class<V> valueClass) {
        super(client);
        this.valueClass = valueClass;
    }

    /**
     * Default constructor for {@link ClientMemorizedMap}.
     *
     * @param valueClass The class type of the values.
     */
    protected ClientMemorizedMap(final @NotNull Class<V> valueClass) {
        this.valueClass = valueClass;
    }

    /**
     * Asynchronously retrieves a value associated with a given key.
     *
     * @param key The key whose associated value is to be returned.
     *
     * @return A {@link CompletableFuture} containing the value to which the specified key is mapped, or null if the map contains no mapping for the key.
     */
    @Override
    @NotNull
    public CompletableFuture<V> asyncGet(final @NotNull K key) {
        final ByteBuf buffer = this.writeId(new ByteBuf().put(ClientPacket.SHOW.getId()));
        this.client().getCodecRegistry().encode(buffer, key);
        return this.queue(buffer).thenApply(b -> b != null ? this.client().getCodecRegistry().decode(b, this.valueClass) : null);
    }

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @param key   The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     */
    @Override
    public void put(final @NotNull K key, final V value) {
        final ByteBuf buffer = this.writeId(new ByteBuf().put(ClientPacket.UPDATE.getId())).put(MapUpdate.SET.getId());
        this.client().getCodecRegistry().encode(buffer, key);
        this.client().getCodecRegistry().encode(buffer, value);
        this.write(buffer);
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     *
     * @param key The key whose mapping is to be removed from the map.
     */
    @Override
    public void remove(final @NotNull K key) {
        final ByteBuf buffer = this.writeId(new ByteBuf().put(ClientPacket.UPDATE.getId())).put(MapUpdate.REMOVE.getId());
        this.client().getCodecRegistry().encode(buffer, key);
        this.write(buffer);
    }

}