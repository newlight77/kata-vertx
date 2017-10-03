package com.newlight77.demo.vertx.binder;

import com.google.inject.AbstractModule;
import com.newlight77.demo.vertx.service.TodoService;
import com.newlight77.demo.vertx.service.TodoServiceImp;

public class BinderModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TodoService.class).to(TodoServiceImp.class);
    }
}
