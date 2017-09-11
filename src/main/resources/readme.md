## deploymentOptions

* config -> Verticle spesific config goes under deploymentOptions/config this will be reached as JsonObject by Verticle when config() called inside the Verticle
* instances -> spesify how many instance will be deployed
* ha -> set true if verticle intented to be High Available
* worker -> If this verticle is doing long running tasks then this should be true, so that we let WorkerEventPool to handle these tasks
* multiThreaded -> // If worker is true then Verticle may be called from different threads. Don't set this true if you don't know what you are doing in ideal situation each Verticle should be called by only one thread

Please refer to documentation for other options which are extraClasspath, isolatedClasses, isolationGroup, maxWorkerExecuteTime multiThreaded, worker, workerPoolName, workerPoolSize


## vertxOptions

* clustered -> If this field is true than vertx will run clustered so cluster.xml must provide
* clusterHost -> cluster host name, (OPTIONAL you can pass things like this"${nodeip}")
* quorumSize -> Untill quorum size satisfied verticle is not gonna be deployed
* haEnabled -> Set true if Vert.x should be High Available
* definition -> If vertx is ha than this can be grouped under this key
* eventLoopPoolSize -> Main Event Loop pool size, should be equal to CPU core size
* workerPoolSize -> If Vert.x has a lot of long running tasks Then Worker Event Pool should handle those And this can be greater than CPU core size

Other options are addressResolverOptions, blockedThreadCheckInterval, clusterPingInterval, clusterPort clusterPublicHost, clusterPublicPort, eventBusOptions, internalBlockingPoolSize, maxEventLoopExecuteTime, maxWorkerExecuteTime, metricsOptions, warningExceptionTime
