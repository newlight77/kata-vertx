package com.newlight77.demo.vertx.verticle;

import com.google.inject.Guice;
import com.newlight77.demo.vertx.binder.BinderModule;
import com.newlight77.demo.vertx.router.TodoRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodoConsumerVerticle extends AbstractVerticle {

    private final static Logger LOGGER = LoggerFactory.getLogger(TodoConsumerVerticle.class);

    @Override
    public void start(Future<Void> future) throws Exception {

        Guice
            .createInjector(new BinderModule(vertx))
            .injectMembers(this);

        final Router router = new TodoRouter(vertx).route();

        LOGGER.info("starting verticle : {}", this.getClass().getSimpleName());

        vertx
            .createHttpServer()
            .requestHandler(router::accept)
            //.requestHandler(req -> req.response().end("Hello World!"))
            .listen(
                // Retrieve the port from the configuration,
                // default to 8080.
                8080,
                result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                });

    }

    @Override
    public void init(Vertx vertx, Context context) {
        this.vertx = vertx;
        this.context = context;

/*
        Guice
            .createInjector(new BinderModule(vertx))
            .injectMembers(this);
*/

    }
}
