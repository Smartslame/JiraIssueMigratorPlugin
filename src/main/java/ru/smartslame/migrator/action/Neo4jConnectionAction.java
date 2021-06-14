package ru.smartslame.migrator.action;

import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.springframework.beans.factory.annotation.Autowired;
import ru.smartslame.migrator.service.NeoConnectionService;

public class Neo4jConnectionAction extends JiraWebActionSupport {
    private final NeoConnectionService neoConnectionService;
    private String url;
    private String user;
    private String password;
    private Boolean connected;

    @Autowired
    public Neo4jConnectionAction(NeoConnectionService neoConnectionService) {
        this.neoConnectionService = neoConnectionService;
        this.url = neoConnectionService.getUrl();
        this.user = neoConnectionService.getUser();
        this.password = "";
        this.connected = neoConnectionService.isConnected();
    }

    public String doConnect() {
        neoConnectionService.updateCredentials(url, user, password);
        neoConnectionService.connect();
        return getRedirect("Neo4jConnectionAction.jspa");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getConnected() {
        return connected;
    }
}
