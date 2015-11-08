package de.fau.fablab.app.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Provides configuration options for DoorState interface
 */
public class DoorStateConfiguration {

    /**
     * location of key file containing the secret shard key used for hash calculation
     */
    @JsonProperty
    private String keyFile;

    /**
     * hashing algorithm that should be used to calculate hash of $data
     */
    @JsonProperty
    private String hashAlgorithm;

    /**
     * maximum difference (in seconds) of time supplied in $data to current time
     */
    @JsonProperty
    private int maximumTimeOffset = 30;

    /**
     * Minimum duration in seconds, until the state actually changes (to avoid sending of push messages,
     * if door bounces)
     */
    @JsonProperty
    private int minimumDurationUntilChange = 3 * 60;

    public String getKeyFile() {
        return keyFile;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public int getMaximumTimeOffset() {
        return maximumTimeOffset;
    }

    public void setMaximumTimeOffset(int maximumTimeOffset) {
        this.maximumTimeOffset = maximumTimeOffset;
    }

    public int getMinimumDurationUntilChange() {
        return minimumDurationUntilChange;
    }

    public void setMinimumDurationUntilChange(int minimumDurationUntilChange) {
        this.minimumDurationUntilChange = minimumDurationUntilChange;
    }
}
