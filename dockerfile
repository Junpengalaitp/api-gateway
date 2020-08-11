FROM amazoncorretto:11-al2-full
VOLUME /tmp
COPY target/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]