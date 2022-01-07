# Challenge

## :computer: How to execute

- Run from IDE: 

The Spring Boot application can be run from an IDE. Import the project and run as a Java application.

- Run as a packaged application: 

Run the "mvn clean package" command from the root directory. A JAr file is generated in the target folder. The application can be run using the "java -jar" command as in the following:

`java -jar target/paymentprocessor-0.0.1-SNAPSHOT.jar`

- Run using the Maven Plugin:

Call the following Maven command to run the application:

`mvn spring-boot:run`



## :memo: Notes

A Spring Boot project is generated by using the Spring Initializr. The project metadata is specified and the required dependencies are added. The downloaded Spring Boot project is imported into Intellij IDEA IDE.

The "application.properties" file is used to specify the application properties. The parameters for PostgreSQL such as username, password, and data source URL are specified in this file. The URIs for payment validation and error log services are indicated in this file. Alternatively, the "application.yml" file format can be used for the same purpose.

The data models are defined under the "entitiy" package. Three models are defined for Account, Payment, and Error. The Account and Payment entities represent the tables in the PostgreSQL database. Spring Data JPA is used to store and retrieve data to/from the database.

The repositories and services of Account and Payment models are in the "repository" package. The JpaRepository is used to manage data.

The configuration for the Kafka consumer is provided under the "config" package. The Kafka consumer service is modeled in the "service" package. The Kafka consumer receives the messages sent from the producer provided in the "delivery" directory. First, the producer in the "delivery" directory is started using the command "docker-compose up". Then, payment messages are sent by the producer. The payment messages classified under two topics (online and offline) are read by the Kafka consumer service. The figure below shows the received payment messages by the consumer:

![after_starting_app](/home/cembila/Desktop/figures/after_starting_app.png)

The offline payments are all valid so that they are directly saved into the PostgreSQL database. The online payments are checked whether they are valid or not by calling the REST endpoint `http://localhost:9000/payment`. If 200 Ok response is returned, it indicates that payment is valid so it is saved in the database. If the received response is not 200 Ok, then it indicates that the corresponding online payment is invalid. In this case an error log is sent by calling the REST end point `http://localhost:9000/log. `Spring's RestTemplate is used to consume the REST services.

The @DataJpaTest is used to provide unit tests for the repositories. The @EmbeddedKafka is used to test the Kafka integration. Mockito framework is used to test the RestTemplate.   



## :pushpin: Things to improve

The code can be reviewed in detail by considering the Java naming conventions. For instance, in the entity models (Account, Payment, etc.) the variable names include underscore, which does not align with the Java naming conventions. Ideally, the variable names start with a lower case letter and the internal words start with a capital letter. Instead of using "last_payment_date", it can be written as "lastPaymentDate". The id variable names representing the primary keys can be represented by only "id". In order to keep the variable names same as the table column names, the Java naming conventions are not considered in the entity models for this task. It is considered in the remaining part of the code. Especially in the production-level development, Java conventions need to be considered. 