# CNT4714_Projects_vanderzalm
<div id="top"></div>

## About The Class

Enterprise Computing (CNT4714) will expose you to the world of heterogeneous enterprise computing architectures with an emphasis on networked, distributed applications using a variety of technologies. Including:

1. Multi-threadedapplications. Communication and synchronization amongst threads.
2. Networking issues for distributed systems.
3. Database systems backend connectivity. JDBC and other technologies.
4. Multi-tier distributed enterprise applications.
5. Apache HTTP Server, ApacheTomcatServer, Server administration issues.
6. Servlet technology.
7. JSP technology.

Languages include: Java, MySQL, JDBC, JSP, XML.

## Project Descriptions

### Project 1 - Event Driven Programming

Develop a Java program that creates a standalone GUI application that simulates an e-store which allows the user to add in stock items to a shopping cart and once all items are included, total all costs (including tax), produces an invoice, and appends a transaction log file.

### Project 2 - Multiple Threads In Java Using Locks – A Banking Simulator
Simulate the deposits and withdrawals made to a fictitious bank account. In this case the deposits and withdrawals will be made by synchronized threads. This application requires cooperation and communication amongst the various threads (cooperating synchronized threads). If a withdrawal thread attempts to withdraw an amount greater than the current balance in the account – then it must block itself and wait until a deposit has occurred before it can try again. This will require that the depositor threads signal all waiting withdrawal threads whenever a deposit is completed.

### Project 3 - Two-Tier Client-Server Application Development With MySQL and JDBC

Objectives: To develop a two-tier Java based client-server application interacting with a MySQL database utilizing JDBC for the connectivity. This project is designed to give you some experience using the various features of JDBC and its interaction with a MySQL DB Server environment.

Description: In this assignment you will develop a Java-based GUI front-end (client-side) application that will connect to your MySQL server via JDBC.

### Project 4 - Developing A Three-Tier Distributed Web-Based Application

To incorporate many of the techniques you’ve learned so far this semester into a distributed three-tier web-based application which uses servlets and JSP technology running on a Tomcat container/server to access and maintain a persistent MySQL database using JDBC.

In this assignment you will utilize a suppliers/parts/jobs/shipments database as the back-end database. Front-end access to this database by the client will occur through a single page displayed in the client’s web browser. The schema of the backend database consists of four tables with the following schemas for each table:


  suppliers (snum, sname, status, city) //information about suppliers

  parts (pnum, pname, color, weight, city) //information about parts

  jobs (jnum, jname, numworkers, city) //information about jobs

  shipments (snum, pnum, jnum, quantity) //suppliers ship parts to jobs in specific quantities
