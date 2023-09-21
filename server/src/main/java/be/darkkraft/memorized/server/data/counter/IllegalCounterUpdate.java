package be.darkkraft.memorized.server.data.counter;

/**
 * Exception thrown when an illegal or invalid counter update is encountered.
 */
public final class IllegalCounterUpdate extends RuntimeException {

    /**
     * Constructs a new {@link IllegalCounterUpdate} with no detail message.
     */
    public IllegalCounterUpdate() {
        super("Illegal or invalid counter update encountered.");
    }

    /**
     * Constructs a new {@link IllegalCounterUpdate} with the specified detail message.
     *
     * @param message The detail message.
     */
    public IllegalCounterUpdate(final String message) {
        super(message);
    }

}