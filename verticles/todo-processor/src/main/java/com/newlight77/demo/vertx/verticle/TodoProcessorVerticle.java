package com.newlight77.demo.vertx.verticle;

import com.newlight77.demo.vertx.service.TodoService;

import com.newlight77.demo.vertx.service.TodoServiceImp;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ProxyHelper;

public class TodoProcessorVerticle extends AbstractVerticle {

    TodoService service;

    @Override
    public void start(Future<Void> future) throws Exception {

        service = new TodoServiceImp();
        ProxyHelper.registerService(TodoService.class, vertx, service, "vertx.todoService");

    }
}
