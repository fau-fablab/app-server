package de.fau.fablab.app.server.resources;

import de.fau.fablab.app.rest.api.ICalApi;
import de.fau.fablab.app.rest.core.ICal;

import de.fau.fablab.app.server.core.ICalFacade;
import de.fau.fablab.app.server.exceptions.Http400Exception;
import de.fau.fablab.app.server.exceptions.Http404Exception;
import de.fau.fablab.app.server.exceptions.Http500Exception;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;


public class ICalResource implements ICalApi {

    private final ICalFacade facade;


    public ICalResource(ICalFacade facade) {
        this.facade = facade;
    }

    @UnitOfWork
    @Override
    public ICal findById(long id) {
        ICal result = this.facade.findById(id);
        if (result == null){
            throw new Http404Exception("There is no Event with id " + id);
        }
        return result;
    }

    @UnitOfWork
    @Override
    public List<ICal> findAll() {
        List<ICal> result = this.facade.findAll();
        if (result == null){
            throw new Http500Exception("An error occurred while updating the Event-list");
        }
        if (result.size() == 0) throw new Http404Exception("Result is empty");
        return result;
    }

    @UnitOfWork
    @Override
    public List<ICal> find(int offset, int limit) {
        if (offset == 0 && limit == 0) return findAll();
        if (offset < 0 || limit < 0) throw new Http400Exception("offset < 0 or limit < 0 is not permitted");

        List<ICal> result = this.facade.find(offset, limit);
        if (result == null){
            throw new Http404Exception("offset " + offset + " is out of bounds!");
        }
        if (result.size() == 0) throw new Http404Exception("Result is empty");
        return result;
    }

    @UnitOfWork
    @Override
    public Long lastUpdate() {
        return this.facade.lastUpdate();
    }

}