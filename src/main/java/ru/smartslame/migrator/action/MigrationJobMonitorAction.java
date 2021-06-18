package ru.smartslame.migrator.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.smartslame.migrator.ao.dao.NeoMigrationJobDao;
import ru.smartslame.migrator.ao.dao.ProjectToMigrateDao;
import ru.smartslame.migrator.ao.entity.NeoMigrationJobEntity;
import ru.smartslame.migrator.service.IssueToMigrateFullerService;
import ru.smartslame.migrator.service.JobSchedulerService;

import java.util.Objects;

public class MigrationJobMonitorAction extends JiraWebActionSupport {
    private final Logger logger = Logger.getLogger(MigrationJobMonitorAction.class);
    private final JobSchedulerService jobSchedulerService;
    private final NeoMigrationJobDao neoMigrationJobDao;
    private final ProjectToMigrateDao projectToMigrateDao;
    private final IssueToMigrateFullerService issueToMigrateFullerService;
    private String projectKey;
    private String schedule;
    private boolean fullUpdate;

    @Autowired
    public MigrationJobMonitorAction(JobSchedulerService jobSchedulerService, NeoMigrationJobDao neoMigrationJobDao, ProjectToMigrateDao projectToMigrateDao, IssueToMigrateFullerService issueToMigrateFullerService) {
        this.jobSchedulerService = jobSchedulerService;
        this.neoMigrationJobDao = neoMigrationJobDao;
        this.projectToMigrateDao = projectToMigrateDao;
        this.issueToMigrateFullerService = issueToMigrateFullerService;
    }

    public NeoMigrationJobEntity[] getAll() {
        return neoMigrationJobDao.findByAll();
    }

    public String doCreate() {
        if (projectToMigrateDao.contains(projectKey)) {
            return doReschedule();
        }
        if (fullUpdate) {
            issueToMigrateFullerService.addAll(projectKey);
        }
        projectToMigrateDao.create(projectKey);
        NeoMigrationJobEntity neoMigrationJobEntity = neoMigrationJobDao.create(projectKey, schedule);
        jobSchedulerService.schedule(neoMigrationJobEntity);
        return getRedirect("MigrationJobMonitorAction.jspa");
    }

    public String doDelete() {
        neoMigrationJobDao.deleteByProjectKey(projectKey);
        projectToMigrateDao.deleteByProjectKey(projectKey);
        jobSchedulerService.unschedule(projectKey);
        return getRedirect("MigrationJobMonitorAction.jspa");
    }

    public String doReschedule() {
        NeoMigrationJobEntity neoMigrationJobEntity = neoMigrationJobDao.update(projectKey, schedule);
        jobSchedulerService.schedule(neoMigrationJobEntity);
        return getRedirect("MigrationJobMonitorAction.jspa");
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setFullUpdate(Boolean fullUpdate) {
        this.fullUpdate = Objects.nonNull(fullUpdate) ? fullUpdate : false;
    }
}
