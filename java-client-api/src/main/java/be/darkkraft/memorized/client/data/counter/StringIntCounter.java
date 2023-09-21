package be.darkkraft.memorized.client.data.counter;

import be.darkkraft.memorized.client.MemorizedClient;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * Provides a client-side implementation of a {@link ClientIntCounter} using strings as keys.
 */
public class StringIntCounter extends ClientIntCounter {

    private final String id;

    /**
     * Constructs a new instance of {@link StringIntCounter}.
     *
     * @param client The {@link MemorizedClient} associated with this counter.
     * @param id     The identifier for this counter.
     */
    public StringIntCounter(final MemorizedClient client, final String id) {
        super(client);
        this.id = id;
    }

    /**
     * Default constructor for {@link StringIntCounter}.
     *
     * @param id The identifier for this counter.
     */
    public StringIntCounter(final String id) {
        this.id = id;
    }

    @Override
    protected @NotNull ByteBuffer writeId(final @NotNull ByteBuffer buffer) {
        return this.client().getCodecRegistry().encode(buffer.putInt(this.getKeyIdentifier(String.class)), this.getId());
    }

    public String getId() {
        return this.id;
    }

}