FROM openjdk:8
COPY ./target/spring-leaf-backend-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp
COPY ./spring-leaf.cfg /tmp
WORKDIR /tmp
ENTRYPOINT ["java","-jar","spring-leaf-backend-1.0-SNAPSHOT-jar-with-dependencies.jar"]