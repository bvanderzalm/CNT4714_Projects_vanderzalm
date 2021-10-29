import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.table.AbstractTableModel;
import java.sql.*;


// Adapted from ResultSetTableModel.java from Webcourses

public class ResultSetTableModel extends AbstractTableModel
{
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numberOfRows;

    // Keep track of database connection status
    private boolean connectedToDatabase = false;

    public ResultSetTableModel(String query, Connection connection) throws SQLException, ClassNotFoundException
    {
        // Connecting to database was already handled in GUI class.
        this.connection = connection;
        try
        {
            // Create Statement to query database
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Update database connection status
            connectedToDatabase = true;

            // Set query and execute it
            setQuery(query);

        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }

    public Class getColumnClass(int column) throws IllegalStateException
    {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        // Determine Java class of column
        try
        {
            String className = metaData.getColumnClassName(column + 1);

            return Class.forName(className);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        // If problems occur above, assume type Object.
        return Object.class;
    }

    public int getColumnCount() throws IllegalStateException
    {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        try
        {
            return metaData.getColumnCount();
        }
        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

        // If problems occurs, return 0 for number of columns
        return 0;
    }

    public String getColumnName(int column) throws IllegalStateException
    {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        try
        {
            return metaData.getColumnName(column + 1);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        // If problems occur, return empty string for column name
        return "";
    }

    public int getRowCount() throws IllegalStateException
    {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        return numberOfRows;
    }

    public Object getValueAt(int row, int column) throws IllegalStateException
    {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        // Obtain value at specified resultSet row and column
        try
        {
            resultSet.next();
            resultSet.absolute(row + 1);
            return resultSet.getObject(column + 1);
        }

        catch (SQLException sqlException)
        {
            sqlException.printStackTrace();
        }

        // If problems occur, return empty string object.
        return "";
    }

    public void setQuery(String query) throws SQLException, IllegalStateException
    {
        if (!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        // Specify query and execute it
        resultSet = statement.executeQuery(query);

        // Obtain meta data for ResultSet
        metaData = resultSet.getMetaData();

        // Determine number of rows in ResultSet
        resultSet.last();                   // move to last row
        numberOfRows = resultSet.getRow();  // get row number

        // Notify JTable that model has changed
        fireTableStructureChanged();
    }

    public void setUpdate(String query) throws SQLException, IllegalStateException
    {
        int res;
        if (!connectedToDatabase)
            throw new IllegalStateException("Not connected to Database");

        // Specify query and execute it
        res = statement.executeUpdate(query);

        /*
        metaData = resultSet.getMetaData();
        resultSet.last();
        numberOfRows = resultSet.getRow();
         */

        fireTableStructureChanged();
    }
}
