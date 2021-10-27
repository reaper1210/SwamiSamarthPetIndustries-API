FROM openjdk:11-jdk-slim

WORKDIR /src
COPY . /src

RUN apt-get update
RUN apt-get install -y dos2unix
RUN dos2unix gradlew

RUN bash gradlew shadowJar

WORKDIR /run

EXPOSE 8080

CMD java -jar /build/libs/com.swamisamarthpet.sspi-api-0.0.1-all.jar