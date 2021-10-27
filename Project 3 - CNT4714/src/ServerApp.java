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
    private ResultSetTableModel tableModel;
    private JTextArea queryArea;
    private JScrollPane scrollPane;
    private JButton sqlExecuteCommandButton, sqlClearCommandButton, connectToDatabaseButton, clearResultWindowButton;
    private Box queryBox;
    private JTable resultTable;

    public ServerApp()
    {
        super("Project 3 - SQL Client App - (BV - CNT 4714 - Fall 2021)");
        try
        {
            tableModel = new ResultSetTableModel(DEFAULT_QUERY);
            setUpQueryTextArea();
            setUpButtons();
            setUpQueryBox();
            resultTable = new JTable(tableModel);
            resultTable.setGridColor(Color.BLACK);

            add(queryBox, BorderLayout.NORTH);
            add(new JScrollPane(resultTable), BorderLayout.CENTER);
            setUpButtonActionListeners();
            setSize(600, 300);
            setVisible(true);

        }

        catch (ClassNotFoundException classNotFound)
        {
            popUpErrorMessage("MySQL driver not found", "Driver not found");
        }

        catch (SQLException sqlException)
        {
            popUpErrorMessage(sqlException.getMessage(), "Database error");
            //******************************************************************************************
            //tableModel.disconnectFromDatabase();
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent event)
            {
                tableModel.disconnectFromDatabase();
                System.exit(0);
            }
        }
        );
    }

    public void executeSQLCommand()
    {
        try
        {
            tableModel.setQuery(queryArea.getText());
        }

        catch (SQLException sqlException)
        {
            popUpErrorMessage(sqlException.getMessage(), "Database error");

            try
            {
                tableModel.setQuery(DEFAULT_QUERY);
                queryArea.setText(DEFAULT_QUERY);
            }

            catch (SQLException sqlException2)
            {
                popUpErrorMessage(sqlException2.getMessage(), "Database error");
                //***************************************************************************************************************************************
//                tableModel.disconnectFromDatabase();
            }
        }
    }

    public void popUpErrorMessage(String msg, String title)
    {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    // Sets up JTextArea in which user types queries.
    public void setUpQueryTextArea() {
        queryArea = new JTextArea(DEFAULT_QUERY, 3, 100);
        queryArea.setWrapStyleWord(true);
        queryArea.setLineWrap(true);
        scrollPane = new JScrollPane(queryArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public void setUpButtons() {
        setUpClearSQLCommandButton();
        setUpExecuteSQLCommandButton();
        setUpConnectToDatabaseButton();
        setUpClearResultWindowButton();
    }

    public void setUpQueryBox()
    {
        queryBox = Box.createHorizontalBox();
        queryBox.add(scrollPane);
        queryBox.add(sqlClearCommandButton);
        queryBox.add(sqlExecuteCommandButton);
//        box.add(connectToDatabaseButton);
//        box.add(clearResultWindowButton);
    }

    public void setUpClearSQLCommandButton() {
        sqlClearCommandButton = new JButton("Clear SQL Command");
        sqlClearCommandButton.setBackground(Color.WHITE);
        sqlClearCommandButton.setForeground(Color.RED);
        sqlClearCommandButton.setBorderPainted(false);
        sqlClearCommandButton.setOpaque(true);
    }

    public void setUpExecuteSQLCommandButton() {
        sqlExecuteCommandButton = new JButton("Execute SQL Command");
        sqlExecuteCommandButton.setBackground(Color.GREEN);
        sqlExecuteCommandButton.setForeground(Color.BLACK);
        sqlExecuteCommandButton.setBorderPainted(false);
        sqlExecuteCommandButton.setOpaque(true);
    }

    public void setUpConnectToDatabaseButton()
    {
        connectToDatabaseButton = new JButton("Connect to Database");
        connectToDatabaseButton.setBackground(Color.BLUE);
        connectToDatabaseButton.setForeground(Color.YELLOW);
        connectToDatabaseButton.setBorderPainted(false);
        connectToDatabaseButton.setOpaque(true);
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
        }
    }

    public static void main(String [] args)
    {
        ServerApp app = new ServerApp();
    }
}
