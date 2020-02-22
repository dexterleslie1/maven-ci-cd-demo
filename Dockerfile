FROM openjdk:8-jdk-alpine

# Setup timezone Asia/Shanghai
RUN apk add tzdata
RUN rm -f /etc/localtime && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# Copy jar
COPY target/maven-ci-cd-demo.jar /usr/local/maven-ci-cd-demo/
COPY entrypoint.sh /usr/local/maven-ci-cd-demo/

CMD ["java","-jar","/usr/local/maven-ci-cd-demo/maven-ci-cd-demo.jar"]
ENTRYPOINT ["sh","/usr/local/maven-ci-cd-demo/entrypoint.sh"]