<!-- Name: Bradley Vanderzalm
	 Course: CNT 4714 - Fall 2021 - Project Four
	 Assignment title: A Three-Tier Distributed Web-Based Application
	 Date: November 30, 2021
-->

<!-- File adapted from welcomesession.jsp on webcourses (Module 5) -->

<!DOCTYPE html>

<html lang="en">
	<!-- head section of document -->
	<head>
		<title>JSP part of Three-Tier Web based application reading SQL Commands</title>
		<meta charset="utf-8">
		<style type="text/css">
			<!--
			body { background-color: black; color:white; font-family: verdana, arial, sans-serif; font-size: 1.1em;  }
			h1 { color:yellow; font-size: 1.3em; }
			h2 { color:lime; font-size: 1.2em; }
			form textarea {background-color: blue; color: white;}
			form input {background-color: yellow; color: red; font-weight: bold;}
			form button {background-color: yellow; color: black; font-weight: bold;}
			/*table {margin-right: auto; margin-left: auto; color: black;}*/
			table {color: black;}
			/* Table Headers */
			table th {background-color: red;}
			/* Table data */
			table td {background-color: lightgray;}
			/*input[type="submit"] {background-color:  yellow; font-weight: bold;}*/
			/*input[type="button"] {background-color:  yellow; color: red; font-weight: bold;}*/
			span {color: red;}
			/*#sqlTextArea { color: white ;background: blue; width: 600px; height: 230px; }*/
			#servlet {color: purple;}
			#jsp {color: cyan;}
			-->
		</style>
	</head>
	<!-- body section of document -->
	<body>
		<h1>Welcome to the Fall 2021 Project 4 Enterprise Database System - Bradley Vanderzalm</h1>
		<h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>
		<hr>
		<p>You are connected to the Project 4 Enterprise System database as a root user. Please enter any valid SQL query or update command in the box below.</p>
		<form action="ServerApp" method="post">
			<textarea id="sqlTextArea" name="sqlTextArea" rows="20" cols="60"></textarea>
			<input type ="submit" name= "submit" value= "Execute Command" />
			<!-- <button type="submit">Execute Command</button> -->
			<!-- <input type="submit" name="submit" value="Execute Command"> -->
			<!-- <input type="submit" value="Reset Form"> -->
			<!-- <input type="submit" value="Clear Results"> -->
		</form>
		<p>All execution results will appear below the line.</p>
		<hr>
		<p>Database Results:</p>
		<br><br><br>
	</body>
</html>

