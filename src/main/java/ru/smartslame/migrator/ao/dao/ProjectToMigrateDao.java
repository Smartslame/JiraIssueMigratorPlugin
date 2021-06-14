package ru.smartslame.migrator.ao.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.ao.entity.ProjectToMigrateEntity;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class ProjectToMigrateDao {
    @JiraImport
    private final ActiveObjects ao;

    @Autowired
    public ProjectToMigrateDao(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    public ProjectToMigrateEntity[] findByAll() {
        return ao.find(ProjectToMigrateEntity.class);
    }

    public ProjectToMigrateEntity create(String projectKey) {
        final ProjectToMigrateEntity project = ao.create(ProjectToMigrateEntity.class);
        project.setKey(projectKey);
        project.save();
        return project;
    }

    public Boolean contains(String projectKey) {
        final boolean[] contains = {false};
        ao.stream(ProjectToMigrateEntity.class, projectToMigrateEntity -> {
            if (projectKey.equals(projectToMigrateEntity.getKey())) {
                contains[0] = true;
            }
        });
        return contains[0];
    }

    public void deleteByProjectKey(String projectKey) {
        final ProjectToMigrateEntity[] project = new ProjectToMigrateEntity[1];
        ao.stream(ProjectToMigrateEntity.class, projectToMigrateEntity -> {
            if (projectKey.equals(projectToMigrateEntity.getKey())) {
                project[0] = projectToMigrateEntity;
            }
        });
        delete(project[0].getID());
    }

    public void delete(int projectId) {
        ao.delete(ao.get(ProjectToMigrateEntity.class, projectId));
    }


}
