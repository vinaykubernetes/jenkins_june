FROM openjdk:18.0.2.1-jdk
ARG JAR_DEST
ENV JAR_DEST ${JAR_DEST}
ARG JAR_SOURCE
ENV JAR_SOURCE ${JAR_SOURCE}
RUN mkdir /opt/i27
WORKDIR /opt/i27
COPY ["${JAR_SOURCE}", "/opt/i27/127cart-eureka.jar"]
RUN chmod 777 /opt/i27
EXPOSE 8761
USER root
CMD ["java", "-jar", "/opt/i27/127cart-eureka.jar"]