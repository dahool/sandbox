FROM eclipse-temurin:11-jre-jammy

COPY ./target/bfe-message-broker.jar /appvol/application/bfe-message-broker/bfe-message-broker.jar
COPY ./scripts/* /appvol/application/bfe-message-broker

EXPOSE 8080
ENTRYPOINT ["/appvol/application/bfe-message-broker/startup.sh"]
