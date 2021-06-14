package ru.smartslame.migrator.service;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.issue.link.IssueLinkCreatedEvent;
import com.atlassian.jira.event.issue.link.IssueLinkDeletedEvent;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.link.IssueLink;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.ao.dao.IssueToMigrateDao;
import ru.smartslame.migrator.neo4j.factory.NeoIssueFactory;

@Component
public class IssueEventListenerService implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(IssueEventListenerService.class);

    @JiraImport
    private final EventPublisher eventPublisher;

    private final IssueToMigrateDao issueToMigrateDao;

    private final NeoIssueFactory neoIssueFactory;

    @Autowired
    public IssueEventListenerService(EventPublisher eventPublisher, IssueToMigrateDao issueToMigrateDao, NeoIssueFactory neoIssueFactory) {
        this.eventPublisher = eventPublisher;
        this.issueToMigrateDao = issueToMigrateDao;
        this.neoIssueFactory = neoIssueFactory;
    }

    @Override
    public void afterPropertiesSet() {
        eventPublisher.register(this);
    }

    @Override
    public void destroy() {
        eventPublisher.unregister(this);
    }

    @EventListener
    public void onIssueEvent(IssueEvent issueEvent) {
        Long eventTypeId = issueEvent.getEventTypeId();
        Issue issue = issueEvent.getIssue();
        logger.debug("Event type id : {}", eventTypeId);
        logger.debug("Issue : {}", issue.getKey());
        issueToMigrateDao.create(issue.getProjectObject().getKey(), issue.getKey());
    }

    @EventListener
    public void onIssueLinkCreatedEvent(IssueLinkCreatedEvent issueLinkEvent) {
        IssueLink issueLink = issueLinkEvent.getIssueLink();
        Issue sourceIssue = issueLink.getSourceObject();
        logger.debug("Issue link created event");
        logger.debug(String.format("From issue: %s", sourceIssue.getKey()));
        logger.debug(String.format("To issue: %s", issueLink.getDestinationObject().getKey()));
        logger.debug(String.format("Link type: %s", issueLink.getIssueLinkType().getName()));
        issueToMigrateDao.create(sourceIssue.getProjectObject().getKey(), sourceIssue.getKey());
    }

    @EventListener
    public void onIssueLinkDeletedEvent(IssueLinkDeletedEvent issueLinkEvent) {
        IssueLink issueLink = issueLinkEvent.getIssueLink();
        Issue sourceIssue = issueLink.getSourceObject();
        logger.debug("Issue link deleted event");
        logger.debug(String.format("From issue: %s", sourceIssue.getKey()));
        logger.debug(String.format("To issue: %s", issueLink.getDestinationObject().getKey()));
        logger.debug(String.format("Link type: %s", issueLink.getIssueLinkType().getName()));
        issueToMigrateDao.create(sourceIssue.getProjectObject().getKey(), sourceIssue.getKey());
    }

}