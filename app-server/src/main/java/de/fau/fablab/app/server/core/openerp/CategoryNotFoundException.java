package de.fau.fablab.app.server.core.openerp;


public class CategoryNotFoundException extends Exception{

    public CategoryNotFoundException(final String aMessage){
        super(aMessage);
    }

    public CategoryNotFoundException(final String aMessage, final Throwable aCause){
        super(aMessage,aCause);
    }
}
