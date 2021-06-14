package ru.smartslame.migrator.neo4j.entity;

public class NeoIssueType {
    public static final String ID_PARAM_NAME = "name";
    public static final String LABEL = "TYPE";

    private final String name;

    public NeoIssueType(String name) {
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
        return "NeoIssueType{" +
                "name='" + name + '\'' +
                '}';
    }
}
