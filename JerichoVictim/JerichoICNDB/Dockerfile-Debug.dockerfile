FROM gradle:4.10.2-jdk-alpine
EXPOSE 8080
EXPOSE 5005
ENTRYPOINT ["gradle", "run", "--no-daemon"]
WORKDIR jericho
ADD . ./
USER root
RUN chown -R gradle:gradle /home/gradle/jericho
USER gradle
