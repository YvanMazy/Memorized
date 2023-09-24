package be.darkkraft.memorized.client.data.counter;

import be.darkkraft.memorized.client.MemorizedClient;
import org.jetbrains.annotations.NotNull;

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
    protected @NotNull Class<?> getKeyClass() {
        return String.class;
    }

    @Override
    protected @NotNull String getKeyId() {
        return this.id;
    }

}