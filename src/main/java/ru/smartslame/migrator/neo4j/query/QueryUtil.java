package ru.smartslame.migrator.neo4j.query;

import org.apache.commons.lang3.tuple.Pair;
import ru.smartslame.migrator.neo4j.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QueryUtil {
    public static String deleteOutgoingRelationBetweenTwoNodes(String fromLabel, String fromIdParamName, String fromId, String toLabel, String relationToDelete) {
        return String.format(
                "MATCH (:%s {%s:\"%s\"})-[r:%s]->(:%s) DELETE r",
                fromLabel, fromIdParamName, fromId, relationToDelete, toLabel);
    }

    public static String deleteIncomingRelationBetweenTwoNodes(String fromLabel, String fromIdParamName, String fromId, String toLabel, String relationToDelete) {
        return String.format(
                "MATCH (:%s {%s:\"%s\"})<-[r:%s]-(:%s) DELETE r",
                fromLabel, fromIdParamName, fromId, relationToDelete, toLabel);
    }

    public static String deleteOutgoingRelations(String fromLabel, String fromIdParamName, String fromId) {
        return String.format(
                "MATCH (:%s {%s:\"%s\"})-[r]->() DELETE r",
                fromLabel, fromIdParamName, fromId);
    }


    public static String setRelationBetweenTwoNodes(String fromLabel, String fromIdParamName, String fromId, String toLabel, String toIdParamName, String toId, String relationToSet) {
        return String.format(
                "MATCH (f:%s {%s: \"%s\"}) " +
                        "MATCH (t:%s {%s : \"%s\"}) " +
                        "MERGE (f)-[:%s]->(t)",
                fromLabel, fromIdParamName, fromId, toLabel, toIdParamName, toId, relationToSet);

    }

    public static String setRelationBetweenTwoNodes2NotExist(String fromLabel, String fromIdParamName, String fromId, String toLabel, String toIdParamName, String toId, String relationToSet) {
        return String.format(
                "MATCH (f:%s {%s: \"%s\"}) " +
                        "MERGE (t:%s {%s : \"%s\"}) " +
                        "MERGE (f)-[:%s]->(t)",
                fromLabel, fromIdParamName, fromId, toLabel, toIdParamName, toId, relationToSet);

    }

    public static String createIssue(NeoIssue issue) {
        return String.format(
                "MERGE (n:%s {%s:\"%s\"}) " +
                        "SET n.key = \"%s\", " +
                        "n.summary = \"%s\", " +
                        "n.created = \"%s\", " +
                        "n.updated = \"%s\"",
                NeoIssue.LABEL, NeoIssue.ID_PARAM_NAME, issue.getId(),
                issue.getKey(),
                issue.getSummary(),
                issue.getCreated(),
                issue.getUpdated());
    }

    public static String createStatus(NeoIssueStatus status) {
        return String.format(
                "MERGE (n:%s {%s:\"%s\"}) " +
                        "ON CREATE SET n.name = \"%s\"",
                NeoIssueStatus.LABEL, NeoIssueStatus.ID_PARAM_NAME, status.getId(),
                status.getName());
    }

    public static String createType(NeoIssueType type) {
        return String.format(
                "MERGE (n:%s {%s:\"%s\"}) " +
                        "ON CREATE SET n.name = \"%s\"",
                NeoIssueType.LABEL, NeoIssueType.ID_PARAM_NAME, type.getId(),
                type.getName());
    }

    public static String createProject(NeoProject project) {
        return String.format(
                "MERGE (n:%s {%s:\"%s\"}) " +
                        "SET n.name = \"%s\", " +
                        "n.key = \"%s\"",
                NeoProject.LABEL, NeoProject.ID_PARAM_NAME, project.getId(),
                project.getName(),
                project.getKey());
    }

    public static String createUser(NeoUser user) {
        return String.format(
                "MERGE (n:%s {%s:\"%s\"}) " +
                        "SET n.name = \"%s\", " +
                        "n.username = \"%s\"",
                NeoUser.LABEL, NeoUser.ID_PARAM_NAME, user.getId(),
                user.getName(),
                user.getUsername());
    }

    public static List<String> getCompleteQuery(NeoIssue neoIssue) {
        List<String> completeQuery = new ArrayList<>();
        completeQuery.add(createIssue(neoIssue));
        completeQuery.add(deleteOutgoingRelations(NeoIssue.LABEL, NeoIssue.ID_PARAM_NAME, neoIssue.getId()));
        if (Objects.nonNull(neoIssue.getStatus())) {
            NeoIssueStatus status = neoIssue.getStatus();
            completeQuery.add(createStatus(status));
            completeQuery.add(setRelationBetweenTwoNodes(NeoIssue.LABEL, NeoIssue.ID_PARAM_NAME, neoIssue.getId(), NeoIssueStatus.LABEL, NeoIssueStatus.ID_PARAM_NAME, status.getId(), NeoRelation.HAS_STATUS));
        }

        if (Objects.nonNull(neoIssue.getType())) {
            NeoIssueType type = neoIssue.getType();
            completeQuery.add(createType(type));
            completeQuery.add(setRelationBetweenTwoNodes(NeoIssue.LABEL, NeoIssue.ID_PARAM_NAME, neoIssue.getId(), NeoIssueType.LABEL, NeoIssueType.ID_PARAM_NAME, type.getId(), NeoRelation.HAS_TYPE));
        }

        if (Objects.nonNull(neoIssue.getProject())) {
            NeoProject project = neoIssue.getProject();
            completeQuery.add(createProject(project));
            completeQuery.add(setRelationBetweenTwoNodes(NeoIssue.LABEL, NeoIssue.ID_PARAM_NAME, neoIssue.getId(), NeoProject.LABEL, NeoProject.ID_PARAM_NAME, project.getId(), NeoRelation.PROJECT));
        }

        if (Objects.nonNull(neoIssue.getAssignee())) {
            NeoUser user = neoIssue.getAssignee();
            completeQuery.add(createUser(user));
            completeQuery.add(setRelationBetweenTwoNodes(NeoIssue.LABEL, NeoIssue.ID_PARAM_NAME, neoIssue.getId(), NeoUser.LABEL, NeoUser.ID_PARAM_NAME, user.getId(), NeoRelation.ASSIGNED_TO));
        }

        if (Objects.nonNull(neoIssue.getReporter())) {
            NeoUser user = neoIssue.getReporter();
            completeQuery.add(createUser(user));
            completeQuery.add(setRelationBetweenTwoNodes(NeoIssue.LABEL, NeoIssue.ID_PARAM_NAME, neoIssue.getId(), NeoUser.LABEL, NeoUser.ID_PARAM_NAME, user.getId(), NeoRelation.REPORTED_BY));
        }

        for (Pair<String, String> keyLink : neoIssue.getOutwardIssueKeyLinks()) {
            completeQuery.add(setRelationBetweenTwoNodes2NotExist(NeoIssue.LABEL, NeoIssue.ID_PARAM_NAME, neoIssue.getId(), NeoIssue.LABEL, NeoIssue.ID_PARAM_NAME, keyLink.getLeft(), keyLink.getRight()));
        }

        return completeQuery;

    }


}
