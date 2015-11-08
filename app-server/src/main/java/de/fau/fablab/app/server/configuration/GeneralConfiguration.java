package de.fau.fablab.app.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class GeneralConfiguration {

    @NotEmpty
    @JsonProperty
    private String url;

    @NotEmpty
    @JsonProperty
    private String eMail;

    @NotEmpty
    @JsonProperty
    private String feedbackEMail;

    public boolean validate() {
        if (url == null || url.isEmpty() || eMail == null || eMail.isEmpty() || feedbackEMail == null || feedbackEMail.isEmpty()) return false;
        return true;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getFeedbackEMail() {
        return feedbackEMail;
    }

    public void setFeedbackEMail(String feedbackEMail) {
        this.feedbackEMail = feedbackEMail;
    }
}
