FROM openjdk:17.0.2-slim-buster

ARG JAR_FILE=""
COPY ${JAR_FILE} /usr/local/lib/app-microservice.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /usr/local/lib/app-microservice.jar $ARGS