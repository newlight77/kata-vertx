package com.newlight77.demo.vertx.verticle;

import com.newlight77.demo.vertx.router.TodoRouter;
import com.newlight77.demo.vertx.service.TodoService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.serviceproxy.ProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodoConsumerVerticle extends AbstractVerticle {

  private final static Logger LOGGER = LoggerFactory.getLogger(TodoConsumerVerticle.class);

  @Override
    public void start(Future<Void> future) throws Exception {
        TodoService service = ProxyHelper.createProxy(TodoService.class, vertx, "vertx.todoService");

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
}
