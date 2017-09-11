package com.newlight77.demo.vertx.factory;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class SimpleVertxFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleVertxFactory.class);

    public Single<Vertx> create(VertxOptions vertxOptions) {
        return Single.just(Vertx.vertx(vertxOptions))
            .map(vertx -> {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> safeShutdown(vertx)));
                return vertx;
            });
    }

    private void safeShutdown(Vertx vertx) {
        Observable.fromIterable(vertx.deploymentIDs())
            .flatMapCompletable(id -> undeploy(vertx, id))
            .doOnComplete(() -> LOGGER.info("shutdonw"))
            .subscribe();
    }

    private Completable undeploy(Vertx vertx, String id) {
        return Completable.fromSingle(single -> vertx.undeploy(id, single::onSuccess));
    }
}
