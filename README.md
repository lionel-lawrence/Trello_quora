
Group Project at Upgrad - Course 5

Project Title    
Trello_quora


This project is for, developing REST API endpoints of various functionalities required for a website (similar to Quora) from scratch.This project followed a definite structure in order to help the co-developers and reviewers for easy understanding. 
Also,the better project structure of this project makes code modular and it becomes easier to implement any new features on the existing application. The main module is divided into three sub-modules —  quora-api, quora-db, and quora-service.

Getting Started
Instructions:

Need to run these JSON files on Swagger UI and observe the details related to each endpoint. Observe the HTTP methods to make a call to that endpoint, request URL pattern, input and output parameters of the endpoint, and the status codes in each endpoint to handle the exceptions related to that endpoint.
The main directory of the project used "mvn clean install -DskipTests". In order to activate the profile setup, move to quora-db folder using "cd quora-db" command in the terminal and then run "mvn clean install -Psetup" command to activate the profile setup.
In this project, always refer to the resource's uuid whenever id of the resource is mentioned.

Prerequisites
Install

JDK      
PostgreSQL    
IntelliJ    
SignUp github     

Running the tests   
Junit is used for all test cases,"mvn clean install -Psetup" in the quora-db folder of the project. 

Deployment     
The Rest API is deployed using spring,for MVC architecture and Apache Tomcat 8(8.0.53)
https://tomcat.apache.org/tomcat-8.0-doc/deployer-howto.html#:~:text=The%20Tomcat%20Manager%20is%20a,for%20Apache%20Ant%20build%20tool

Built With      
Swagger UI  - The web framework used   
Maven       - Dependency Management   
PostgreSQL  - Data base   
JPA         - Used for implementation        


Versioning   
We used git and git hub for versioning. For the versions available see the repositories.

List of Contributors :

Lionel Lawrence      - Group Facilitator    
Sawan Verma          - Group Member   
Bindu Sowjanya S.R.  - Group Member   
Deavyansh.G          - Group Member

License
This project is licensed under the Upgrad License .
