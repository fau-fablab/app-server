package de.fau.fablab.app.server.core.drupal;

import de.fau.fablab.app.rest.core.FabTool;

import java.util.List;

public interface DrupalInterface {

    List<FabTool> findAllTools();
    FabTool findToolById(long id);
    boolean updateNeeded();
}
