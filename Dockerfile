FROM openjdk:17
ADD target/demo-0.0.1-SNAPSHOT.jar app.jar
VOLUME /simple.app
EXPOSE 8095
ENTRYPOINT ["java", "-jar", "/app.jar"]