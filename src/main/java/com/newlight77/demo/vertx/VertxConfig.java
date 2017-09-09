package com.newlight77.demo.vertx;


import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.vertx.core.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertxConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(VertxConfig.class);

    private static final String JVM_ARG_APP_CONFIG = "app-config";
    private static final String DEFAULT_CONFIG = "app-config.json";
    private static final String VERTICLES_CONFIG = "verticles-config.json";

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

    public JsonObject appConfig() {
        if (appConfig == null || appConfig.isEmpty()) {
            loadAppConfig();
        }
        return appConfig;
    }

    public JsonObject verticlesConfig() {
        if (verticlesConfig == null || verticlesConfig.isEmpty()) {
            loadVerticlesConfig();
        }
        return verticlesConfig;
    }

    public VertxConfig loadBeneathAppConfig(JsonObject config) {
        if (config == null || config.isEmpty()) {
            LOGGER.warn("failed loading appConfig beneath current one : config={}", config);
            return this;
        }

        LOGGER.debug("loading appConfig beneath current one. appConfig from arg will be overridden. config={} appConfig={}", config, appConfig);
        this.appConfig = config.mergeIn(appConfig());
        LOGGER.info("merged appConfig : {}", appConfig);
        return this;
    }

    private void loadAppConfig() {
        LOGGER.info("loadAppConfig");
        if (!loadConfig(JVM_ARG_APP_CONFIG)) {
            appConfig = loadFromFile(DEFAULT_CONFIG);
        }
    }

    private void loadVerticlesConfig() {
        LOGGER.info("loadVerticlesConfig");
        verticlesConfig = loadFromFile(VERTICLES_CONFIG);
    }

    private boolean loadConfig(String propertyName) {
        String configFile = System.getProperties().getProperty(propertyName);
        LOGGER.info("loadConfig propertyName={} configFile={}", propertyName, configFile);

        if (configFile != null) {
            try {
                Path path = Paths.get(configFile);
                String file = Files.toString(path.toFile(), Charset.defaultCharset());
                appConfig = new JsonObject(file);
                return true;
            } catch (IOException e) {
                LOGGER.error("Could not read the file {} --> using default configuration file from classpath", configFile, e.getMessage());
            }
        }
        return false;
    }

    private JsonObject loadFromFile(String fileName) {
        LOGGER.info("loadFromFile fileName={}", fileName);
        try {
            URL jsonFileUrl = Resources.getResource(fileName);
            return new JsonObject(Resources.toString(jsonFileUrl, Charsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Could not read the default appConfig file {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
