FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY target/*.jar scrapper.jar
ENTRYPOINT ["java", "-jar", "/scrapper.jar"]
