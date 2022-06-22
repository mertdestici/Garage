FROM maven:3.6.3 AS maven
WORKDIR /usr/src/app
COPY . /usr/src/app
# Compile and package the application to an executable JAR
RUN mvn package 



FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=Garage-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY --from=maven /usr/src/app/target/${JAR_FILE} /opt/app/
#COPY target/Garage-0.0.1-SNAPSHOT.jar garage.jar
#EXPOSE 8080
#ENTRYPOINT exec java $JAVA_OPTS -jar garage.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
ENTRYPOINT ["java","-jar","Garage-0.0.1-SNAPSHOT.jar"]
#ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom -jar garage.jar
