package de.fau.fablab.app.server.core;

import de.fau.fablab.app.rest.core.CartServer;
import de.fau.fablab.app.rest.core.CartStatus;
import de.fau.fablab.app.server.core.pushservice.PushFacade;

public class CartFacade{

    private final CartDAO dao;
    private OldCartRemover remover;

    public CartFacade(CartDAO dao) {
        this.dao = dao;
        this.remover = new OldCartRemover(dao, 30); // Removes old carts after 30 min!
    }

    public CartServer create(CartServer obj) {
        obj.setSentToServer();
        return this.dao.create(obj);
    }

    public CartServer getCart(String id){
        return this.dao.findById(id);
    }

    public CartStatus getStatus(String id) {
        return this.dao.findById(id).getStatus();
    }

    public boolean updateCartStatus(String id, CartStatus status){
        remover.removeAllOldCarts();
        if(this.dao.updateCartStatus(id, status)){
            PushFacade.getInstance().cartStatusChanged(this.dao.findById(id));
            return true;
        }
        return false;
    }



}
