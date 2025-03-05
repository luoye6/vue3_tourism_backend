# Docker 镜像构建
# @author 程序员小白条
# @from <a href="https://luoye6.github.io/"> 个人博客
FROM maven:3.8.1-jdk-8-slim as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Run the web service on container startup.
CMD ["java","-jar","/app/target/springboot-init-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]