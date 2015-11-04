package de.fau.fablab.app.server.core;

import de.fau.cs.mad.fablab.rest.core.FabTool;
import de.fau.fablab.app.server.core.drupal.DrupalInterface;
import de.fau.fablab.app.server.core.drupal.DrupalClient;

import java.util.List;

public class DrupalFacade {

    private final DrupalDAO dao;
    private final DrupalInterface drupalInterface;

    public DrupalFacade(DrupalDAO dao) {
        this.dao = dao;
        this.drupalInterface = DrupalClient.getInstance();
    }

    public FabTool findToolById(Long id) {
        checkAndUpdateToolList();
        return dao.findToolById(id);
    }

    public List<FabTool> findAllTools() {
        checkAndUpdateToolList();

        return dao.findAllTools();
    }

    private void checkAndUpdateToolList() {

        if (drupalInterface.updateNeeded()) {
            dao.deleteAll();

            List<FabTool> toolList = drupalInterface.findAllTools();

            for (FabTool tool : toolList) {
                dao.create(tool);
            }
        }
    }
}
