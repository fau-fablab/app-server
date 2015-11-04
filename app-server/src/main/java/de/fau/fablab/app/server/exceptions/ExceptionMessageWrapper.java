package de.fau.fablab.app.server.exceptions;

public class ExceptionMessageWrapper {
    String errorMessage;

    public ExceptionMessageWrapper(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
