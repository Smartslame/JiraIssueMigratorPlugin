package ru.smartslame.migrator.ao.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.ao.entity.IssueToMigrateEntity;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class IssueToMigrateDao {
    @JiraImport
    private final ActiveObjects ao;

    @Autowired
    public IssueToMigrateDao(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    public IssueToMigrateEntity[] findByAll() {
        return ao.find(IssueToMigrateEntity.class);
    }

    public List<IssueToMigrateEntity> findByProjectKey(String projectKey) {
        final List<IssueToMigrateEntity> issues = new ArrayList<>();
        ao.stream(IssueToMigrateEntity.class, issueToMigrateEntity -> {
            if (projectKey.equals(issueToMigrateEntity.getProjectKey())) {
                issues.add(issueToMigrateEntity);
            }
        });
        return issues;
    }

    public void deleteByProjectKey(String projectKey) {
        final List<IssueToMigrateEntity> issues = new ArrayList<>();
        ao.stream(IssueToMigrateEntity.class, issueToMigrateEntity -> {
            if (projectKey.equals(issueToMigrateEntity.getProjectKey())) {
                issues.add(issueToMigrateEntity);
            }
        });
        issues.forEach(this::delete);
    }

    public IssueToMigrateEntity create(String projectKey, String issueKey) {
        final IssueToMigrateEntity issue = ao.create(IssueToMigrateEntity.class);
        issue.setProjectKey(projectKey);
        issue.setIssueKey(issueKey);
        issue.save();
        return issue;
    }

    public void delete(IssueToMigrateEntity issue) {
        ao.delete(ao.get(IssueToMigrateEntity.class, issue.getID()));
    }

}
