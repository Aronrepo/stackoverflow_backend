#### Stage 2: Build the application
FROM maven:3.8.7-openjdk-18-slim AS backend-build

#### add pom.xml and source code
ADD ./pom.xml pom.xml
ADD ./src src/

####package jar
RUN mvn clean package
#### List files in the target directory
RUN ls -l ./target
####Second stage: runtime environment
FROM eclipse-temurin:17.0.8_7-jre

####copy jar from the first stage
COPY  --from=backend-build ./target/stackoverflow-tw-0.0.1-SNAPSHOT.jar /app/

EXPOSE 8080

CMD ["java","-jar","/app/stackoverflow-tw-0.0.1-SNAPSHOT.jar"]