package de.fau.fablab.app.server.resources;

import de.fau.fablab.app.rest.api.DataApi;
import de.fau.fablab.app.rest.core.MailAddresses;
import de.fau.fablab.app.server.configuration.GeneralConfiguration;
import de.fau.fablab.app.server.exceptions.Http404Exception;
import de.fau.fablab.app.server.exceptions.Http500Exception;

public class GeneralDataResource implements DataApi {

    private GeneralConfiguration config;

    public GeneralDataResource(GeneralConfiguration config) {
        this.config = config;
    }

    @Override
    public String getFabLabMailAddress() {
        String mail = config.geteMail();
        if (mail == null){
            throw new Http500Exception("An error occurred while retrieving the FabMail-Adress");
        }
        if (mail.length() == 0) throw new Http404Exception("Result is empty");
        return mail;
    }

    @Override
    public String getFeedbackMailAddress() {
        String mail = config.geteMail();
        if (mail == null){
            throw new Http500Exception("An error occurred while retrieving the FabMail-Adress");
        }
        if (mail.length() == 0) throw new Http404Exception("Result is empty");
        return mail;
    }

    @Override
    public MailAddresses getMailAddresses() {
        MailAddresses ret = new MailAddresses(config.getFeedbackEMail(), config.geteMail());
        if (ret == null || ret.getFeedbackMail() == null || ret.getFabLabMail() == null) {
            throw new Http500Exception("An error occurred while retrieving the Mail-Adresses");
        }
        return ret;
    }
}
