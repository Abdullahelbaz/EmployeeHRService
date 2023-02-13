 ******************* FOR SENARIOS AND Documentation ***************
 JSON file > Contracts.txt
 Documentaion > DOC.pdf 
 
******************Environment***************************
Technologies and TOOLS

1- Spring BOOT
2- Spring validation
3- JPA
4- RESTFULL API
5- Swagger
6- MYSQL
7- Impeded Server (Tomcat)
8- MAVEN
9- Java 8
10- Maven build tool
11- STS / Eclips
12 - Spring ExceptionHandler  (ControllerAdvice)
13- Camunda


****************DB discription*********************************

employee_tranasction_type type of services 
employee_transaction_history history for saving any transactions (or events)  which succeeded only 

********************Before Run and Build***********************

1- application.properties file contains some constant values, 
   * server port : 8052
   * DB port : 3306
   * DB url = jdbc:mysql://localhost:3306/employee
   and some other configration
   
   
******************* For Run and Build the project ***************
--   DB
1- create MySQL database by below script and ddl
   
2- run  dml 

upper steps in file : DB ddl && dml  .txt

-- for build the package 
1- bound and add JDK 8 in build path
2- update maven then clean the package then install maven
3- build mvn by 'clean package'
4- the package will build and the jar in target directory
5- the package has impeded tomcat server 

6- run the jar by below command
     nohup java -jar Employee-Service-1.0.jar     or use any build tool like STS
OR
- docker file attached in the package : dokerfile

     
7-you can use swagger by below link and use the JSON requests
	JSON request file name : Requests JSON.txt
   	Swagger Link  : http://localhost:8052/swagger-ui.html#/
   	
   	

 

