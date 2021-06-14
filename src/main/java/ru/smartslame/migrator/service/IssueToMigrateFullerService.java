package ru.smartslame.migrator.service;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.ofbiz.core.entity.GenericEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.ao.dao.IssueToMigrateDao;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class IssueToMigrateFullerService {
    @JiraImport
    private final ProjectManager  projectManager;
    @JiraImport
    private final IssueManager issueManager;
    private final IssueToMigrateDao issueToMigrateDao;

    @Autowired
    public IssueToMigrateFullerService(ProjectManager projectManager, IssueManager issueManager, IssueToMigrateDao issueToMigrateDao) {
        this.projectManager = projectManager;
        this.issueManager = issueManager;
        this.issueToMigrateDao = issueToMigrateDao;
    }

    public void addAll(String projectKey) {
        Long projectId = projectManager.getProjectByCurrentKey(projectKey).getId();
        List<String> issueKeys = new ArrayList<>();
        try {
            Collection<Long> issueIdsForProject = issueManager.getIssueIdsForProject(projectId);
            List<String> collect = issueIdsForProject.stream()
                    .map(issueManager::getAllIssueKeys)
                    .flatMap(Set::stream)
                    .collect(Collectors.toList());
            issueKeys = collect;
        } catch (GenericEntityException e) {
            e.printStackTrace();
        }

        issueKeys.forEach(issueKey -> issueToMigrateDao.create(projectKey, issueKey));
    }
}
