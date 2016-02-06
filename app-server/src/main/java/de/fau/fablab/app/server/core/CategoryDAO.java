package de.fau.fablab.app.server.core;

import de.fau.fablab.app.rest.core.Category;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;


public class CategoryDAO extends AbstractDAO<Category> {

    private static final String QUERY_DELETE_ALL_1 = "delete FROM Category_child_categories";
    private static final String QUERY_DELETE_ALL_2 = "delete FROM Category";
    private static final String QUERY_FIND_ALL = "FROM Category";


    public CategoryDAO(SessionFactory aFactory){
        super(aFactory);
    }

    public List<Category> findAll() {
        return super.currentSession().createQuery(QUERY_FIND_ALL).list();
    }


    public Category create(Category aObject){
        return persist(aObject);
    }

    public boolean delete(long id) {
        if (get(id) == null)
            return false;

        currentSession().delete(get(id));
        return true;
    }

    public void deleteAll(){
        // we cannot delete via Hibernate HDL because it doesn't handle deletion of many-to-one fields
        // https://stackoverflow.com/questions/3492453/hibernate-and-delete-all#3492613
        // iterate over categories and delete them individually
        for (Category c: findAll()) {
            delete(c.getId());
	}
    }
}
