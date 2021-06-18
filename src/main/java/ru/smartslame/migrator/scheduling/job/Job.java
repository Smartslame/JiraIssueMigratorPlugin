package ru.smartslame.migrator.scheduling.job;

import java.io.Serializable;
import java.util.Map;

public interface Job {
    void execute(Map<String, Serializable> jobDataMap);
    String getJobType();
}
