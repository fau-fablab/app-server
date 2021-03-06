package de.fau.fablab.app.server.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class GeneralDataConfiguration {

    @NotEmpty
    @JsonProperty
    private String fabUrl;

    @NotEmpty
    @JsonProperty
    private String fabMail;

    @NotEmpty
    @JsonProperty
    private String feedbackMail;

    public boolean validate() {
        if (fabUrl == null || fabUrl.isEmpty() || fabMail == null || fabMail.isEmpty() || feedbackMail == null || feedbackMail.isEmpty()) return false;
        return true;
    }

    public String getFabUrl() {
        return fabUrl;
    }

    public void setFabUrl(String fabUrl) {
        this.fabUrl = fabUrl;
    }

    public String getFabMail() {
        return fabMail;
    }

    public void setFabMail(String fabMail) {
        this.fabMail = fabMail;
    }


    public String getFeedbackMail() {
        return feedbackMail;
    }

    public void setFeedbackMail(String feedbackMail) {
        this.feedbackMail = feedbackMail;
    }
}
