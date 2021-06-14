package ru.smartslame.migrator.neo4j.factory;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.link.IssueLink;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.atlassian.jira.issue.link.IssueLinkTypeManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.smartslame.migrator.neo4j.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype")
public class NeoIssueFactory {
    private final Logger logger = Logger.getLogger(NeoIssueFactory.class);
    @JiraImport
    private final IssueManager issueManager;
    @JiraImport
    private final IssueLinkManager issueLinkManager;
    @JiraImport
    private final IssueLinkTypeManager issueLinkTypeManager;

    @Autowired
    public NeoIssueFactory(IssueManager issueManager, IssueLinkManager issueLinkManager, IssueLinkTypeManager issueLinkTypeManager) {
        this.issueManager = issueManager;
        this.issueLinkManager = issueLinkManager;
        this.issueLinkTypeManager = issueLinkTypeManager;
    }

    public NeoIssue create(String issueKey) {
        MutableIssue jiraIssue = issueManager.getIssueByCurrentKey(issueKey);

        return new NeoIssue(
                jiraIssue.getKey(),
                new NeoIssueType(jiraIssue.getIssueType().getName()),
                new NeoIssueStatus(jiraIssue.getStatus().getName()),
                jiraIssue.getSummary(),
                jiraIssue.getCreated().toLocalDateTime(),
                jiraIssue.getUpdated().toLocalDateTime(),
                new NeoProject(
                        jiraIssue.getProjectObject().getName(),
                        jiraIssue.getProjectObject().getKey()),
                new NeoUser(
                        jiraIssue.getAssigneeUser().getUsername(),
                        jiraIssue.getAssigneeUser().getName()),
                new NeoUser(
                        jiraIssue.getReporterUser().getUsername(),
                        jiraIssue.getReporterUser().getName()),
                findOutwardLinks(jiraIssue));
    }

    public List<Pair<String, String>> findOutwardLinks(Issue issue) {
        List<IssueLink> outwardLinks = issueLinkManager.getOutwardLinks(issue.getId());
        return outwardLinks.stream()
                .map(link ->
                        Pair.of(link.getDestinationObject().getKey(),
                                issueLinkTypeManager.getIssueLinkType(link.getLinkTypeId()).getName()))
                .collect(Collectors.toList());
    }


}
