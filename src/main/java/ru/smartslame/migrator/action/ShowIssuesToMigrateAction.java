package ru.smartslame.migrator.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import ru.smartslame.migrator.ao.dao.IssueToMigrateDao;
import ru.smartslame.migrator.ao.entity.IssueToMigrateEntity;

import java.util.List;

public class ShowIssuesToMigrateAction extends JiraWebActionSupport {
    private final IssueToMigrateDao issueToMigrateDao;

    @Autowired
    public ShowIssuesToMigrateAction(IssueToMigrateDao issueToMigrateDao) {
        this.issueToMigrateDao = issueToMigrateDao;
    }

    public IssueToMigrateEntity[] getAll() {
        return issueToMigrateDao.findByAll();
    }

    public List<IssueToMigrateEntity> getAllByProjectKey(String projectKey) {
        return issueToMigrateDao.findByProjectKey(projectKey);
    }
}
