package com.newlight77.demo.vertx;


import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import io.vertx.core.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertxConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(VertxConfig.class);

    private static final String JVM_ARG_OVERRIDE_CONFIG = "override-config";
    private static final String JVM_ARG_VERTICLES_CONFIG = "override-verticles-config";
    private static final String DEFAULT_CONFIG = "default-config.json";
    private static final String VERTICLES_CONFIG = "default-verticles-config.json";

    private JsonObject config;
    private JsonObject appConfig;
    private JsonObject verticlesConfig;
    private static VertxConfig singleton;

    private VertxConfig() {
    }

    public static VertxConfig singleton() {
        if (singleton == null) {
            singleton = new VertxConfig();
        }
        return singleton;
    }

    /**
     * Vert.x -config argument. This will override default-config.json
     * @param config
     * @return
     */
    public VertxConfig config(JsonObject config) {
        Objects.requireNonNull(config, "config must not be null");
        this.config = config;
        return this;
    }

    /**
     * Get the config. If not done, this will load merging override config over vert.x -conf and over default-config.json
     * @return
     */
    public JsonObject appConfig() {
        if (appConfig == null || appConfig.isEmpty()) {
            loadConfig();
        }
        return appConfig;
    }

    /**
     * Get the default-verticles-config.json
     * @return
     */
    public JsonObject verticlesConfig() {
        if (verticlesConfig == null || verticlesConfig.isEmpty()) {
            loadVerticlesConfig();
        }
        return verticlesConfig;
    }

    public VertxConfig loadConfig() {
        LOGGER.debug("loadConfig: overriding defaultConfig by vert.x -conf arg, then by system property config file");

        JsonObject defaultConfig = loadFromFile(DEFAULT_CONFIG);
        JsonObject overrideConfig = loadConfig(JVM_ARG_OVERRIDE_CONFIG);
        LOGGER.info("defaultConfig : {}", defaultConfig);
        LOGGER.info("config : {}", config);
        LOGGER.info("overrideConfig : {}", overrideConfig);

        this.appConfig = defaultConfig;

        if (defaultConfig != null && config != null) {
            this.appConfig = defaultConfig.mergeIn(config, true);
        }
        if (appConfig != null && overrideConfig != null) {
            this.appConfig = appConfig.mergeIn(overrideConfig, true);
        }
        if( appConfig == null) {
            throw new RuntimeException("Could not load any Config");
        }

        LOGGER.info("merged all config : {}", appConfig);
        return this;
    }

    private VertxConfig loadVerticlesConfig() {
        LOGGER.info("loadVerticlesConfig : overriding defaultVerticlesConfig by system property config file");
        JsonObject defaultVerticlesConfig = loadFromFile(VERTICLES_CONFIG);
        JsonObject overrideVerticlesConfig = loadConfig(JVM_ARG_VERTICLES_CONFIG);

        LOGGER.info("defaultVerticlesConfig : {}", defaultVerticlesConfig);
        LOGGER.info("overrideVerticlesConfig : {}", overrideVerticlesConfig);

        this.verticlesConfig = defaultVerticlesConfig;

        if (defaultVerticlesConfig != null && overrideVerticlesConfig != null) {
            this.verticlesConfig = defaultVerticlesConfig.mergeIn(overrideVerticlesConfig, true);
        }
        if( verticlesConfig == null) {
            throw new RuntimeException("Could not load any verticles Config");
        }

        LOGGER.info("merged all verticles config : {}", verticlesConfig);
        return this;
    }

    private JsonObject loadConfig(String propertyName) {
        String configFile = System.getProperty(propertyName);
        LOGGER.info("loadConfig propertyName={} configFile={}", propertyName, configFile);

        if (configFile != null) {
            try {
                Path path = Paths.get(configFile);
                String file = Files.toString(path.toFile(), Charset.defaultCharset());
                return new JsonObject(file);
            } catch (IOException e) {
                LOGGER.error("Could not read the file {} --> using default configuration file from classpath", configFile, e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private JsonObject loadFromFile(String fileName) {
        LOGGER.info("loadFromFile fileName={}", fileName);
        try {
            URL jsonFileUrl = Resources.getResource(fileName);
            return new JsonObject(Resources.toString(jsonFileUrl, Charsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Could not read the default defaultConfig file {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
