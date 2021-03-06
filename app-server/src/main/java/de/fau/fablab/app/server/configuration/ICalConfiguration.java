package de.fau.fablab.app.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Provides configuration options for ICal-Feed
 */
public class ICalConfiguration {

    @NotEmpty
    @JsonProperty
    private String endpoint;

    @NotEmpty
    @JsonProperty
    private String icalUrl;

    @NotEmpty
    @JsonProperty
    private String fallback;

    public boolean validate() {
        if (endpoint == null || endpoint.isEmpty() || icalUrl == null || icalUrl.isEmpty()) return false;
        return true;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getIcalUrl() {
        return icalUrl;
    }

    public void setIcalUrl(String icalUrl) {
        this.icalUrl = icalUrl;
    }

    public String getFallback() {
        return fallback;
    }

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }
}
