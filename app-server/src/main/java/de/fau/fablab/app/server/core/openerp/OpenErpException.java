package de.fau.fablab.app.server.core.openerp;

public class OpenErpException extends Exception {

    private String message;
    private String jsonErrorMessage;

    public OpenErpException(String message, String jsonErrorMessage) {
        this.message = message;
        this.jsonErrorMessage = jsonErrorMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getJsonErrorMessage() {
        return jsonErrorMessage;
    }
}
