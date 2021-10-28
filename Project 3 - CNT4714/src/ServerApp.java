import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class ServerApp extends JFrame
{
    static final String DEFAULT_QUERY = "";
    private final String DEFAULT_DATABASE_URL = "jdbc:mysql://localhost:3306/project3?useTimezone=true&serverTimezone=UTC";
    private final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    private String driverStringList [] = {DEFAULT_DRIVER, ""}, databaseURLStringList [] = {DEFAULT_DATABASE_URL, ""};
    private ResultSetTableModel tableModel;
    private JTextArea queryArea;
    private JScrollPane scrollPane;
    private JButton sqlExecuteCommandButton, sqlClearCommandButton, connectToDatabaseButton, clearResultWindowButton;
    private JLabel driverLabel, databaseURLLabel, usernameLabel, passwordLabel, databaseConnectionLabel;
    private JComboBox driverComboBox, databaseURLComboBox;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private Box queryBox;
    private JPanel userTextFieldsAndLabelsPanel, sqlCommandButtonsPanel, dbConnectButtonAndLabelPanel;
    private JPanel northComponents, centerComponents, southComponents;

    private JTable resultTable;

    public ServerApp()
    {
        super("Project 3 - SQL Client App - (BV - CNT 4714 - Fall 2021)");
        try
        {
//            tableModel = new ResultSetTableModel(DEFAULT_QUERY);
            setUpQueryTextArea();
            setUpTextFieldsAndLabels();
            setUpButtons();




            //setUpQueryBox();

            //tableModel = new ResultSetTableModel();
            //resultTable = new JTable(tableModel);
            //resultTable.setGridColor(Color.BLACK);

            placeItemsUsingPanels();

            //add(queryBox);//, BorderLayout.NORTH);
            //add(new JScrollPane(resultTable), BorderLayout.CENTER);
            setUpButtonActionListeners();
            setSize(800, 500);
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
        System.out.println("Execute SQLCommand");
//        try
//        {
//            tableModel.setQuery(queryArea.getText());
//        }
//
//        catch (SQLException sqlException)
//        {
//            popUpErrorMessage(sqlException.getMessage(), "Database error");
//
//            try
//            {
//                tableModel.setQuery(DEFAULT_QUERY);
//                queryArea.setText(DEFAULT_QUERY);
//            }
//
//            catch (SQLException sqlException2)
//            {
//                popUpErrorMessage(sqlException2.getMessage(), "Database error");
//                //***************************************************************************************************************************************
////                tableModel.disconnectFromDatabase();
//            }
//        }
    }

    public void popUpErrorMessage(String msg, String title)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    // Sets up JTextArea in which user types queries.
    public void setUpQueryTextArea()
    {
        queryArea = new JTextArea(DEFAULT_QUERY, 3, 100);
        queryArea.setWrapStyleWord(true);
        queryArea.setLineWrap(true);
        scrollPane = new JScrollPane(queryArea,
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

//    public void setUpQueryBox()
//    {
//        queryBox = Box.createHorizontalBox();
//        queryBox.add(scrollPane);
//        queryBox.add(sqlClearCommandButton);
//        queryBox.add(sqlExecuteCommandButton);
//        queryBox.setBounds(400, 200, 230, 50);
////        box.add(connectToDatabaseButton);
////        box.add(clearResultWindowButton);
//    }

    public void placeItemsUsingPanels()
    {
        northComponents = new JPanel(new GridLayout(1, 2));
        northComponents.add(userTextFieldsAndLabelsPanel);
        northComponents.add(queryArea);

        centerComponents = new JPanel();
//        centerComponents.add(sqlCommandButtonsPanel, BorderLayout.EAST);
//        centerComponents.add(dbConnectButtonAndLabelPanel, BorderLayout.SOUTH);

//        centerComponents.add(dbConnectButtonAndLabelPanel, BorderLayout.EAST);
        centerComponents.add(sqlCommandButtonsPanel, BorderLayout.SOUTH);

        southComponents = new JPanel();
        southComponents.setLayout(new BorderLayout(20, 0));
        //south.add(scrollpane) borderlayout north
        southComponents.add(dbConnectButtonAndLabelPanel, BorderLayout.NORTH);
        southComponents.add(clearResultWindowButton, BorderLayout.SOUTH);

        add(northComponents, BorderLayout.NORTH);
        add(centerComponents, BorderLayout.CENTER);
        add(southComponents, BorderLayout.SOUTH);
    }



    //******************** Setup for labels and TextFields********************

    public void setUpJDBCDriverLabelAndComboBox()
    {
        // Label
        driverLabel = new JLabel("JDBC Driver");//, SwingConstants.LEFT);
        driverLabel.setBackground(Color.GRAY);
        driverLabel.setForeground(Color.BLACK);
        driverLabel.setOpaque(true);
//        driverLabel.setBounds(10, 20, 23, 25);
//        add(driverLabel);//, BorderLayout.WEST);

        // ComboBox (Textfield with different choices)
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
//        databaseURLLabel.setBounds(10, 40, 23, 25);
//        add(databaseURLLabel);//, BorderLayout.WEST);

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
        //sqlClearCommandButton.setBounds(400, 200, 230, 25);
        //add(sqlClearCommandButton);
        sqlCommandButtonsPanel.add(sqlClearCommandButton);
    }

    public void setUpExecuteSQLCommandButton() {
        sqlExecuteCommandButton = new JButton("Execute SQL Command");
        sqlExecuteCommandButton.setBackground(Color.GREEN);
        sqlExecuteCommandButton.setForeground(Color.BLACK);
        sqlExecuteCommandButton.setBorderPainted(false);
        sqlExecuteCommandButton.setOpaque(true);
//        sqlExecuteCommandButton.setBounds(450, 200, 230, 25);
//        add(sqlExecuteCommandButton);
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
//        clearResultWindowButton.getPreferredSize();
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
                    System.out.println("Clear SQL Command");
                }

                else if (actionEventObject == connectToDatabaseButton)
                {
                    System.out.println("Connect to Database");
                }

                else if (actionEventObject == clearResultWindowButton)
                {
                    System.out.println("Clear Result Window");
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
