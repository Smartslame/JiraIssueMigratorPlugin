package ru.smartslame.migrator.scheduling;

import com.atlassian.jira.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.scheduling.job.Job;

import java.util.List;

@Component
public class JobFactory {
    private static final Logger logger = LoggerFactory.getLogger(JobFactory.class);
    private final List<Job> jobs;

    @Autowired
    public JobFactory(List<Job> jobs) {
        this.jobs = jobs;
    }

    public Job getJobByType(String jobType) {
        return jobs.stream().filter(job -> job.getJobType().equals(jobType)).findFirst().orElseThrow(NotFoundException::new);
    }
}
