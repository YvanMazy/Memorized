package be.darkkraft.memorized.server.data.map;

/**
 * Exception thrown when an illegal or invalid map update is encountered.
 */
public final class IllegalMapUpdate extends RuntimeException {

    /**
     * Constructs a new {@link IllegalMapUpdate} with no detail message.
     */
    public IllegalMapUpdate() {
        super("Illegal or invalid map update encountered.");
    }

    /**
     * Constructs a new {@link IllegalMapUpdate} with the specified detail message.
     *
     * @param message The detail message.
     */
    public IllegalMapUpdate(final String message) {
        super(message);
    }

}