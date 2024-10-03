# Sử dụng image Maven để build ứng dụng Spring Boot
FROM maven:3.8.4-openjdk-17-slim AS build

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép file pom.xml từ thư mục haibazo-shop vào container
COPY ./haibazo-shop/pom.xml .

# Cài đặt các dependencies cần thiết cho dự án
RUN mvn dependency:go-offline

# Sao chép toàn bộ mã nguồn từ thư mục haibazo-shop vào container
COPY ./haibazo-shop/ .

# Build ứng dụng mà không chạy các bài test
RUN mvn clean package -DskipTests

# Tạo image runtime từ OpenJDK
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép file JAR từ giai đoạn build
COPY --from=build /app/target/*.jar app.jar

# Expose cổng 8080 cho ứng dụng
EXPOSE 8080

# Lệnh để chạy ứng dụng
CMD ["java", "-jar", "app.jar"]
