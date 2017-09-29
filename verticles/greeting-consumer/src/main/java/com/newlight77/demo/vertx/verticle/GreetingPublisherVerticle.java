package com.newlight77.demo.vertx.verticle;

import com.newlight77.demo.vertx.service.GreetingService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ProxyHelper;

public class GreetingPublisherVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        GreetingService service = ProxyHelper.createProxy(GreetingService.class, vertx, "vertx.greetingService");

        JsonObject document = new JsonObject().put("name", "vertx");

        service.process(document, (r) -> {
            if (r.succeeded()) {
                System.out.println(r.result().encodePrettily());
            } else {
                System.out.println(r.cause());
            }
        });
    }
}
