package ru.smartslame.migrator.scheduling;

import com.atlassian.scheduler.JobRunnerRequest;
import com.atlassian.scheduler.JobRunnerResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.scheduling.job.Job;
import ru.smartslame.migrator.scheduling.job.JobType;
import ru.smartslame.migrator.service.JobSchedulerService;

import javax.annotation.Nullable;
import java.io.Serializable;

@Component
public class NeoMigrationJobRunnerImpl implements NeoMigrationJobRunner {
    private final Logger logger = Logger.getLogger(NeoMigrationJobRunnerImpl.class);
    private final JobFactory jobFactory;

    @Autowired
    public NeoMigrationJobRunnerImpl(JobFactory jobFactory) {
        this.jobFactory = jobFactory;
    }

    @Nullable
    @Override
    public JobRunnerResponse runJob(JobRunnerRequest jobRunnerRequest) {
        String job_type = (String) jobRunnerRequest.getJobConfig().getParameters().get(JobType.JOB_TYPE_KEY);
        logger.info(job_type);
        Job job = jobFactory.getJobByType(job_type);
        job.execute(jobRunnerRequest.getJobConfig().getParameters());
        return JobRunnerResponse.success();
    }
}
