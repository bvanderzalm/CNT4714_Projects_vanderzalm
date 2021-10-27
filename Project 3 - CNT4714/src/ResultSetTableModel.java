import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.table.AbstractTableModel;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class ResultSetTableModel extends AbstractTableModel
{
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numberOfRows;

    // Keep track of database connection status
    private boolean connectedToDatabase = false;

    public ResultSetTableModel(String query) throws SQLException, ClassNotFoundException
    {
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;


    }

}
