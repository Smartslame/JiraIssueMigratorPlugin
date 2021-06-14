package ru.smartslame.migrator.neo4j.entity;

import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.List;

public class NeoIssue {
    public static final String ID_PARAM_NAME = "key";
    public static final String LABEL = "ISSUE";

    private final String key;
    private final NeoIssueType type;
    private final NeoIssueStatus status;
    private final String summary;
    private final LocalDateTime created;
    private final LocalDateTime updated;
    private final NeoProject project;
    private final NeoUser assignee;
    private final NeoUser reporter;
    private final List<Pair<String, String>> outwardIssueKeyLinks;
    public NeoIssue(String key,
                    NeoIssueType type,
                    NeoIssueStatus status,
                    String summary,
                    LocalDateTime created,
                    LocalDateTime updated,
                    NeoProject project,
                    NeoUser assignee,
                    NeoUser reporter,
                    List<Pair<String, String>> outwardIssueKeyLinks) {
        this.key = key;
        this.type = type;
        this.status = status;
        this.summary = summary;
        this.created = created;
        this.updated = updated;
        this.project = project;
        this.assignee = assignee;
        this.reporter = reporter;
        this.outwardIssueKeyLinks = outwardIssueKeyLinks;
    }

    public String getKey() {
        return key;
    }

    public NeoIssueType getType() {
        return type;
    }

    public NeoIssueStatus getStatus() {
        return status;
    }

    public String getSummary() {
        return summary;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public NeoProject getProject() {
        return project;
    }

    public NeoUser getAssignee() {
        return assignee;
    }

    public NeoUser getReporter() {
        return reporter;
    }

    public List<Pair<String, String>> getOutwardIssueKeyLinks() {
        return outwardIssueKeyLinks;
    }

    public String getId() {
        return getKey();
    }

    @Override
    public String toString() {

        return "NeoIssue{" + '\n' +
                "key='" + key + '\'' + '\n' +
                ", type=" + type + '\n' +
                ", status=" + status + '\n' +
                ", summary='" + summary + '\'' + '\n' +
                ", created=" + created + '\n' +
                ", updated=" + updated + '\n' +
                ", project=" + project + '\n' +
                ", assignee=" + assignee + '\n' +
                ", reporter=" + reporter + '\n' +
                ", outwardLinks=" + outwardsLinksToString() + '\n' +
                '}';
    }

    private String outwardsLinksToString() {
        StringBuilder sb = new StringBuilder("[ ");
        for (Pair<String, String> outwardLink : outwardIssueKeyLinks) {
            sb.append("(issueKey: ")
                    .append(outwardLink.getLeft())
                    .append(", linkName: ")
                    .append(outwardLink.getRight())
                    .append("), ");

        }
        if (!outwardIssueKeyLinks.isEmpty()) {
            sb.deleteCharAt(sb.length() - 2); //delete last comma
        }
        sb.append("]");
        return sb.toString();
    }
}
