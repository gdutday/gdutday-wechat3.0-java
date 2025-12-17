# Deprecated: 以上使用的是 HotSpot 运行时，已弃用
#FROM registry.gdut.edu.cn/docker/library/eclipse-temurin:17-jre
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
#ENTRYPOINT ["java","-jar","/app.jar","--spring.profiles.active=prod","-XX:+UseZGC","-XX:ZCollectionInterval=120", "-XX:ZAllocationSpikeTolerance=4","-XX:-ZProactive",  "-XX:+HeapDumpOnOutOfMemoryError","-XX:HeapDumpPath=./errorDump.hprof"]

# 使用新的 OpenJ9 运行时
FROM docker.m.ixdev.cn/library/ibm-semeru-runtimes:open-17.0.17_9-jre-noble
ARG JAR_FILE=target/*.jar
ARG IMAGE_CREATED=1970-01-01T00:00:00Z
ARG IMAGE_VERSION=0.0.0
ARG IMAGE_REVISION=1
COPY ${JAR_FILE} app.jar

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 仅支持 OpenJ9 的 JVM 参数（JVM 参数必须在 -jar 之前）
ENTRYPOINT ["java", "-Xgcpolicy:gencon", "-Xquickstart", "-Xdump:heap:events=throw,filter=java/lang/OutOfMemoryError,file=/tmp/errorDump.hprof", "-jar", "/app.jar", "--spring.profiles.active=prod"]

# 添加标签
LABEL org.opencontainers.image.created="${IMAGE_CREATED}" \
  org.opencontainers.image.authors="gregPerlinLi,Ymri,shan-liangguang,Xb2555" \
  org.opencontainers.image.url="https://github.com/gdutday/gdutday-wechat3.0-java" \
  org.opencontainers.image.documentation="https://github.com/gdutday/gdutday-wechat3.0-java/blob/main/readme.md" \
  org.opencontainers.image.source="https://github.com/gdutday/gdutday-wechat3.0-java" \
  org.opencontainers.image.version="${IMAGE_VERSION}" \
  org.opencontainers.image.revision="${IMAGE_REVISION}" \
  org.opencontainers.image.vendor="GDUTDays" \
  org.opencontainers.image.licenses="GPL-3.0" \
  org.opencontainers.image.title="GDUTDays 4 后端服务" \
  org.opencontainers.image.description="GDUTDays/日在广工Plus，本小程序为gdutday小程序的3.0后端，为各位同学提供绩点查询，课程查询等功能。"
