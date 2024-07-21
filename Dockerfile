FROM openjdk:17-jdk as build

ARG MAVEN_VERSION=3.9.7
RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

WORKDIR /app
COPY . .

RUN mvn -f pom.xml clean package

FROM openjdk:17-jre-slim
COPY --from=build /app/target/*.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
