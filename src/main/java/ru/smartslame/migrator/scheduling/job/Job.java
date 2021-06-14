package ru.smartslame.migrator.scheduling.job;

import java.io.Serializable;
import java.util.Map;

public interface Job {
    String JOB_ID_SUFFIX = "_MigrationJob";
    void execute(Map<String, Serializable> jobDataMap);
}
