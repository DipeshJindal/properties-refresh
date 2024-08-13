package org.developement.asyncprogramming.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EnvHelper implements EnvironmentAware {

    @Autowired
    private RestartEndpoint restartEndpoint;

    private Environment environment;

    @Autowired
    public ConfigurableEnvironment configurableEnvironment;

    public void setProperty(Map<String, Object> props) {
        configurableEnvironment.getPropertySources().addFirst(new MapPropertySource("dynamicProps", props));
        restartEndpoint.restart();
    }

    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
