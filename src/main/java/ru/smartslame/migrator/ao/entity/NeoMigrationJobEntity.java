package ru.smartslame.migrator.ao.entity;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("MigrationJob")
public interface NeoMigrationJobEntity extends Entity {
    String getProjectKey();
    void setProjectKey(String projectKey);
    String getSchedule();
    void setSchedule(String schedule);
}