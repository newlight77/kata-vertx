package com.newlight77.demo.vertx.handler;

import com.newlight77.demo.vertx.service.TodoService;

import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class TodoHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(TodoHandler.class);

    private TodoService service;

    public TodoHandler(Vertx vertx) {
        service = ProxyHelper.createProxy(TodoService.class, vertx, "vertx.todoService");
    }

    public void handleCreateTodo(RoutingContext routingContext) {
        LOGGER.info("handleCreateTodo with uri={}", routingContext.request().absoluteURI());
        HttpServerResponse response = routingContext.response();
        JsonObject todo = routingContext.getBodyAsJson();
        if (todo == null) {
            sendError(400, response);
        } else {
            service.create(todo);
            response.end();
        }
    }

    public void handleUpdateTodo(RoutingContext routingContext) {
        LOGGER.info("handleUpdateTodo with uri={}", routingContext.request().absoluteURI());
        String id = routingContext.request().getParam("id");
        HttpServerResponse response = routingContext.response();
        if (id == null) {
            sendError(400, response);
        } else {
            JsonObject todo = routingContext.getBodyAsJson();
            if (todo == null) {
                sendError(400, response);
            } else {
                service.update(id, todo);
                response.end();
            }
        }
    }

    public void handleDeleteTodo(RoutingContext routingContext) {
        LOGGER.info("handleDeleteTodo with uri={}", routingContext.request().absoluteURI());
        String id = routingContext.request().getParam("id");
        HttpServerResponse response = routingContext.response();
        if (id == null) {
            sendError(400, response);
        } else {
            service.delete(id);
            response.end();
        }
    }

    public void handleFindTodo(RoutingContext routingContext) {
        LOGGER.info("handleFindTodo with uri={}", routingContext.request().absoluteURI());
        String id = routingContext.request().getParam("id");
        HttpServerResponse response = routingContext.response();
        if (id == null) {
            sendError(400, response);
        } else {
            JsonObject todo = service.find(id);
            if (todo == null) {
                sendError(404, response);
            } else {
                response.putHeader("content-type", "application/json").end(todo.encodePrettily());
            }
        }
    }

    public void handleFindTodos(RoutingContext routingContext) {
        LOGGER.info("handleFindTodos with uri={}", routingContext.request().absoluteURI());
        JsonArray array = new JsonArray();
        service.find().forEach(value -> array.add(value));
        routingContext.response().putHeader("content-type", "application/json").end(array.encodePrettily());
    }

    public void sendError(int statusCode, HttpServerResponse response) {
        response.setStatusCode(statusCode).end();
    }
}
