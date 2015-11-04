package de.fau.fablab.app.server.core.drupal;

import de.fau.fablab.app.rest.core.News;

import java.util.Date;
import java.util.List;

public interface NewsInterface {
    public News findById(long id);
    public List<News> find(int offset, int limit);
    public List<News> findAll();
    public List<News> findNewsSince(long timestamp);
    public long lastUpdate();
}
