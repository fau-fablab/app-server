package de.fau.fablab.app.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Provides configuration options for Drupal-API for News
 */
public class ToolsConfiguration {

    @NotEmpty
    @JsonProperty
    private String url;

    public boolean validate() {
        if (url == null || url.isEmpty() || url == null || url.isEmpty()) return false;
        return true;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
