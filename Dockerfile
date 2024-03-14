FROM registry.gdut.edu.cn/aliyun/library/openjdk:17-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod","-XX:+UseZGC","-XX:ZCollectionInterval=120", "-XX:ZAllocationSpikeTolerance=4","-XX:-ZProactive",  "-XX:+HeapDumpOnOutOfMemoryError","-XX:HeapDumpPath=./errorDump.hprof"]
