package de.fau.fablab.app.server.core;

import de.fau.fablab.app.rest.core.ICal;
import de.fau.fablab.app.server.core.drupal.ICalClient;
import de.fau.fablab.app.server.core.drupal.ICalInterface;

import java.util.List;

public class ICalFacade {

    private final ICalDAO dao;
    private final ICalInterface iCalInterface;

    public ICalFacade(ICalDAO dao) {
        this.dao = dao;
        this.iCalInterface = ICalClient.getInstance();
        //insertElements();
    }

    public ICal findById(Long id) {
        //return this.dao.findById(id);
        return iCalInterface.findById(id);
    }

    public List<ICal> findAll() {
        //return this.dao.findAll();
        return iCalInterface.findAll();
    }

    public List<ICal> find(int offset, int limit) {
        //return this.dao.find(offset, limit);
        return iCalInterface.find(offset, limit);
    }

    public long lastUpdate() {
        return iCalInterface.lastUpdate();
    }

    public void updateAll() {
        List<ICal> events = iCalInterface.findAll();
        for (ICal event : events) {
            dao.update(event);
        }
    }

    private void insertElements() {
        List<ICal> events = iCalInterface.findAll();
        for (ICal event : events) {
            dao.create(event);
        }
    }

}
