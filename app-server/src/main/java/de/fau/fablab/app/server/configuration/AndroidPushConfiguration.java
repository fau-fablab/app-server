package de.fau.fablab.app.server.configuration;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class AndroidPushConfiguration {

    @NotEmpty
    @JsonProperty
    private String apiKey;

    @NotEmpty
    @JsonProperty
    private String url;

    public void setUrl(String newUrl) {
        url = newUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setApiKey(String newApiKey) { apiKey = newApiKey; }

    public String getApiKey() {
        return apiKey;
    }

}
