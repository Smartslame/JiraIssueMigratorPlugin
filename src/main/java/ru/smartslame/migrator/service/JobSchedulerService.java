package ru.smartslame.migrator.service;

import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.scheduler.SchedulerService;
import com.atlassian.scheduler.SchedulerServiceException;
import com.atlassian.scheduler.config.JobConfig;
import com.atlassian.scheduler.config.JobId;
import com.atlassian.scheduler.config.RunMode;
import com.atlassian.scheduler.config.Schedule;
import com.google.common.collect.ImmutableMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.ao.entity.NeoMigrationJobEntity;
import ru.smartslame.migrator.scheduling.NeoMigrationJobRunner;
import ru.smartslame.migrator.scheduling.NeoMigrationJobRunnerImpl;
import ru.smartslame.migrator.scheduling.job.JobType;

import java.time.Instant;
import java.util.Date;

@Component
public class JobSchedulerService implements InitializingBean {
    private final Logger logger = Logger.getLogger(JobSchedulerService.class);
    @JiraImport
    private final SchedulerService schedulerService;
    private final NeoMigrationJobRunner neoMigrationJobRunner;

    @Autowired
    public JobSchedulerService(SchedulerService schedulerService, NeoMigrationJobRunner neoMigrationJobRunner) {
        this.schedulerService = schedulerService;
        this.neoMigrationJobRunner = neoMigrationJobRunner;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        schedulerService.registerJobRunner(neoMigrationJobRunner.JOB_RUNNER_KEY, neoMigrationJobRunner);
    }

    public void schedule(NeoMigrationJobEntity neoMigrationJobEntity) {
        JobConfig jobConfig = JobConfig.forJobRunnerKey(NeoMigrationJobRunnerImpl.JOB_RUNNER_KEY)
                .withParameters(ImmutableMap.of(
                        NeoMigrationJobRunner.PROJECT_KEY, neoMigrationJobEntity.getProjectKey(),
                        JobType.JOB_TYPE_KEY, JobType.MIGRATION))
                .withSchedule(Schedule.forInterval(Integer.parseInt(neoMigrationJobEntity.getSchedule()) * 1000 * 60, Date.from(Instant.now())))
                .withRunMode(RunMode.RUN_LOCALLY);

        JobId jobId = JobId.of(neoMigrationJobEntity.getProjectKey() + "_" + JobType.MIGRATION);

        try {
            schedulerService.scheduleJob(jobId, jobConfig);
        } catch (SchedulerServiceException e) {
            e.printStackTrace();
        }

        logger.debug(String.format(jobId.toString() + " job scheduled to run with interval: %s ms", neoMigrationJobEntity.getSchedule()));
    }

    public void unschedule(String projectKey) {
        JobId jobId = JobId.of(projectKey + JobType.MIGRATION);

        schedulerService.unscheduleJob(jobId);

        logger.debug(jobId.toString() + " job unscheduled");
    }
}
