package com.newlight77.demo.vertx.verticle;

import com.google.inject.Guice;

import com.newlight77.demo.vertx.BinderModule;
import com.newlight77.demo.vertx.Deployer;
import com.newlight77.demo.vertx.VertxConfig;
import com.newlight77.demo.vertx.router.TodoRouter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class TodoVerticle extends AbstractVerticle {

    private final static Logger LOGGER = LoggerFactory.getLogger(Deployer.class);

    @Inject
    private TodoRouter todoRouter;

    public void start(Future<Void> future) throws Exception {

        Guice
            .createInjector(new BinderModule())
            .injectMembers(this);

        JsonObject appConfig = VertxConfig.singleton().config(config()).appConfig();

        final Router router = todoRouter.route(vertx);

        LOGGER.info("starting verticle : {}", this.getClass().getSimpleName());

        vertx
            .createHttpServer()
            .requestHandler(router::accept)
            //.requestHandler(req -> req.response().end("Hello World!"))
            .listen(
                // Retrieve the port from the configuration,
                // default to 8080.
                appConfig.getInteger("http.port", 8080),
                result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });
    }

}
