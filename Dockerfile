FROM openjdk:11-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN mkdir -p filestorage
EXPOSE 8080/tcp
ENTRYPOINT ["java","-jar","/app.jar"]