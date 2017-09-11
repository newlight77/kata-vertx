package com.newlight77.demo.vertx.factory;

import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.core.LifecycleEvent;

import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class ClusteredVertxFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClusteredVertxFactory.class);

    private HazelcastClusterManager clusterManager;

    public ClusteredVertxFactory() {
        try {
            Config conf = new FileSystemXmlConfig(System.getProperty("cluster-xml"));
            clusterManager = new HazelcastClusterManager(conf);
        } catch (FileNotFoundException e) {
            LOGGER.error("Error loading cluster-xml : {}", e);
            throw new RuntimeException("Error loading cluster-xml");
        }
    }

    public Single<Vertx> create(VertxOptions vertxOptions) {
        vertxOptions.setClusterManager(clusterManager);
        return Single.fromPublisher(publisher ->
            Vertx.clusteredVertx(vertxOptions, response -> handleResult(publisher, response)));
    }

    private void handleResult(Subscriber<? super Vertx> publisher, AsyncResult<Vertx> response) {
        if (response.succeeded()) {
            Vertx vertx = response.result();
            clusterManager.getHazelcastInstance()
                .getLifecycleService()
                .addLifecycleListener(state -> {
                if (state.getState() == LifecycleEvent.LifecycleState.SHUTTING_DOWN) {
                    safeShutdown(vertx);
                }
            });
            publisher.onNext(vertx);
        } else {
            publisher.onError(response.cause());
        }
        publisher.onComplete();
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
