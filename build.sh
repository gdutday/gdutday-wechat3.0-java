#!/bin/bash

# 检查是否传入了版本号参数
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <version>"
    exit 1
fi

# 获取传递进来的版本号参数
VERSION=$1

# 进入到包含 Dockerfile 的 Maven 项目目录
cd ./ || exit

# 构建 Docker 镜像，并将版本号作为标签
docker build -t reg.gdutelc.com/gdutday/gdutday:"$VERSION" .

# 检查构建是否成功
if [ $? -ne 0 ]; then
    echo "Failed to build the Docker image."
    exit 1
fi

# 给镜像加上 latest 标签
docker tag reg.gdutelc.com/gdutday/gdutday:"$VERSION" reg.gdutelc.com/gdutday/gdutday:latest

# 登录 Docker 注册表（请替换为实际的用户名、密码和注册表地址）
# echo "your-username" | docker login -u "your-username" --password-stdin registry.example.com

# 将镜像推送到 Docker 注册表
docker push reg.gdutelc.com/gdutday/gdutday:"$VERSION"
docker push reg.gdutelc.com/gdutday/gdutday:latest

# 如果推送成功，则输出提示信息
if [ $? -eq 0 ]; then
    echo "Successfully pushed Docker image with tag $VERSION"
else
    echo "Failed to push the Docker image."
fi

docker stop gdutday3

docker rm gdutday3

docker run -d  -p 1888:8080 --name gdutday3 --restart=always reg.gdutelc.com/gdutday/gdutday:latest

# 如果部署成功，则输出提示信息
if [ $? -eq 0 ]; then
    echo "Successfully deploy new container with tag $VERSION"
else
    echo "Failed to deploy the container."
fi