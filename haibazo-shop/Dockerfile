# Sử dụng image Maven để build và run ứng dụng Spring Boot
FROM maven:3.8.4-openjdk-17-slim AS build

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép file pom.xml để tải dependencies trước
COPY pom.xml .

# Cài đặt các dependencies cho dự án trước khi sao chép toàn bộ mã nguồn
RUN mvn dependency:go-offline

# Sao chép toàn bộ mã nguồn vào container
COPY . .

# Build ứng dụng mà không chạy các bài test
RUN mvn clean package -DskipTests

# Tạo image runtime từ OpenJDK
FROM openjdk:17-jdk-slim

# Thiết lập thư mục làm việc trong container cho runtime
WORKDIR /app

# Sao chép file JAR từ giai đoạn build vào container
COPY --from=build /app/target/*.jar app.jar

# Expose cổng 8080 cho ứng dụng
EXPOSE 8080

# Lệnh để chạy ứng dụng
CMD ["java", "-jar", "app.jar"]
