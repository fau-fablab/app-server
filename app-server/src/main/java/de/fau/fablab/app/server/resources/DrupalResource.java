package de.fau.fablab.app.server.resources;

import de.fau.cs.mad.fablab.rest.api.DrupalApi;
import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.fablab.app.server.core.DrupalFacade;
import de.fau.fablab.app.server.exceptions.Http404Exception;
import de.fau.fablab.app.server.exceptions.Http500Exception;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;

public class DrupalResource implements DrupalApi {

    private final DrupalFacade facade;

    public DrupalResource(DrupalFacade drupalFacade) {
        this.facade = drupalFacade;
    }

    @UnitOfWork
    @Override
    public List<FabTool> findAllTools() {
        List<FabTool> result = this.facade.findAllTools();
        if (result == null){
            throw new Http500Exception("An error occurred while updating the Tools-list");
        }
        if (result.size() == 0) throw new Http404Exception("Result is empty");
        return result;
    }

    @UnitOfWork
    @Override
    public FabTool findToolById(long id) {
        FabTool result = this.facade.findToolById(id);
        if (result == null){
            throw new Http404Exception("There is no Tool with id " + id);
        }
        return result;
    }
}
