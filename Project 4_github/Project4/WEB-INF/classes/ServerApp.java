/*  Name: Bradley Vanderzalm
    Course: CNT 4714 - Fall 2021 - Project Four
    Assignment title: A Three-Tier Distributed Web-Based Application
    Date: November 21, 2021
 */

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class ServerApp extends HttpServlet
{
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException, IOException
    {


        // Indicate the MIME for user (Multipurpose Internet Mail Extension) where
        // it's telling the browser we are using html elements.
        // (Adapted from first-example WelcomeServlet.java)
        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();

        Connection connection;
        Statement statement = null;
        String sqlCommand;
        ResultSet resultSet;
        ResultSetMetaData metaData;

        // Establish connection to database
        try
        {
            String defaultDriver = "com.mysql.cj.jdbc.Driver";
            String defaultDatabaseURL = "jdbc:mysql://localhost:3306/project4?useTimezone=true&serverTimezone=UTC";
            String username = "root", password = "Bvucf7530";
            Class.forName(defaultDriver);

            // Attempt to connect to database
            connection = DriverManager.getConnection(defaultDatabaseURL, username, password);
            statement = connection.createStatement();
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println(ex.getMessage() + " Driver not found.");
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage() +
                    " Error establishing connection to database and executing commands.");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        // Get text here user wrote now so it doesn't "disappear" after user clicks execute command button.
        sqlCommand = request.getParameter("sqlTextArea");

        // This is copying what I wrote in my index.jsp file and displaying it with PrintWriter.
        // However it stops right before displaying the SQL Table.
        printHTMLElementsFromIndexJSP(out, sqlCommand);

        // Attempt to Execute SQL Command
        try
        {
            if (sqlCommand.startsWith("select"))
            {
                // Start the first row of the database: the headers.
                // Note: <tr> stands for Table Row Element
                out.println( "<tr>" );
                // Execute the command and retrieve all data on it.
                resultSet = statement.executeQuery(sqlCommand);
                metaData = resultSet.getMetaData();

                // Finds the number of cols and rows, this number will help us
                // fill in the data.
                int numColumns = metaData.getColumnCount();
//                resultSet.last();   // Move to last row
//                int numRows = resultSet.getRow();
//                resultSet.beforeFirst(); // Set cursor back to where it was

                // Retrieve database header names
                for (int i = 0; i < numColumns; i++)
                {
                    // Note: <th> stands for table header. This will make the
                    // headers stand out from the rest of the data below it.
                    out.println( "<th>" + metaData.getColumnName(i+1) + "</th>");
                }
                // End of first row (headers).
                out.println( "</tr>" );

                // Essentially repeat the process we just went through except for the rest of the
                // rows in the database table.
                while (resultSet.next())
                {
                    out.println( "<tr>" );
                    for (int i = 0; i < numColumns; i++)
                    {
                        out.println( "<td>" + resultSet.getString(i+1) + "</td>" );
                    }
                    out.println( "</tr>" );
                }
//                for (int i = 0; i < numRows; i++)
//                {
//                    // Start of table row element
//                    out.println( "<tr>" );
//                    for (int j = 0; j < numColumns; j++)
//                    {
//                        out.println( "<td>" );
//                        out.println(resultSet.getString(i+1));
//                        out.println( "</td>" );
//                    }
//                    out.println( "</tr>" );
//                }
            }
        }
        catch (SQLException ex)
        {
            out.println( "<th><strong>Error executing the SQL statement:</strong> " + ex.getMessage() + "</th>" );
        }

        finally
        {
            out.println( "</table>" );
//            out.println( "</main>" );
            out.println( "</body>" );
            out.println( "</html>" );
            out.close(); // close stream to complete page
        }
    }

    // This is copying what I wrote in my index.jsp file and displaying it with PrintWriter.
    // However it stops right before displaying the SQL Table.
    private void printHTMLElementsFromIndexJSP(PrintWriter out, String sqlCommand)
    {
        out.println( "<!DOCTYPE html>" );
        out.println( "<html lang=\"en\">" );
        out.println( "<head>" );
        out.println( "<title>JSP part of Three-Tier Web based application reading SQL Commands</title>" );
        out.println( "<meta charset=\"utf-8\">" );
        out.println( "<style type=\"text/css\">" );
        out.println( "<!-- body{background-color:black; color:white; font-family: verdana, arial, sans-serif; font-size: 1.1em;}" );
        out.println( " h1{color:yellow; font-size:1.3em;} " );
        out.println( " h2{color:lime; font-size; 1.2em;}" );
        out.println( " form textarea {background-color: blue; color: white;}" );
        out.println( " form input {background-color: yellow; color: red; font-weight: bold;}" );
        out.println( " form button {background-color: yellow; color: black; font-weight: bold;}" );
        out.println( " table {color: black;}" );
//        out.println( " table {margin-right: auto; margin-left: auto; color: black;}" );
        out.println( " table th {background-color: red;}" );
        out.println( " table td {background-color: lightgray;}" );
        out.println( " span{color:red;}" );
        out.println( " #servlet {color:purple;}" );
        out.println( " #jsp {color:cyan;}" );
        out.println( "-->" );
        out.println( "</style>" );
        out.println( "</head>" );
        out.println( "<body>" );
        out.println( "<h1>Welcome to the Fall 2021 Project 4 Enterprise Database System - Bradley Vanderzalm</h1>" );
        out.println( "<h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>" );
        out.println( "<hr>" );
        out.println( "<p>You are connected to the Project 4 Enterprise System database as a root user. Please enter any valid SQL query or update command in the box below.</p>" );
        out.println( "<form action=\"ServerApp\" method=\"post\">" );

        // Only difference between initial part of my index.jsp file. Includes the command user already typed.
        out.println( "<textarea id=\"sqlTextArea\" name=\"sqlTextArea\" rows=\"20\" cols=\"60\">" + sqlCommand + "</textarea>" );
        out.println( "<input type =\"submit\" name= \"submit\" value= \"Execute Command\" />" );

//        out.println( "<input type=\"submit\" name=\"submit\" value=\"Execute Command\">" );
//        out.println( "<input type=\"submit\" value=\"Reset Form\">" );
//        out.println( "<input type=\"submit\" value=\"Clear Results\">" );
        out.println( "</form>" );
        out.println( "<p>All execution results will appear below the line.</p>" );
        out.println( "<hr>" );
        out.println( "<p>Database Results:</p>" );
        out.println( "<br><br><br>" );
        out.println( "<table>" );
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException
    {
        // Same as beginning of doPost (Adapted from first-example WelcomeServlet.java)
        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();
    }

}
