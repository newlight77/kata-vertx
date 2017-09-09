package com.newlight77.demo.vertx;

import com.google.inject.AbstractModule;

import com.newlight77.demo.vertx.handler.TodoHandler;
import com.newlight77.demo.vertx.service.TodoService;

import javax.inject.Singleton;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class BinderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TodoService.class).in(Singleton.class);
        bind(TodoHandler.class).in(Singleton.class);

    }
}
