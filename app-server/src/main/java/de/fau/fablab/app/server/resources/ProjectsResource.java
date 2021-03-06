package de.fau.fablab.app.server.resources;

import de.fau.fablab.app.rest.api.ProjectsApi;
import de.fau.fablab.app.rest.core.ProjectImageUpload;
import de.fau.fablab.app.rest.core.ProjectFile;
import de.fau.fablab.app.server.core.projects.ProjectsClient;
import de.fau.fablab.app.server.core.projects.ProjectsInterface;
import de.fau.fablab.app.server.exceptions.Http500Exception;

public class ProjectsResource implements ProjectsApi {

    private ProjectsInterface projectsInterface;

    public ProjectsResource() {
        projectsInterface = ProjectsClient.getInstance();
    }

    @Override
    public String createProject(ProjectFile project) {
        String gistUrl = projectsInterface.postProject(project);
        if (gistUrl == null) {
            throw new Http500Exception("Project was not created.");
        }
        return gistUrl;
    }

    @Override
    public String updateProjectOld(String gistId, ProjectFile project) {
        return updateProject(gistId, project);
    }

    @Override
    public String updateProject(String gistId, ProjectFile project) {
        String gistUrl = projectsInterface.patchProject(gistId, project);
        if (gistUrl == null) {
            throw new Http500Exception("Project was not patched.");
        }
        return gistUrl;
    }

    @Override
    public void deleteProject(String gistId) {
        projectsInterface.deleteProject(gistId);
    }

    @Override
    public String uploadImage(ProjectImageUpload image) {
        String imageUrl = projectsInterface.commitImage(image);
        if (imageUrl == null) {
            throw new Http500Exception("Image was not committed.");
        }
        return imageUrl;
    }
}
