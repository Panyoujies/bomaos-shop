FROM maven:3.8.1-jdk-8-slim as build

WORKDIR /workdir
COPY ./pom.xml ./pom.xml
COPY src ./src/

RUN mvn package -DskipTests

FROM openjdk:8u212-jre as runtime

COPY --from=build /workdir/target/bomaos-shop-*.jar /app/app.jar


# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

ENTRYPOINT ["java", "-server", "-Xms1024M", "-Xmx1024M", "-Djava.security.egd=file:/dev/./urandom", "-Dfile.encoding=UTF-8", "-XX:+HeapDumpOnOutOfMemoryError", "-jar", "/app/app.jar" ]


