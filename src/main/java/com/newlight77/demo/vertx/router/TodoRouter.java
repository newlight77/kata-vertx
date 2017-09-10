package com.newlight77.demo.vertx.router;

import com.newlight77.demo.vertx.handler.TodoHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class TodoRouter {

    private final static Logger LOGGER = LoggerFactory.getLogger(TodoRouter.class);

    @Inject
    private TodoHandler todoHandler;

    public Router route(Vertx vertx) {
        LOGGER.info("configureRouter for verticle : {}", this.getClass().getSimpleName());


        final Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.post("/todos").handler(context -> todoHandler.handleCreateTodo(context));
        router.put("/todos/:id").handler(context -> todoHandler.handleUpdateTodo(context));
        router.delete("/todos/:id").handler(context -> todoHandler.handleDeleteTodo(context));
        router.get("/todos/:id").handler(context -> todoHandler.handleFindTodo(context));
        router.get("/todos").handler(context -> todoHandler.handleFindTodos(context));

        return router;
    }
}
