package de.fau.fablab.app.server.core.projects;

import de.fau.fablab.app.rest.core.ProjectImageUpload;
import de.fau.fablab.app.rest.core.ProjectFile;

public interface ProjectsInterface {
    public String postProject(ProjectFile project);
    public String patchProject(String gistId, ProjectFile project);
    public void deleteProject(String gistId);
    public String commitImage(ProjectImageUpload image);
}
