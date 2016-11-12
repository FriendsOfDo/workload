package de.friendsofdo.workload.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "project")
public class ProjectProperties {

    private String id;
    private String authKeyFile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthKeyFile() {
        return authKeyFile;
    }

    public void setAuthKeyFile(String authKeyFile) {
        this.authKeyFile = authKeyFile;
    }
}
