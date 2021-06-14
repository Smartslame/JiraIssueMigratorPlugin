package ru.smartslame.migrator.ao.entity;

import net.java.ao.Entity;
import net.java.ao.schema.Table;

@Table("Project")
public interface ProjectToMigrateEntity extends Entity {
    String getKey();
    void setKey(String projectKey);
}
