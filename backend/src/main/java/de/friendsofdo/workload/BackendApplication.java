package de.friendsofdo.workload;

import com.google.cloud.AuthCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import de.friendsofdo.workload.config.properties.DatastoreProperties;
import de.friendsofdo.workload.config.properties.ProjectProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class BackendApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendApplication.class);

    @Autowired
    private ProjectProperties projectProperties;

    @Autowired
    private DatastoreProperties datastoreProperties;


    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    @Profile("development")
    public Datastore localCloudDatastore() throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("-----");
            LOGGER.debug("instantiate Datastore bean for 'development':");
            LOGGER.debug("projectId: " + projectProperties.getId());
            LOGGER.debug("authKeyFile: " + projectProperties.getAuthKeyFile());
            LOGGER.debug("datastore host: " + datastoreProperties.getHost());
            LOGGER.debug("datastore connect timeout: " + datastoreProperties.getConnectTimeout());
            LOGGER.debug("datastore read timeout: " + datastoreProperties.getReadTimeout());
            LOGGER.debug("-----");
        }

        return DatastoreOptions.newBuilder()
                .setProjectId(projectProperties.getId())
                .setHost(datastoreProperties.getHost())
                .setConnectTimeout(datastoreProperties.getConnectTimeout())
                .setReadTimeout(datastoreProperties.getReadTimeout())
                .setAuthCredentials(AuthCredentials.createForJson(new FileInputStream(projectProperties.getAuthKeyFile())))
                .setNamespace("Workload")
                .build()
                .getService();
    }

    @Bean
    @Profile("production")
    public Datastore productionCloudDatastore() throws IOException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("-----");
            LOGGER.debug("instantiate Datastore bean for 'production':");
            LOGGER.debug("projectId: " + projectProperties.getId());
            LOGGER.debug("datastore connect timeout: " + datastoreProperties.getConnectTimeout());
            LOGGER.debug("datastore read timeout: " + datastoreProperties.getReadTimeout());
            LOGGER.debug("-----");
        }

        return DatastoreOptions.newBuilder()
                .setProjectId(projectProperties.getId())
                //.setConnectTimeout(datastoreProperties.getConnectTimeout())
                //.setReadTimeout(datastoreProperties.getReadTimeout())
                .setNamespace("Workload")
                .build()
                .getService();
    }
}
