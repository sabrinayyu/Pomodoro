### *Requirements:*
JDK 13.0.2

Maven 3

MySQL 8.0
 
### Setting up database:

Create user and database. Grant all privileges on the newly created DB to the newly created user. Commands for this:

~~~~
mysql -uroot

mysql> create database ptt;

mysql> create user 'springuser'@'%' identified by 'ThePassword';

mysql> grant all on ptt.* to 'springuser'@'%'; 
~~~~


### How to compile and run the service:
Go to 6301Spring20Backend1/PTTBackend1 and run the following command (depending on your OS): 

~~~~
mvn spring-boot:run

~~~~

OR

~~~~
mvnw spring-boot:run

~~~~


### How to run the test sets:
Go to 6301Spring20Backend1/PTTBackend1 and run the following command (for Web1 test set): 

~~~~

gradle BackendTestsWeb1

~~~~



