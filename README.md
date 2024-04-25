```shell
./gradlew clean build 
```

```shell
java  \
-javaagent:/Users/chenjie/Downloads/blog-img/demo/opentelemetry-javaagent.jar \
-Dotel.javaagent.extensions=context-1.0-SNAPSHOT.jar \
-Dotel.traces.exporter=otlp \
-Dotel.propagators=tracecontext,baggage,demo \
-Dotel.exporter.otlp.endpoint=http://127.0.0.1:5317 \
      -jar target/demo-0.0.1-SNAPSHOT.jar --server.port=8181 --grpc.server.port=8282
```

demo-0.0.1-SNAPSHOT.jar is any springboot application.