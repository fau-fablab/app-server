package de.fau.fablab.app.server.resources;


import de.fau.fablab.app.rest.api.CategoryApi;
import de.fau.fablab.app.rest.core.Category;
import de.fau.fablab.app.server.core.openerp.OpenErpClient;
import de.fau.fablab.app.server.core.openerp.OpenErpInterface;
import de.fau.fablab.app.server.core.CategoryFacade;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;

public class CategoryResource implements CategoryApi{

    private CategoryFacade mCategoryFacade;

    public CategoryResource(CategoryFacade aCategoryFacade){
        mCategoryFacade = aCategoryFacade;
    }

    @UnitOfWork
    @Override
    public List<Category> findAll() {
        try {
            OpenErpInterface openErpClient = OpenErpClient.getInstance();
            return openErpClient.getCategories();
        }catch (Exception e){
            e.printStackTrace();
        }

        return mCategoryFacade.findAll();
    }

    @UnitOfWork
    @Override
    public List<String> getAutoCompletions() {
        return mCategoryFacade.getAutoCompletions();
    }
}
