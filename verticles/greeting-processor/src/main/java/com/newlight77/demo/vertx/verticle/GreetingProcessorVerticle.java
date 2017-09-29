package com.newlight77.demo.vertx.verticle;

import com.newlight77.demo.vertx.service.GreetingService;
import com.newlight77.demo.vertx.service.GreetingServiceImp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.serviceproxy.ProxyHelper;

public class GreetingProcessorVerticle extends AbstractVerticle {

    GreetingService service;

    @Override
    public void start(Future<Void> future) throws Exception {

        service = new GreetingServiceImp();
        ProxyHelper.registerService(GreetingService.class, vertx, service, "vertx.greetingService");

        Router router = Router.router(vertx);

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
}
