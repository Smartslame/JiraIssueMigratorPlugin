package ru.smartslame.migrator.ao.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.ao.entity.IssueToMigrateEntity;
import ru.smartslame.migrator.ao.entity.NeoMigrationJobEntity;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class NeoMigrationJobDao {
    @JiraImport
    private final ActiveObjects ao;

    @Autowired
    public NeoMigrationJobDao(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    public NeoMigrationJobEntity[] findByAll() {
        return ao.find(NeoMigrationJobEntity.class);
    }

    public NeoMigrationJobEntity create(String projectKey, String schedule) {
        final NeoMigrationJobEntity task = ao.create(NeoMigrationJobEntity.class);
        task.setProjectKey(projectKey);
        task.setSchedule(schedule);
        task.save();
        return task;
    }

    public NeoMigrationJobEntity findByProjectKey(String projectKey) {
        final NeoMigrationJobEntity[] neoMigrationJobEntity = new NeoMigrationJobEntity[1];
        ao.stream(NeoMigrationJobEntity.class, jobEntity -> {
            if (projectKey.equals(jobEntity.getProjectKey())) {
                neoMigrationJobEntity[0] = jobEntity;
            }
        });
        return neoMigrationJobEntity[0];
    }

    public void deleteByProjectKey(String projectKey) {
        final NeoMigrationJobEntity[] neoMigrationJobEntity = new NeoMigrationJobEntity[1];
        ao.stream(NeoMigrationJobEntity.class, jobEntity -> {
            if (projectKey.equals(jobEntity.getProjectKey())) {
                neoMigrationJobEntity[0] = jobEntity;
            }
        });
        delete(neoMigrationJobEntity[0].getID());
    }

    public NeoMigrationJobEntity update(String projectKey, String schedule) {
        final NeoMigrationJobEntity task = findByProjectKey(projectKey);
        NeoMigrationJobEntity task1 = ao.get(NeoMigrationJobEntity.class, task.getID());
        task1.setSchedule(schedule);
        task1.save();
        return task1;
    }

    public void delete(int jobId) {
        ao.delete(ao.get(NeoMigrationJobEntity.class, jobId));
    }

}
