package com.newlight77.demo.vertx;

import com.newlight77.demo.vertx.factory.VertxFactory;

import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import io.netty.util.internal.StringUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


public class Deployer {

    private final static Logger LOGGER = LoggerFactory.getLogger(Deployer.class);

    private Consumer<String> logSuccess = message -> LOGGER.info("Verticle Deployed {}", message);

    private Consumer<Throwable> logError = throwable -> LOGGER.error("Verticle Could not be Deployed {}", throwable);

    private static Deployer singleton;

    private Deployer() {
    }

    public static Deployer singleton() {
        if (singleton == null) {
            singleton = new Deployer();
        }
        return singleton;
    }

    public void deploy(VertxOptions vertxOptions, JsonArray verticlesConfig) {

        LOGGER.info("deploying verticles");
        new VertxFactory()
            .create(vertxOptions)
            .flatMapObservable(vertx -> deployVerticles(vertx, verticlesConfig))
            .subscribe(logSuccess, logError);
    }

    private Observable<String> deployVerticles(Vertx vertx, JsonArray verticlesConfig) {
        return Observable
            .fromIterable(verticlesConfig)
            .flatMap(verticleConf -> deployVerticle(vertx, (JsonObject) verticleConf));
    }

    private Observable<String> deployVerticle(Vertx vertx, JsonObject verticleConf) {

        JsonObject deploymentConf = verticleConf.getJsonObject("deploymentOptions");
        DeploymentOptions deploymentOptions = new DeploymentOptions(deploymentConf);

        return Observable.fromPublisher(observer -> {
            String className = verticleConf.getString("class");
            LOGGER.info("deploying verticle=[{}]", classShortName(className));

            vertx.deployVerticle(className, deploymentOptions, result -> handleCompletion(observer, result, className));
        });
    }

    private void handleCompletion(Subscriber<? super String> observer, AsyncResult<String> result, String className) {
        if (result.succeeded()) {
            observer.onNext("verticle=" + classShortName(className) + " : " + result.result());
        } else {
            observer.onError(result.cause());
        }
        observer.onComplete();
    }

    private String classShortName(String className) {
        return StringUtil.substringAfter(className, '.');
    }
}
