FROM openjdk:8-jre-slim

ENV DEB_VERSION=stretch

# Install gcloud utils
RUN echo "deb http://packages.cloud.google.com/apt cloud-sdk-$DEB_VERSION main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list
RUN apt-get update && apt-get install curl -y
# RUN curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
# RUN apt-get install google-cloud-sdk -y --allow-unauthenticated

RUN mkdir /deploy
ADD build/libs/*.jar /deploy
ADD docker/ /deploy

EXPOSE 8080

CMD ["sh", "/deploy/run.sh"]
