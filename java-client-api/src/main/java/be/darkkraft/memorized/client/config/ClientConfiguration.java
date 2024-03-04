package be.darkkraft.memorized.client.config;

public record ClientConfiguration(int packetSizeLimit, int unauthenticatedPacketSizeLimit, long connectionRetryDelay) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int packetSizeLimit = 1_048_576, unauthenticatedPacketSizeLimit = 320;
        private long connectionRetryDelay = 10_000L;

        private Builder() {
        }

        public int packetSizeLimit() {
            return this.packetSizeLimit;
        }

        public Builder packetSizeLimit(final int packetSizeLimit) {
            this.packetSizeLimit = packetSizeLimit;
            return this;
        }

        public int unauthenticatedPacketSizeLimit() {
            return this.unauthenticatedPacketSizeLimit;
        }

        public Builder unauthenticatedPacketSizeLimit(final int unauthenticatedPacketSizeLimit) {
            this.unauthenticatedPacketSizeLimit = unauthenticatedPacketSizeLimit;
            return this;
        }

        public long connectionRetryDelay() {
            return this.connectionRetryDelay;
        }

        public Builder connectionRetryDelay(final long connectionRetryDelay) {
            this.connectionRetryDelay = connectionRetryDelay;
            return this;
        }

        public ClientConfiguration build() {
            return new ClientConfiguration(this.packetSizeLimit, this.unauthenticatedPacketSizeLimit, this.connectionRetryDelay);
        }

    }

}