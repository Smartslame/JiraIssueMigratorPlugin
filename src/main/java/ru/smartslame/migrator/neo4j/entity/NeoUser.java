package ru.smartslame.migrator.neo4j.entity;

public class NeoUser {
    public static final String ID_PARAM_NAME = "username";
    public static final String LABEL = "USER";
    private final String username;
    private final String name;

    public NeoUser(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return getUsername();
    }

    @Override
    public String toString() {
        return "NeoUser{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
