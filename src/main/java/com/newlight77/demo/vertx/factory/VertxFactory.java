package com.newlight77.demo.vertx.factory;

import io.reactivex.Single;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public interface VertxFactory {

    static Single<Vertx> create(VertxOptions vertxOptions) {
        if (vertxOptions.isClustered()) {
            return new ClusteredVertxFactory().create(vertxOptions);
        } else {
            return new SimpleVertxFactory().create(vertxOptions);
        }
    }
}
