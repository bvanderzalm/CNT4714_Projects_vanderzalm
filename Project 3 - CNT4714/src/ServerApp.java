import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public ServerApp()
    {
        super("Project 3 - SQL Client App - (BV - CNT 4714 - Fall 2021)");
        try
        {
            setUpQueryTextArea();
            setUpTextFieldsAndLabels();
            setUpButtons();

            resultTable = new JTable();
            resultTable.setModel(new DefaultTableModel());
            resultTable.setGridColor(Color.BLACK);

            placeItemsUsingPanels();

            setUpButtonActionListeners();
            setSize(1200, 650);
            setVisible(true);
        }

//        catch (ClassNotFoundException classNotFound)
//        {
//            popUpErrorMessage("MySQL driver not found", "Driver not found");
//        }
//
//        catch (SQLException sqlException)
//        {
//            popUpErrorMessage(sqlException.getMessage(), "Database error");
//            //******************************************************************************************
//            //tableModel.disconnectFromDatabase();
//        }

        catch (Exception ex)
        {
            popUpErrorMessage(ex.getMessage(), "Error");
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent event)
            {
                //tableModel.disconnectFromDatabase();
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
            try
            {
                tableModel = new ResultSetTableModel(queryArea.getText(), connection);
                tableModel.setQuery(queryArea.getText());
                resultTable.setModel(tableModel);

                // If we get to this point, the command was successfully executed.
                // Hence, update operations database.
                connectToOperationsLogDatabase();
                updateOperationsLog();
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

    public void connectToOperationsLogDatabase()
    {

    }

    public void updateOperationsLog()
    {
        
    }

    public void clearSQLCommand()
    {
        queryArea.setText("");
    }

    public void connectToDatabase()
    {
        try
        {
            String selectedDriver = driverComboBox.getSelectedItem().toString();
            String databaseURL = databaseURLComboBox.getSelectedItem().toString();
            String username = usernameTextField.getText();
            String password = String.copyValueOf(passwordField.getPassword());
            Class.forName(selectedDriver);

            connection = DriverManager.getConnection(databaseURL, username, password);

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
        databaseConnectionLabel.setText("No Connection Now");
        databaseConnectionLabel.setForeground(Color.RED);
        connectedToDatabase = false;
        // tableModel.disconnectFromDatabase();
        // tableModel = null;
    }

    public void clearResultWindow()
    {
        resultTable.setModel(new DefaultTableModel());
    }

    public void popUpErrorMessage(String msg, String title)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
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
        sqlCommandButtonsPanel = new JPanel(new GridLayout(1, 4));
        setUpClearSQLCommandButton();
        setUpExecuteSQLCommandButton();
        setUpConnectToDatabaseButton();
        setUpClearResultWindowButton();


    }

    public void placeItemsUsingPanels()
    {
        northComponents = new JPanel(new GridLayout(1, 2));
        northComponents.add(userTextFieldsAndLabelsPanel);
        northComponents.add(queryArea);

        centerComponents = new JPanel();
        centerComponents.add(sqlCommandButtonsPanel, BorderLayout.SOUTH);

        southComponents = new JPanel();
        southComponents.setLayout(new BorderLayout(20, 0));
        southComponents.add(dbConnectButtonAndLabelPanel, BorderLayout.NORTH);
        southComponents.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        southComponents.add(clearResultWindowButton, BorderLayout.SOUTH);

        add(northComponents, BorderLayout.NORTH);
        add(centerComponents, BorderLayout.CENTER);
        add(southComponents, BorderLayout.SOUTH);
    }

    //******************** Setup for labels and TextFields********************

    public void setUpJDBCDriverLabelAndComboBox()
    {
        driverLabel = new JLabel("JDBC Driver");//, SwingConstants.LEFT);
        driverLabel.setBackground(Color.GRAY);
        driverLabel.setForeground(Color.BLACK);
        driverLabel.setOpaque(true);

        driverComboBox = new JComboBox(driverStringList);

        userTextFieldsAndLabelsPanel.add(driverLabel);
        userTextFieldsAndLabelsPanel.add(driverComboBox);
    }

    public void setUpDatabaseURLLabelAndComboBox()
    {
        databaseURLLabel = new JLabel("Database URL");//, SwingConstants.LEFT);
        databaseURLLabel.setBackground(Color.GRAY);
        databaseURLLabel.setForeground(Color.BLACK);
        databaseURLLabel.setOpaque(true);

        databaseURLComboBox = new JComboBox(databaseURLStringList);

        userTextFieldsAndLabelsPanel.add(databaseURLLabel);
        userTextFieldsAndLabelsPanel.add(databaseURLComboBox);
    }

    public void setUpUsernameLabelAndTextField()
    {
        usernameLabel = new JLabel("Username");
        usernameLabel.setBackground(Color.GRAY);
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setOpaque(true);
        usernameTextField = new JTextField();
        userTextFieldsAndLabelsPanel.add(usernameLabel);
        userTextFieldsAndLabelsPanel.add(usernameTextField);
    }

    public void setUpPasswordLabelAndTextField()
    {
        passwordLabel = new JLabel("Password");
        passwordLabel.setBackground(Color.GRAY);
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setOpaque(true);
        passwordField = new JPasswordField();
        userTextFieldsAndLabelsPanel.add(passwordLabel);
        userTextFieldsAndLabelsPanel.add(passwordField);
    }

    public void setUpDatabaseConnectionLabel()
    {
        databaseConnectionLabel = new JLabel("No Connection Now.");
        databaseConnectionLabel.setBackground(Color.BLACK);
        databaseConnectionLabel.setForeground(Color.RED);
        databaseConnectionLabel.setOpaque(true);
    }

    //******************** Setup for Buttons ********************

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
