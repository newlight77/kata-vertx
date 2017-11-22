package com.newlight77.demo.vertx.binder;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.newlight77.demo.vertx.service.TodoService;
import com.newlight77.demo.vertx.service.TodoServiceImp;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ProxyHelper;

public class BinderModule extends AbstractModule {

    private Vertx vertx;
    public BinderModule(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected void configure() {
//        TodoService todoService = ProxyHelper.createProxy(TodoService.class, vertx, "vertx.todoService");
//        bind(TodoService.class).toInstance(todoService);
        TodoService todoService = new TodoServiceImp();
        bind(TodoService.class).to(TodoServiceImp.class);
    }
}
