package com.newlight77.demo.vertx;

import com.newlight77.demo.vertx.factory.VertxFactory;

import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;


public class Deployer {

    private final static Logger LOGGER = LoggerFactory.getLogger(Deployer.class);

    private Consumer<String> logSuccess = message -> LOGGER.info("Verticle Deployed {}", message);

    private Consumer<Throwable> logError = throwable -> LOGGER.error("Verticle Could not be Deployed {}", throwable);

    private static Deployer singleton;

    private Deployer() {}

    public static Deployer singleton() {
        if (singleton == null) {
            singleton = new Deployer();
        }
        return singleton;
    }

    public void deploy() {


        JsonObject appConfig = VertxConfig.singleton().appConfig();
        LOGGER.info("deploy appConfig : {}", appConfig);
        VertxOptions vertxOptions = new VertxOptions(appConfig.getJsonObject("vertxOptions"));
        JsonObject verticlesConfig = VertxConfig.singleton().verticlesConfig().getJsonObject("verticles");

        new VertxFactory()
            .create(vertxOptions)
            .flatMapObservable(vertx -> deployVerticles(vertx, verticlesConfig))
            .subscribe(logSuccess, logError);
    }

    private Observable<String> deployVerticles(Vertx vertx, JsonObject verticlesConfig) {
        return Observable.fromIterable(verticlesConfig)
            .flatMap(verticleConf -> deployVerticle(vertx, verticleConf));
    }

    private Observable<String> deployVerticle(Vertx vertx, Map.Entry<String, Object> verticleConf) {

        JsonObject deploymentConf = ((JsonObject) verticleConf.getValue()).getJsonObject("deploymentOptions");
        DeploymentOptions deploymentOptions = new DeploymentOptions(deploymentConf);

        return Observable.fromPublisher(observer -> {
            String className = verticleConf.getKey();
            vertx.deployVerticle(className, deploymentOptions, result -> handleCompletion(observer, result));
        });
    }

    private void handleCompletion(Subscriber<? super String> observer, AsyncResult<String> result) {
        if (result.succeeded()) {
            observer.onNext(result.result());
        } else {
            observer.onError(result.cause());
        }
        observer.onComplete();
    }
}
