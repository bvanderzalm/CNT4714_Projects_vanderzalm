/*  Name: Bradley Vanderzalm
    Course: CNT 4714 - Fall 2021 - Project Four
    Assignment title: A Three-Tier Distributed Web-Based Application
    Date: December 2, 2021
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
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage() +
                    " Error establishing connection to database and executing commands.");
            ex.printStackTrace();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        // Get text here user wrote now so it doesn't "disappear" after user clicks execute command button.
        String sqlCommand = request.getParameter("sqlTextArea");

        // This is copying what I wrote in my index.jsp file and displaying it with PrintWriter.
        // However it stops right before displaying the SQL Table.
        boolean displayTableResults = true; // include the <table> to the printWriter.
        printHTMLElementsFromIndexJSP(out, sqlCommand, displayTableResults);

        // Attempt to Execute SQL Command
        try
        {
            ResultSet resultSet;
            ResultSetMetaData metaData;

            // If select cmd, execute it and display table.
            if (sqlCommand.startsWith("select"))
            {
                // Execute the command and retrieve all data on it.
                resultSet = statement.executeQuery(sqlCommand);
                metaData = resultSet.getMetaData();

                // Finds the number of cols and rows, this number will help us
                // fill in the data.
                int numColumns = metaData.getColumnCount();

                // Start the first row of the database: the headers.
                // Note: <tr> stands for Table Row Element
                out.println( "<tr>" );
                for (int i = 0; i < numColumns; i++)
                {
                    // Note: <th> stands for table header. This will make the
                    // headers stand out from the rest of the data below it.
                    out.println( "<th>" );
                    out.println(metaData.getColumnName(i+1));
                    out.println( "</th>" );
                }
                // End of first row (headers).
                out.println( "</tr>" );

                // Essentially repeat the process we just went through except for the rest of the
                // rows in the database table.
                while (true)
                {
                    // If we reached the last row, break out of this loop.
                    if (resultSet.next() == false)
                        break;

                    out.println( "<tr>" );
                    for (int i = 0; i < numColumns; i++)
                    {
                        // <td> stands for table data
                        // Using these instead of table header like before.
                        out.println( "<td>" );
                        out.println(resultSet.getString(i+1));
                        out.println( "</td>" );
                    }
                    out.println( "</tr>" );
                }
            }

            // This runs every other command that requires a change to the database.
            // This will execute the command and then display a message to the user that
            // the change was successful. No table will be shown.
            else
            {
                // COUNT returns just the number.
                String determineNumShipmentsAbove100 = "select COUNT(*) from shipments where quantity >= 100";
                int numShipmentsAbove100;
                ResultSet shipmentsAbove100 = statement.executeQuery(determineNumShipmentsAbove100);

                shipmentsAbove100.next();
                // Since COUNT(*) is retrieving just a number we just need the resultSet to return that integer.
                numShipmentsAbove100 = shipmentsAbove100.getInt(1);

                // "like" means copy the headers from shipments for this table we are going to use to store
                // the old values before we use the new user sql command.
                String makeSQLTable = "create table oldVersion like shipments";
                String insertIntoOldTable = "insert into oldVersion select * from shipments";

                // make temporary data storing old data in it as we call the new command that'll update the db.
                statement.executeUpdate("drop table if exists oldVersion");
                statement.executeUpdate(makeSQLTable);
                statement.executeUpdate(insertIntoOldTable);
                // Determine the number of rows affected by the textarea sql command.
                int rowCount = statement.executeUpdate(sqlCommand);

                // Show how many rows were affected with this command.
                out.println( "<tr>" );
                out.println( "<th style=\"background-color: lime\">" );
                out.println( "<strong>The statement executed successfully.</strong><br>" );
                out.println( rowCount + " row(s) affected.<br><br>" );

                // Similar to process above.
                ResultSet currentShipmentsAbove100 = statement.executeQuery(determineNumShipmentsAbove100);
                int currentNumShipmentsAbove100;
                currentShipmentsAbove100.next();
                currentNumShipmentsAbove100 = currentShipmentsAbove100.getInt(1);

                // Business Logic was detected
                if (currentNumShipmentsAbove100 > numShipmentsAbove100)
                {
                    out.println( "Business Logic Detected! - Updating Supplier Status" );
                    // This will update suppliers by adding 5.
                    String queryToUpdate =
                            "update suppliers set status = status + 5" +
                                    " where snum in " +
                                        "( select distinct snum from shipments left join oldVersion" +
                                        " using (snum, pnum, jnum, quantity)" +
                                            " where oldVersion.snum is null)";

                    int numSupplierStatusMarks = statement.executeUpdate(queryToUpdate);
                    out.println( "<br>" );
                    out.println( "Business Logic Updated " + numSupplierStatusMarks + " supplier status marks." );
                }

                else
                {
                    out.println( "Business Logic Not Triggered!<br>" );
                }

                // Table was temporary and contains old data now since we just did an update query.
                statement.executeUpdate("drop table oldVersion");
                out.println( "</th>" );
                out.println( "</tr>" );
            }
        }
        catch (NullPointerException ex)
        {
            out.println( "<th>" );
            out.println( "<strong>Error connecting to database.</strong><br>" );
            out.println( "Error Msg: " + ex.getMessage());
            out.println( "</th>" );
        }

        catch (SQLException ex)
        {
            out.println( "<th>" );
            out.println( "<strong>Error executing the SQL statement above.</strong><br>" );
            out.println( "Error Msg: " + ex.getMessage());
            out.println( "</th>" );
            ex.printStackTrace();
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
    private void printHTMLElementsFromIndexJSP(PrintWriter out, String sqlCommand, boolean displayTableResults)
    {
        out.println( "<!DOCTYPE html>" );
        out.println( "<html lang=\"en\">" );
        out.println( "<head>" );
        out.println( "<title>JSP part of Three-Tier Web based application reading SQL Commands</title>" );
        out.println( "<meta charset=\"utf-8\">" );
        out.println( "<style type=\"text/css\">" );
        out.println( "<!-- body{background-color:black; color:white; font-family: verdana, arial, sans-serif; font-size: 1.1em;}" );
        out.println( " h1{color:yellow; font-size: 1.3em;} " );
        out.println( " h2{color:lime; font-size; 1.2em;}" );
        out.println( " form textarea {background-color: blue; color: white;}" );
        out.println( " form input {background-color: yellow; color: red; font-weight: bold;}" );
        out.println( " form button {background-color: lime; color: darkblue; font-weight: bold;}" );
        out.println( " table {color: black;}" );
        out.println( " table th {background-color: red;}" ); // table header
        out.println( " table td {background-color: lightgray;}" ); // regular data cells in table
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
        out.println( "<br>" );
        out.println( "<input type =\"submit\" name= \"submit\" value= \"Execute Command\" />" );
        out.println( "<button type = \"submit\" id=\"clear\" formaction=\"ServerApp\" formmethod=\"get\">Clear Results</button>" );
        out.println( "</form>" );
        out.println( "<form action =\"ServerApp\" method=\"get\">" );
        out.println( "<input type =\"submit\" name = \"reset\" value = \"Reset Form\" />" );
        out.println( "</form>" );
        out.println( "<p>All execution results will appear below the line.</p>" );
        out.println( "<hr>" );
        out.println( "<p>Database Results:</p>" );
        out.println( "<br><br><br>" );

        if (displayTableResults)
            out.println( "<table>" );

        // This is used for the clear button, end the document with no
        else
        {
            out.println( "</body>" );
            out.println( "</html>" );
        }
    }

    // Essentially the clear button, set a PrintWriter to display the HTML elements.
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException
    {
        // Same as beginning of doPost (Adapted from first-example WelcomeServlet.java)
        response.setContentType( "text/html" );
        PrintWriter out = response.getWriter();

        boolean clearResultsButtonPushed = false;
        boolean resetFormButtonPushed = false;

        // Keep the command the user wrote where button only clears the table.
        String sqlCommand = request.getParameter("sqlTextArea");
        String clear = request.getParameter("clear");
        String reset = request.getParameter("reset");
        if (clear != null)
        {
            clearResultsButtonPushed = true;
            if (sqlCommand == null)
                sqlCommand = "";
        }

        else if (reset != null)
        {
            resetFormButtonPushed = true;
            sqlCommand = "";
        }

        //  "Clear" table data or write HTML elements without the table.
        boolean displayTableResults = false;
        printHTMLElementsFromIndexJSP(out, sqlCommand, displayTableResults);
    }

}
