package ru.smartslame.migrator.scheduling.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.ao.dao.IssueToMigrateDao;
import ru.smartslame.migrator.ao.entity.IssueToMigrateEntity;
import ru.smartslame.migrator.neo4j.dao.NeoIssueDao;
import ru.smartslame.migrator.neo4j.entity.NeoIssue;
import ru.smartslame.migrator.neo4j.factory.NeoIssueFactory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Scope(value = "prototype")
public class MigrationJob implements Job {
    private final IssueToMigrateDao issueToMigrateDao;
    private final NeoIssueFactory neoIssueFactory;
    private final NeoIssueDao neoIssueDao;

    @Autowired
    public MigrationJob(IssueToMigrateDao issueToMigrateDao, NeoIssueFactory neoIssueFactory, NeoIssueDao neoIssueDao) {
        this.issueToMigrateDao = issueToMigrateDao;
        this.neoIssueFactory = neoIssueFactory;
        this.neoIssueDao = neoIssueDao;
    }

    public void execute(Map<String, Serializable> jobDataMap) {
        List<IssueToMigrateEntity> issuesKeys = issueToMigrateDao.findByProjectKey((String) jobDataMap.get("PROJECT_KEY"));
        Set<String> issueKeysSet = new HashSet<>();

        for (IssueToMigrateEntity issueKey : issuesKeys) {
            if (!issueKeysSet.contains(issueKey.getIssueKey())) {
                NeoIssue neoIssue = neoIssueFactory.create(issueKey.getIssueKey());
                neoIssueDao.update(neoIssue);
                issueKeysSet.add(issueKey.getIssueKey());
            }

            issueToMigrateDao.delete(issueKey);


        }
    }

    @Override
    public String getJobType() {
        return JobType.MIGRATION;
    }
}
