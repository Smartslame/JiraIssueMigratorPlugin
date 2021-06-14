package ru.smartslame.migrator.service;

import org.apache.log4j.Logger;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class NeoConnectionService {
    private final Logger logger = Logger.getLogger(NeoConnectionService.class);
    private String url = "bolt://localhost:7687";
    private String user = "neo4j";
    private Boolean isConnected = false;
    private String password = "";
    private Driver driver;


    public void updateCredentials(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void connect() {
        boolean catchException = false;
        driver = GraphDatabase.driver(URI.create(this.url), AuthTokens.basic(this.user, this.password));
        try {
            driver.verifyConnectivity();
        } catch (Exception e) {
            catchException = true;
        }
        isConnected = !catchException;
        logger.debug(String.format("Connect is: %s", isConnected));
    }

    public Session getSession() {
        if (!this.isConnected) {
            throw new RuntimeException("Neo4j DB not connected");
        }

        return driver.session();
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Boolean isConnected() {
        return isConnected;
    }


}
