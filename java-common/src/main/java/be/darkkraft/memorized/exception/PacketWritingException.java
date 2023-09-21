package be.darkkraft.memorized.exception;

/**
 * Custom exception class to handle packet writing failures.
 *
 * @see RuntimeException
 */
public final class PacketWritingException extends RuntimeException {

    /**
     * Constructs a new {@link PacketWritingException} with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public PacketWritingException(final Throwable cause) {
        super(cause);
    }

}