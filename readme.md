## Vert.x demo project

This project show how to build a micro-service with vert.x providing a
simple todo resource.


### Build
The build.gradle uses the Gradle shadowJar plugin to assemble the
application and all it’s dependencies into a single "fat" jar.

To build the "fat jar"

```
./gradlew shadowJar


```


### Run

To run the application

```
./gradlew run


```

or launch

```
java -jar build/libs/vertx-demo-1.0.0-SNAPSHOT-fat.jar

```


### Test it

Go to the http://localhost:8082/todos


### More verticles

To embbed more verticles :

* Implement a class extends AbstractVerticle (see TodoVerticle)
* add verticle config in verticles-config.json as below


```
{
{
  "verticles" : [
    {
      "class" : "com.newlight77.demo.vertx.verticle.TodoVerticle",
      "deploymentOptions": {
        "config": {
        },
        "instances": 1,
        "ha": true,
        "worker": false,
        "multiThreaded": false
      }
    }
  ]
}
}
```