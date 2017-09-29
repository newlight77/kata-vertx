package com.newlight77.demo.vertx.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface GreetingService {

    void process(JsonObject document, Handler<AsyncResult<JsonObject>> resultHandler);
}
