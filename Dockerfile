FROM ubuntu:14.04

RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en  
ENV LC_ALL en_US.UTF-8

RUN apt-get update && apt-get install -y --no-install-recommends \
	openjdk-7-jdk

RUN addgroup --system --gid=403 fablab && adduser --group --system fablab --uid=403

COPY app-server/ /home/fablab/app-server
COPY app-common/ /home/fablab/app-common

COPY entrypoint.sh /home/fablab/app-server/
RUN mkdir -p /home/fablab/app-server/src/dist/

WORKDIR /home/fablab/app-server
RUN chown -R fablab:fablab /home/fablab
RUN sudo -u fablab -- ./gradlew build

EXPOSE 80 8081

USER fablab
ENTRYPOINT ["./entrypoint.sh"]
CMD ["./gradlew", "run"]