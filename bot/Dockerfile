FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY target/*.jar bot.jar
ENTRYPOINT ["java", "-jar", "/bot.jar"]
