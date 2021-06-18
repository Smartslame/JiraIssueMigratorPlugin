package ru.smartslame.migrator.scheduling.job;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
public class AddAllIssuesJob implements Job {
    @Override
    public void execute(Map<String, Serializable> jobDataMap) {
    }

    @Override
    public String getJobType() {
        return JobType.ADD_ALL;
    }
}
