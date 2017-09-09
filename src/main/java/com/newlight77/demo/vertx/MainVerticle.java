package com.newlight77.demo.vertx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class MainVerticle extends AbstractVerticle {

    private static Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start(Future<Void> future) {

        JsonObject verticlesConfig = VertxConfig.singleton().verticlesConfig();

        Router.route(verticlesConfig);

        Deployer.singleton().deploy();

    }


}
