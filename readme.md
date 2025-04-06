# GdutDays3.0 后端代码重构

GDUTDays/日在广工Plus，本小程序为gdutday小程序的3.0后端，为各位同学提供绩点查询，课程查询等功能。非学校官方小程序。

前端地址: https://github.com/gdutday/gdutday-wechat-2.0

## Java Version
`17`
## SpringBoot Version
`3.1.4`
## How To Run
1. `mvn clean package -DskipTests=true`
2. `export GDUTDAYS_SECRET='changemechangeme'`
3. `java --jar target/gdutday-wechat3-java-0.0.1-SNAPSHOT.jar`


## 注意事项

为了学生的信息安全，通信密钥将通过环境变量`GDUTDAYS_SECRET`读取，如果您有意愿成为开发者并发起网络请求，请向开发团队申请权限



