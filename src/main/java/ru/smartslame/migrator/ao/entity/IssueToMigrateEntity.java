package ru.smartslame.migrator.ao.entity;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("Issue")
public interface IssueToMigrateEntity extends Entity {
    String getIssueKey();

    void setIssueKey(String issueKey);

    String getProjectKey();

    void setProjectKey(String projectKey);

}
