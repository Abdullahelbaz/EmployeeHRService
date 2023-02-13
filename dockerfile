FROM openjdk:8-jdk-alpine
copy ./target/*.jar app.jar
copy wait-for.sh wait-for.sh
RUN chmod +x wait-for.sh
EXPOSE 8093
CMD ["java","-jar","app.jar"]