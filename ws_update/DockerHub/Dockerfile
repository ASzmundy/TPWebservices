# syntax=docker/dockerfile:1

FROM openjdk:11-jre

LABEL author="SZMUNDY Alexis"
LABEL email="alexis.szmundy@etu.uphf.fr"

RUN useradd -u 1000 insa
RUN mkdir -p /opt/app && chown -R insa:insa /opt/app
RUN mkdir -p /opt/configs && chown -R insa:insa /opt/configs

#partage volume configs avec extérieur
VOLUME /opt/configs

#Répertoire de l'application
WORKDIR /opt/app

#copier le fat jar
COPY ws_services_template-0.0.1.jar /opt/app/
COPY ws_client.properties /opt/configs/

#expose le pot 6666  port le service DEMO et SETUP
EXPOSE 8880

#connecter le user insa
USER insa

#gerer l'entrypoint
ENTRYPOINT [ "java","-jar", "/opt/app/ws_services_template-0.0.1.jar","--spring.config.location=file:///opt/configs/ws-template.properties" ]
