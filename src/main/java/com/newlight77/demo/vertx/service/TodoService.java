package com.newlight77.demo.vertx.service;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.vertx.core.json.JsonObject;

public class TodoService
{
    private Map<String, JsonObject> todos = new HashMap<>();

    public TodoService() {
        initData();
    }

    public JsonObject create(JsonObject object) {
        object.put("id", UUID.randomUUID().toString());
        todos.put(object.getString("id"), object);
        return object;
    }

    public JsonObject update(String id, JsonObject object) {
        Objects.requireNonNull(id);
        object.put("id", id);
        todos.put(id, object);
        return object;
    }

    public void delete(String id) {
        Objects.requireNonNull(id);
        todos.remove(id);
    }

    public JsonObject find(String id) {
        Objects.requireNonNull(id);
        return todos.get(id);
    }

    public Collection<JsonObject> find() {
        return todos.values();
    }


    public void initData() {
        create(new JsonObject().put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
        create(new JsonObject().put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
        create(new JsonObject().put("name", "Spatula").put("price", 1.00).put("weight", 80));
    }

}
