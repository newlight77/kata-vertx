package com.newlight77.demo.vertx.factory;

import com.newlight77.demo.vertx.Deployer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class VertxFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(Deployer.class);

    private static final long WAIT_TIME = 30000;
    private CountDownLatch latch;

    public Single<Vertx> create(VertxOptions vertxOptions) {
        this.latch = new CountDownLatch(1);
        return Single.just(Vertx.vertx(vertxOptions))
            .map(vertx -> {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> safeShutdown(vertx)));
                return vertx;
            });
    }

    public void safeShutdown(Vertx vertx) {
        Observable.fromIterable(vertx.deploymentIDs())
            .flatMapCompletable(id -> undeploy(vertx, id))
            .doOnComplete(latch::countDown)
            .subscribe();

        try {
            latch.await(WAIT_TIME, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.warn("safeShutdown interrupted : {}", e);
        }
    }

    private Completable undeploy(Vertx vertx, String id) {
        return Completable.fromSingle(single -> vertx.undeploy(id, single::onSuccess));
    }
}
