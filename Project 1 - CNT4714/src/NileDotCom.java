// Bradley Vanderzalm
// CNT 4714, Fall 2021

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class NileDotCom
{
    private static JPanel websitePanel;
    private static JFrame websiteFrame;

    private JLabel numItemsLabel, itemIDLabel, itemQuantityLabel, itemInfoLabel, orderSubtotalLabel;
//    private static JLabel itemIDLabel;
//    private static JLabel itemQuantityLabel;
//    private static JLabel itemInfoLabel;
//    private static JLabel orderSubtotalLabel;

    private JTextField numItemsTextField, itemIDTextField, itemQuantityTextField, itemInfoTextField, orderSubtotalTextField;
//    private static JTextField itemIDTextField;
//    private static JTextField itemQuantityTextField;
//    private static JTextField itemInfoTextField;
//    private static JTextField orderSubtotalTextField;


    private JButton processItemButton, confirmItemButton, viewOrderButton, finishOrderButton, newOrderButton, exitButton;
//    private static JButton confirmItemButton;
//    private static JButton viewOrderButton;
//    private static JButton finishOrderButton;
//    private static JButton newOrderButton;
//    private static JButton exitButton;

    public void startUpWebsite()
    {
        openPrimaryGUI();
        setUpLabelInstructions();
        setUpUserTextFields();
        setUpButtons();
        websiteFrame.setVisible(true);

//        runWebsite();
    }

    public void openPrimaryGUI()
    {
        websitePanel = new JPanel();
        websiteFrame = new JFrame();
        websiteFrame.setSize(800, 400);
        websiteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        websiteFrame.setTitle("Nile Dot Com BV");
        websiteFrame.add(websitePanel);

        websitePanel.setLayout(null);
    }

    public void setUpLabelInstructions()
    {
        setUpNumItemsLabel();
        setUpItemIDLabel();
        setUpItemQuantityLabel();
        setUpItemInfoLabel();
        setUpOrderSubtotalLabel();
    }

    public void setUpUserTextFields()
    {
        setUpNumItemsTextField();
        setUpItemIDTextField();
        setUpItemQuantityTextField();
        setUpItemInfoTextField();
        setUpOrderSubtotalTextField();
    }

    public void setUpButtons()
    {
        setUpProcessItemButton();
        setUpConfirmItemButton();
        setUpViewOrderButton();
        setUpFinishOrderButton();
        setUpNewOrderButton();
        setUpExitButton();
        setUpButtonActionListeners();
    }

    public void processItem() throws Exception {
        String numItemsStr = null;
        String itemID;
        String itemQuantityStr;
        int numItems;
        int itemQuantity;

        while(true)
        {
            try
            {
                numItemsStr = numItemsTextField.getText();
                itemID = itemIDTextField.getText();
                itemQuantityStr = itemQuantityTextField.getText();
            }
            catch (NullPointerException ex)
            {
                printErrorMsg("Make sure to fill out all necessary boxes.");
                break;
            }

            try
            {
                numItems = Integer.parseInt(numItemsStr);
                itemQuantity = Integer.parseInt(itemQuantityStr);
            }
            catch (NumberFormatException ex)
            {
                printErrorMsg("Make sure to fill all boxes correctly.");
                break;
            }

            if (numItems <= 0 || itemQuantity <= 0)
            {
                printErrorMsg("Please enter a number greater than zero");
                break;
            }

            boolean inStock = checkInventory(numItems, itemID, itemQuantity);

            break;
        }
    }

    private void printErrorMsg(String s)
    {
        System.out.println(s);
    }

    // maybe change this to void instead of boolean. Seems like this method is doing all of the work anyway.
    public boolean checkInventory(int numItems, String itemID, int itemQuantity)
    {
        try (Scanner input = new Scanner(Paths.get("inventory.txt")))
        {
            while (input.hasNext())
            {
                String databaseLine = null;
                databaseLine = input.nextLine();
//                System.out.println(databaseLine);
                String[] tempItemDetails = databaseLine.split(", ");
//                System.out.println(Arrays.toString(tempItemDetails));

                if (tempItemDetails[0].equalsIgnoreCase(itemID))
                {
//                    System.out.println("Found item");

                    if (tempItemDetails[2].equals("true"))
                    {
                        double itemPrice = Double.parseDouble(tempItemDetails[3]);
                        double totalPrice = itemPrice * itemQuantity;
                        int discountRate = checkForDiscount(itemQuantity);

                        if (discountRate != 0)
                        {
                            totalPrice = applyDiscount(totalPrice, discountRate);
                        }

                        totalPrice = Math.round(totalPrice);

                        String itemInfo =
                                itemID + " " + tempItemDetails[1] + " " + "$" +
                                itemPrice + " " + itemQuantity + " " +
                                discountRate + "%" + " " + "$" + totalPrice;

                        itemInfoTextField.setText(itemInfo);
                    }

                    else
                    {
                        printErrorMsg("Sorry... that item is out of stock, please try another item");
                    }

                    break;
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            printErrorMsg("Error, database not found");
        }
        catch (IOException ex)
        {
            printErrorMsg("Unexpected error.");
        }

        return false;
    }

    public int checkForDiscount(int itemQnty)
    {
        if (itemQnty >= 1 && itemQnty <= 4)
            return 0;
        else if (itemQnty >= 5 && itemQnty<= 9)
            return 10;
        else if (itemQnty >= 10 && itemQnty <= 14)
            return 15;
        else
            return 20;
    }

    public double applyDiscount(double price, double rate)
    {
        return (price - (price * (rate / 100)));
//        double actualRate = rate / 100;
//        double subtractThis = price * actualRate;
//        return price - subtractThis;
    }

    //******************** Setup for labels ********************

    public void setUpNumItemsLabel()
    {
        numItemsLabel = new JLabel("Enter number of items in this order: ", SwingConstants.RIGHT);
        numItemsLabel.setBounds(10, 20, 230, 25);
        websitePanel.add(numItemsLabel);
    }

    public void setUpItemIDLabel()
    {
        itemIDLabel = new JLabel("Enter item ID for Item #~: ", SwingConstants.RIGHT);
        itemIDLabel.setBounds(10,40, 230, 25);
        websitePanel.add(itemIDLabel);
    }

    public void setUpItemQuantityLabel()
    {
        itemQuantityLabel = new JLabel("Enter quantity for Item #~: ", SwingConstants.RIGHT);
        itemQuantityLabel.setBounds(10, 60, 230, 25);
        websitePanel.add(itemQuantityLabel);
    }

    public void setUpItemInfoLabel()
    {
        itemInfoLabel = new JLabel("Item #~ info: ", SwingConstants.RIGHT);
        itemInfoLabel.setBounds(10, 80, 230, 25);
        websitePanel.add(itemInfoLabel);
    }

    public void setUpOrderSubtotalLabel()
    {
        orderSubtotalLabel = new JLabel("Order subtotal for ~ item(s): ", SwingConstants.RIGHT);
        orderSubtotalLabel.setBounds(10, 100, 230, 25);
        websitePanel.add(orderSubtotalLabel);
    }

    //******************** Setup for Text Fields ********************

    public void setUpNumItemsTextField()
    {
        numItemsTextField = new JTextField();
        numItemsTextField.setBounds(241, 20, 500, 25);
        websitePanel.add(numItemsTextField);
    }

    public void setUpItemIDTextField()
    {
        itemIDTextField = new JTextField();
        itemIDTextField.setBounds(241, 40, 500, 25);
        websitePanel.add(itemIDTextField);
    }

    public void setUpItemQuantityTextField()
    {
        itemQuantityTextField = new JTextField();
        itemQuantityTextField.setBounds(241, 60, 500, 25);
        websitePanel.add(itemQuantityTextField);
    }

    public void setUpItemInfoTextField()
    {
        itemInfoTextField = new JTextField();
        itemInfoTextField.setBounds(241, 80, 500, 25);
        itemInfoTextField.setEnabled(false);
        websitePanel.add(itemInfoTextField);
    }

    public void setUpOrderSubtotalTextField()
    {
        orderSubtotalTextField = new JTextField();
        orderSubtotalTextField.setBounds(241, 100, 500, 25);
        orderSubtotalTextField.setEnabled(false);
        orderSubtotalTextField.setText("$0.00");
        websitePanel.add(orderSubtotalTextField);
    }

    //******************** Setup for Buttons ********************

    public void setUpProcessItemButton()
    {
        processItemButton = new JButton("Process Item #~");

        Dimension tempCoords = processItemButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

//        System.out.println("process item width is " + width);

        processItemButton.setBounds(10, 150, width, height);
//        processItemButton.addActionListener();
        websitePanel.add(processItemButton);
    }

    public void setUpConfirmItemButton()
    {
        confirmItemButton = new JButton("Confirm Item #~");

        Dimension tempCoords = confirmItemButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

//        System.out.println("confirm item width is " + width);

        confirmItemButton.setBounds(159, 150, width, height);

//        confirmItemButton.setEnabled(false);
        websitePanel.add(confirmItemButton);
    }

    public void setUpViewOrderButton()
    {
        viewOrderButton = new JButton("View Order");

        Dimension tempCoords = viewOrderButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

//        System.out.println("view order width is " + width);

        viewOrderButton.setBounds(311, 150, width, height);

//        viewOrderButton.setEnabled(false);
        websitePanel.add(viewOrderButton);
    }

    public void setUpFinishOrderButton()
    {
        finishOrderButton = new JButton("Finish Order");

        Dimension tempCoords = finishOrderButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

//        System.out.println("finish order width is " + width);

        finishOrderButton.setBounds(428, 150, width, height);

//        finishOrderButton.setEnabled(false);
        websitePanel.add(finishOrderButton);
    }

    public void setUpNewOrderButton()
    {
        newOrderButton = new JButton("New Order");

        Dimension tempCoords = newOrderButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

//        System.out.println("new order width is " + width);

        newOrderButton.setBounds(554, 150, width, height);

        websitePanel.add(newOrderButton);
    }

    public void setUpExitButton()
    {
        exitButton = new JButton("Exit");

        Dimension tempCoords = exitButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

//        System.out.println("exit width is " + width);

        exitButton.setBounds(669, 150, width, height);

        websitePanel.add(exitButton);
    }

    public void setUpButtonActionListeners()
    {
        ActionListener buttonActionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Object actionEventObject = e.getSource();

                if (actionEventObject == processItemButton)
                {
                    try {
                        processItem();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

                else if (actionEventObject == confirmItemButton)
                {
                    System.out.println("Confirm Item");
                }

                else if (actionEventObject == viewOrderButton)
                {
                    System.out.println("View Order");
                }

                else if (actionEventObject == finishOrderButton)
                {
                    System.out.println("Finish Order");
                }

                else if (actionEventObject == newOrderButton)
                {
                    System.out.println("New Order");
                }

                else if (actionEventObject == exitButton)
                {
                    // Close the GUI and exit the program.
                    websiteFrame.dispatchEvent(new WindowEvent(websiteFrame, WindowEvent.WINDOW_CLOSING));
                }
            }
        };

        processItemButton.addActionListener(buttonActionListener);
        confirmItemButton.addActionListener(buttonActionListener);
        viewOrderButton.addActionListener(buttonActionListener);
        finishOrderButton.addActionListener(buttonActionListener);
        newOrderButton.addActionListener(buttonActionListener);
        exitButton.addActionListener(buttonActionListener);
    }

    public static void main(String [] args)
    {
        NileDotCom nile = new NileDotCom();
        nile.startUpWebsite();
    }
}
