FROM openjdk:17.0.2
RUN mkdir /bitespeed-sampleproject
WORKDIR /bitespeed-sampleproject
COPY . /bitespeed-sampleproject
RUN chmod +x /bitespeed-sampleproject/mvnw
RUN ./mvnw clean install -Dmaven.test.skip=true
EXPOSE 8080
CMD [ "./mvnw", "spring-boot:run" ]