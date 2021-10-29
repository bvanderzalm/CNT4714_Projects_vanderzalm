/* Name: Bradley Vanderzalm
   Course: CNT4714 - Fall 2021
   Assignment title: Project 3 - Two-Tier Client-Server Application Development with MySQL and JDBC
   Date: Thursday October 28, 2021
 */

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ServerApp extends JFrame
{
    private final String DEFAULT_DATABASE_URL = "jdbc:mysql://localhost:3306/project3?useTimezone=true&serverTimezone=UTC";
    private final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    private String driverStringList [] = {DEFAULT_DRIVER, ""};
    private String databaseURLStringList [] = {DEFAULT_DATABASE_URL, "jdbc:mysql://localhost:3306/bikedb?useTimezone=true&serverTimezone=UTC"};
    private ResultSetTableModel tableModel;
    private JTextArea queryArea;
    private JButton sqlExecuteCommandButton, sqlClearCommandButton, connectToDatabaseButton, clearResultWindowButton;
    private JLabel driverLabel, databaseURLLabel, usernameLabel, passwordLabel, databaseConnectionLabel;
    private JComboBox driverComboBox, databaseURLComboBox;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JPanel userTextFieldsAndLabelsPanel, sqlCommandButtonsPanel, dbConnectButtonAndLabelPanel;
    private JPanel northComponents, centerComponents, southComponents;
    private Connection connection = null;
    private JTable resultTable;
    private boolean connectedToDatabase = false;

    private final Font FONT = new JLabel().getFont();

    // Constructor setting up GUI components, no attempt to connect to database here.
    public ServerApp()
    {
        // GUI Title
        super("Project 3 - SQL Client App - (BV - CNT 4714 - Fall 2021)");
        try
        {
            // Setup GUI components
            setUpQueryTextArea();
            setUpTextFieldsAndLabels();
            setUpButtons();

            // Set query results as blank default table. Nothing should show up yet
            // since user hasn't connected to database yet.
            resultTable = new JTable();
            resultTable.setModel(new DefaultTableModel());
            resultTable.setGridColor(Color.BLACK);

            placeGUIComponentsUsingPanels();
            setUpButtonActionListeners();
            setSize(1200, 760);
            setVisible(true);
        }

        catch (Exception ex)
        {
            popUpErrorMessage(ex.getMessage(), "Error");
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent event)
            {
                tableModel.disconnectFromDatabase();
                changeDatabaseConnectionStatus();
                System.exit(0);
            }
        }
        );
    }

    public void executeSQLCommand()
    {
        if (!connectedToDatabase)
        {
            popUpErrorMessage("Please make sure to log in and connect to the database.", "Login Connection Error");
        }

        else
        {
            // Attempt to display database results. If successful update operations log database
            try
            {
                tableModel = new ResultSetTableModel(queryArea.getText(), connection);
                tableModel.setQuery(queryArea.getText());
                resultTable.setModel(tableModel);

                // If we get to this point, the command was successfully executed.
                // Hence, update operations database.
                connectAndUpdateOperationsLogDatabase();
            }
            catch (ClassNotFoundException ex)
            {
                popUpErrorMessage(ex.getMessage(), "Error");
                clearResultWindow();
            }
            catch (SQLException sqlException)
            {
                popUpErrorMessage(sqlException.getMessage(), "Database error");
                clearResultWindow();
            }
        }
    }

    public void clearSQLCommand()
    {
        queryArea.setText("");
    }

    public void connectToDatabase()
    {
        try
        {
            // Take information given by user.
            String selectedDriver = driverComboBox.getSelectedItem().toString();
            String databaseURL = databaseURLComboBox.getSelectedItem().toString();
            String username = usernameTextField.getText();
            String password = String.copyValueOf(passwordField.getPassword());
            Class.forName(selectedDriver);

            // Attempt to connect to database
            connection = DriverManager.getConnection(databaseURL, username, password);

            // Update screen to confirm user the new database connection was successful.
            databaseConnectionLabel.setText("Connected to " + databaseURL);
            databaseConnectionLabel.setForeground(Color.GREEN);
            connectedToDatabase = true;
            clearResultWindow();
        }
        catch (NullPointerException ex)
        {
            popUpErrorMessage(ex.getMessage(), "Null Pointer, please retype");
            changeDatabaseConnectionStatus();
        }
        catch (ClassNotFoundException ex)
        {
            popUpErrorMessage(ex.getMessage(), "Driver not found");
            changeDatabaseConnectionStatus();
        }
        catch (SQLException sqlException)
        {
            popUpErrorMessage(sqlException.getMessage() + "...Ensure username and password are correct.", "Database error");
            changeDatabaseConnectionStatus();
        }
        catch (Exception ex)
        {
            popUpErrorMessage(ex.getMessage(), "Error");
            changeDatabaseConnectionStatus();
        }
    }

    private void changeDatabaseConnectionStatus()
    {
        databaseConnectionLabel.setText("  No Connection Now");
        databaseConnectionLabel.setForeground(Color.RED);
        connectedToDatabase = false;
//        tableModel.disconnectFromDatabase();
    }

    public void clearResultWindow()
    {
        resultTable.setModel(new DefaultTableModel());
    }

    public void popUpErrorMessage(String msg, String title)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    // Method adapted from SimpleJDCProperties.java from Webcourses
    public void connectAndUpdateOperationsLogDatabase()
    {
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;

        try
        {
            // Read properties file
            filein = new FileInputStream("db.properties");
            properties.load(filein);
            dataSource = new MysqlDataSource();
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            // Connect to database and create statement.
            Connection operationsConnection = dataSource.getConnection();
            Statement statement = operationsConnection.createStatement();

            int resultSet;
            String num_query_str = "update operationscount set num_queries=num_queries+1";
            String num_updates_str = "update operationscount set num_updates=num_updates+1";
            String query = queryArea.getText();

            if (query.contains("select"))
                resultSet = statement.executeUpdate(num_query_str);

            else
                resultSet = statement.executeUpdate(num_updates_str);

            operationsConnection.close();
        }
        catch (IOException io)
        {
            popUpErrorMessage(io.getMessage(), "Error reading operationslog database properties file");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            popUpErrorMessage(ex.getMessage(), "Error connecting and updating operationslog database.");
        }
    }

    // Sets up JTextArea in which user types queries.
    public void setUpQueryTextArea()
    {
        queryArea = new JTextArea("", 3, 100);
        queryArea.setWrapStyleWord(true);
        queryArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(queryArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public void setUpTextFieldsAndLabels()
    {
        userTextFieldsAndLabelsPanel = new JPanel(new GridLayout(4, 2));
        setUpJDBCDriverLabelAndComboBox();
        setUpDatabaseURLLabelAndComboBox();
        setUpUsernameLabelAndTextField();
        setUpPasswordLabelAndTextField();
        setUpDatabaseConnectionLabel();
    }

    public void setUpButtons()
    {
        setUpPanelForPositioning();
        setUpClearSQLCommandButton();
        setUpExecuteSQLCommandButton();
        setUpConnectToDatabaseButton();
        setUpClearResultWindowButton();
    }

    // This method deals with positioning of all components. Uses a mix of GridLayout and
    // hard coding positioning.
    public void placeGUIComponentsUsingPanels()
    {
        addInstructionLabels();

        northComponents = new JPanel(new GridLayout(2, 2));
        // Blank labels to help with spacing and positioning.
        JLabel blank1 = new JLabel(""), blank2 = new JLabel("");
        northComponents.add(blank1, BorderLayout.NORTH);
        northComponents.add(blank2, BorderLayout.NORTH);
        northComponents.add(userTextFieldsAndLabelsPanel);
        northComponents.add(queryArea);

        centerComponents = new JPanel();
        centerComponents.add(sqlCommandButtonsPanel);

        southComponents = new JPanel();
        southComponents.setLayout(new BorderLayout(20, 0));
        southComponents.add(dbConnectButtonAndLabelPanel, BorderLayout.NORTH);
        southComponents.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        southComponents.add(clearResultWindowButton, BorderLayout.SOUTH);

        // Add to GUI
        add(northComponents, BorderLayout.NORTH);
        add(centerComponents, BorderLayout.CENTER);
        add(southComponents, BorderLayout.SOUTH);
    }

    // This adds labels instructing the user what to do. Places above SQL query area and
    // database info area.
    public void addInstructionLabels()
    {
        JLabel enterDbInfo = new JLabel("Enter Database Information");
        enterDbInfo.setForeground(Color.BLUE);
        enterDbInfo.setFont(new Font(FONT.getName(), Font.BOLD, 14));
        enterDbInfo.setBounds(7, 80, 230,25);

        JLabel enterSQLCmd = new JLabel("Enter An SQL Command");
        enterSQLCmd.setForeground(Color.BLUE);
        enterSQLCmd.setFont(new Font(FONT.getName(), Font.BOLD, 14));
        enterSQLCmd.setBounds(604, 80, 230, 25);

        // Add to panel
        add(enterDbInfo);
        add(enterSQLCmd);
    }

    //******************** Setup for labels and TextFields********************

    public void setUpJDBCDriverLabelAndComboBox()
    {
        driverLabel = new JLabel("  JDBC Driver");//, SwingConstants.LEFT);
        driverLabel.setBackground(Color.GRAY);
        driverLabel.setForeground(Color.BLACK);
        driverLabel.setOpaque(true);

        driverComboBox = new JComboBox(driverStringList);

        userTextFieldsAndLabelsPanel.add(driverLabel);
        userTextFieldsAndLabelsPanel.add(driverComboBox);
    }

    public void setUpDatabaseURLLabelAndComboBox()
    {
        databaseURLLabel = new JLabel("  Database URL");//, SwingConstants.LEFT);
        databaseURLLabel.setBackground(Color.GRAY);
        databaseURLLabel.setForeground(Color.BLACK);
        databaseURLLabel.setOpaque(true);

        databaseURLComboBox = new JComboBox(databaseURLStringList);

        userTextFieldsAndLabelsPanel.add(databaseURLLabel);
        userTextFieldsAndLabelsPanel.add(databaseURLComboBox);
    }

    public void setUpUsernameLabelAndTextField()
    {
        usernameLabel = new JLabel("  Username");
        usernameLabel.setBackground(Color.GRAY);
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setOpaque(true);
        usernameTextField = new JTextField();
        userTextFieldsAndLabelsPanel.add(usernameLabel);
        userTextFieldsAndLabelsPanel.add(usernameTextField);
    }

    public void setUpPasswordLabelAndTextField()
    {
        passwordLabel = new JLabel("  Password");
        passwordLabel.setBackground(Color.GRAY);
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setOpaque(true);
        passwordField = new JPasswordField();
        userTextFieldsAndLabelsPanel.add(passwordLabel);
        userTextFieldsAndLabelsPanel.add(passwordField);
    }

    public void setUpDatabaseConnectionLabel()
    {
        databaseConnectionLabel = new JLabel("  No Connection Now.");
        databaseConnectionLabel.setBackground(Color.BLACK);
        databaseConnectionLabel.setForeground(Color.RED);
        databaseConnectionLabel.setOpaque(true);
    }

    //******************** Setup for Buttons ********************

    // This sets up a gridlayout to put empty labels so the sql command buttons can be right aligned
    public void setUpPanelForPositioning()
    {
        sqlCommandButtonsPanel = new JPanel(new GridLayout(1, 4));
        JLabel blank1 = new JLabel(" ");
        JLabel blank2 = new JLabel(" ");
        sqlCommandButtonsPanel.add(blank1);
        sqlCommandButtonsPanel.add(blank2);
    }

    public void setUpClearSQLCommandButton() {
        sqlClearCommandButton = new JButton("Clear SQL Command");
        sqlClearCommandButton.setBackground(Color.WHITE);
        sqlClearCommandButton.setForeground(Color.RED);
        sqlClearCommandButton.setBorderPainted(false);
        sqlClearCommandButton.setOpaque(true);
        sqlCommandButtonsPanel.add(sqlClearCommandButton);
    }

    public void setUpExecuteSQLCommandButton() {
        sqlExecuteCommandButton = new JButton("Execute SQL Command");
        sqlExecuteCommandButton.setBackground(Color.GREEN);
        sqlExecuteCommandButton.setForeground(Color.BLACK);
        sqlExecuteCommandButton.setBorderPainted(false);
        sqlExecuteCommandButton.setOpaque(true);
        sqlCommandButtonsPanel.add(sqlExecuteCommandButton);
    }

    public void setUpConnectToDatabaseButton()
    {
        connectToDatabaseButton = new JButton("Connect to Database");
        connectToDatabaseButton.setBackground(Color.BLUE);
        connectToDatabaseButton.setForeground(Color.YELLOW);
        connectToDatabaseButton.setBorderPainted(false);
        connectToDatabaseButton.setOpaque(true);

        dbConnectButtonAndLabelPanel = new JPanel(new GridLayout(1, 5));
        dbConnectButtonAndLabelPanel.add(connectToDatabaseButton);
        dbConnectButtonAndLabelPanel.add(databaseConnectionLabel);
    }

    public void setUpClearResultWindowButton()
    {
        clearResultWindowButton = new JButton("Clear Result Window");
        clearResultWindowButton.setBackground(Color.YELLOW);
        clearResultWindowButton.setForeground(Color.BLACK);
        clearResultWindowButton.setBorderPainted(false);
        clearResultWindowButton.setOpaque(true);
    }

    public void setUpButtonActionListeners()
    {
        ActionListener buttonActionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Object actionEventObject = e.getSource();

                if (actionEventObject == sqlExecuteCommandButton)
                {
                    executeSQLCommand();
                }

                else if (actionEventObject == sqlClearCommandButton)
                {
                    clearSQLCommand();
                }

                else if (actionEventObject == connectToDatabaseButton)
                {
                    connectToDatabase();
                }

                else if (actionEventObject == clearResultWindowButton)
                {
                    clearResultWindow();
                }
            }
        };

        sqlExecuteCommandButton.addActionListener(buttonActionListener);
        sqlClearCommandButton.addActionListener(buttonActionListener);
        connectToDatabaseButton.addActionListener(buttonActionListener);
        clearResultWindowButton.addActionListener(buttonActionListener);
    }

    public static void main(String [] args)
    {
        ServerApp app = new ServerApp();
    }
}
