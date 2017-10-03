package com.newlight77.demo.vertx.verticle;

import com.google.inject.Guice;
import com.newlight77.demo.vertx.binder.BinderModule;
import com.newlight77.demo.vertx.service.TodoService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ProxyHelper;

import javax.inject.Inject;

public class TodoProcessorVerticle extends AbstractVerticle {

    @Inject
    TodoService service;

    @Override
    public void start(Future<Void> future) throws Exception {

        Guice
            .createInjector(new BinderModule())
            .injectMembers(this);

        ProxyHelper.registerService(TodoService.class, vertx, service, "vertx.todoService");

    }
}
