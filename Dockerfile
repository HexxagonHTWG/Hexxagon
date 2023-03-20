# https://github.com/sbt/docker-sbt
FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.8.2_3.2.2
WORKDIR /scala
COPY . .
CMD sbt run
