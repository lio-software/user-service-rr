FROM maven:3.8.5-openjdk-17
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean install -X
ARG SERVER_PORT=3002
EXPOSE ${SERVER_PORT}
CMD ["java", "-jar", "target/userservice-0.0.1-SNAPSHOT.jar", "--server.port=${SERVER_PORT}"]
