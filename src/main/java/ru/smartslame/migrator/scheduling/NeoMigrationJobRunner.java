package ru.smartslame.migrator.scheduling;

import com.atlassian.scheduler.JobRunner;
import com.atlassian.scheduler.config.JobRunnerKey;

public interface NeoMigrationJobRunner extends JobRunner {
    JobRunnerKey JOB_RUNNER_KEY = JobRunnerKey.of(NeoMigrationJobRunner.class.getName());
    String PROJECT_KEY = "PROJECT_KEY";
}
