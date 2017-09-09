package com.newlight77.demo.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class VerticleLauncher extends AbstractVerticle {

    private static Logger LOGGER = LoggerFactory.getLogger(VerticleLauncher.class);

    @Override
    public void start(Future<Void> future) {

        JsonObject appConfig = VertxConfig.singleton().appConfig();
        LOGGER.info("start appConfig : {}", appConfig);
        Deployer.singleton().deploy(appConfig);

    }

    public static void main(String[] args) {
        new VerticleLauncher().start(Future.future());
    }

}
