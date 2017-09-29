package com.newlight77.demo.vertx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

import java.util.*;

public class TodoServiceImp implements TodoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TodoServiceImp.class);

    private Map<String, JsonObject> todos = new HashMap<>();

    public TodoServiceImp() {
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
        create(new JsonObject().put("assigned", "Kong").put("title", "Learn vert.x").put("done", true));
        create(new JsonObject().put("assigned", "Mr B").put("title", "Learn Angular").put("done", false));
        create(new JsonObject().put("assigned", "Mr S").put("title", "Learn Kafka").put("done", false));
    }
}
