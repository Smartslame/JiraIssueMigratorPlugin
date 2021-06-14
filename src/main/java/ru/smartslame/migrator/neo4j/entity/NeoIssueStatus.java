package ru.smartslame.migrator.neo4j.entity;

public class NeoIssueStatus {
    public static final String ID_PARAM_NAME = "name";
    public static final String LABEL = "STATUS";

    private final String name;

    public NeoIssueStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return getName();
    }

    @Override
    public String toString() {
        return "NeoIssueStatus{" +
                "name='" + name + '\'' +
                '}';
    }
}
