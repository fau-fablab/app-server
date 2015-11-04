package de.fau.fablab.app.server.resources;

import de.fau.cs.mad.fablab.rest.api.CartApi;
import de.fau.cs.mad.fablab.rest.core.*;
import de.fau.fablab.app.server.core.CartFacade;
import de.fau.fablab.app.server.exceptions.Http403Exception;
import io.dropwizard.hibernate.UnitOfWork;


public class CartResource implements CartApi {

    private final CartFacade facade;

    public CartResource(CartFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public void create(CartServer obj) {
        if(CheckoutResource.getAcceptedCode().equals(obj.getCartCode()))
            this.facade.create(obj);
        else
            throw new Http403Exception("Your cart code (" + obj.getCartCode() + ") is not valid.");
    }

    @UnitOfWork
    @Override
    public CartStatus getStatus(String id) {
        return this.facade.getStatus(id);
    }
}