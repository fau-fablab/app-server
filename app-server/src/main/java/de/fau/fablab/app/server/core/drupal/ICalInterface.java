package de.fau.fablab.app.server.core.drupal;

import java.util.List;

import de.fau.fablab.app.rest.core.ICal;

public interface ICalInterface {
    public ICal findById(Long id);
    public List<ICal> findAll();
    public List<ICal> find(int offset, int limit);
    public long lastUpdate();
}
