package com.newlight77.demo.vertx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public class GreetingServiceImp implements GreetingService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GreetingServiceImp.class);

    @Override
    public void process(JsonObject document, Handler<AsyncResult<JsonObject>> resultHandler) {
        LOGGER.info("Processing document={}", document);

        JsonObject result = document.copy();
        if (!document.containsKey("data")) {
            resultHandler.handle(Future.failedFuture("No name in the document"));
        } else {
            result.put("approved", true);
            resultHandler.handle(Future.succeededFuture(result));
        }
    }
}
