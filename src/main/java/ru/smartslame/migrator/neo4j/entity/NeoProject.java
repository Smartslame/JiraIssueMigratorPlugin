package ru.smartslame.migrator.neo4j.entity;

import com.atlassian.jira.user.ApplicationUser;

public class NeoProject {
    public static final String ID_PARAM_NAME = "key";
    public static final String LABEL = "PROJECT";

    private final String name;
    private final String key;

    public NeoProject(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getId() {
        return getKey();
    }

    @Override
    public String toString() {
        return "NeoProject{" +
                "name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
