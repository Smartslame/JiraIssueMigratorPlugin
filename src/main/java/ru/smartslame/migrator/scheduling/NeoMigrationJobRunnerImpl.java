package ru.smartslame.migrator.scheduling;

import com.atlassian.scheduler.JobRunnerRequest;
import com.atlassian.scheduler.JobRunnerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.scheduling.job.Job;

import javax.annotation.Nullable;

@Component
public class NeoMigrationJobRunnerImpl implements NeoMigrationJobRunner {
    private final Job migrationJob;

    @Autowired
    public NeoMigrationJobRunnerImpl(Job migrationJob) {
        this.migrationJob = migrationJob;
    }

    @Nullable
    @Override
    public JobRunnerResponse runJob(JobRunnerRequest jobRunnerRequest) {
        migrationJob.execute(jobRunnerRequest.getJobConfig().getParameters());
        return JobRunnerResponse.success();
    }
}
