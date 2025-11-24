# Deprecated: 以上使用的是 HotSpot 运行时，已弃用
#FROM registry.gdut.edu.cn/docker/library/eclipse-temurin:17-jre
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
#ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod","-XX:+UseZGC","-XX:ZCollectionInterval=120", "-XX:ZAllocationSpikeTolerance=4","-XX:-ZProactive",  "-XX:+HeapDumpOnOutOfMemoryError","-XX:HeapDumpPath=./errorDump.hprof"]

# 使用新的 OpenJ9 运行时
FROM docker.m.ixdev.cn/library/ibm-semeru-runtimes:open-17.0.17_9-jre-noble
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 仅支持 OpenJ9 的 JVM 参数（JVM 参数必须在 -jar 之前）
ENTRYPOINT ["java", "-Xgcpolicy:gencon", "-Xquickstart", "-Xdump:heap:events=throw,filter=java/lang/OutOfMemoryError,file=/tmp/errorDump.hprof", "-jar", "/app.jar", "--spring.profiles.active=prod"]