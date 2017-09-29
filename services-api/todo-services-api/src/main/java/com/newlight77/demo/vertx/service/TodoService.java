package com.newlight77.demo.vertx.service;

import io.vertx.core.json.JsonObject;

import java.util.Collection;

public interface TodoService {

    JsonObject create(JsonObject object);

    JsonObject update(String id, JsonObject object);

    void delete(String id);

    JsonObject find(String id);

    Collection<JsonObject> find();

}
