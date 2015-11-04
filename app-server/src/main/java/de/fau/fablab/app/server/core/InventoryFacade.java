package de.fau.fablab.app.server.core;


import de.fau.fablab.app.rest.core.InventoryItem;

import java.util.List;

public class InventoryFacade{

    private final InventoryDAO dao;

    public InventoryFacade(InventoryDAO dao) {
        this.dao = dao;
    }

    public InventoryItem create(InventoryItem obj) {
        return this.dao.create(obj);
    }


    public List<InventoryItem> getAll() {
        return this.dao.findAll();
    }

    public boolean deleteAll(){
        return this.dao.deleteAll();
    }


}
