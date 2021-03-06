package de.fau.fablab.app.server.core;


import de.fau.fablab.app.rest.core.ICal;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

public class ICalDAO extends AbstractDAO<ICal> {

    public ICalDAO(SessionFactory factory) {
        super(factory);
    }

    //GET
    public ICal findById(long id) {
        return super.get(id);
    }

    @SuppressWarnings("unchecked")
    public List<ICal> findAll() {
        return super.currentSession().createQuery("FROM ICal").list();
    }

    //Create
    public ICal create(ICal obj){
        return persist(obj);
    }


    //Update
    public ICal update(ICal modified) {
        ICal stored = this.get(modified.getId());
        stored.setDescription(modified.getDescription());
        stored.setStart(modified.getStart());
        stored.setEnd(modified.getEnd());
        stored.setLocation(modified.getLocation());
        stored.setUrl(modified.getUrl());
        this.persist(stored);
        return stored;
    }


    //Delete
    public boolean delete(long id) {
        if (get(id) == null)
            return false;

        currentSession().delete(get(id));
        return true;
    }

    public void deleteAll(){
        currentSession().createQuery("delete FROM ICal").executeUpdate();
    }
}
