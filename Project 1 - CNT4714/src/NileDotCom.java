/* Name: Bradley Vanderzalm
   Course: CNT4714 - Fall 2021
   Assignment title: Project 1 - Event-driven Enterprise Simulation
   Date: Sunday September 12, 2021
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.StringBuilder;
import java.util.TimeZone;

public class NileDotCom
{
    private JPanel websitePanel;
    private JFrame websiteFrame;
    private JLabel numItemsLabel, itemIDLabel, itemQuantityLabel, itemInfoLabel, orderSubtotalLabel;
    private JTextField numItemsTextField, itemIDTextField, itemQuantityTextField, itemInfoTextField, orderSubtotalTextField;
    private JButton processItemButton, confirmItemButton, viewOrderButton, finishOrderButton, newOrderButton, exitButton;

    private int itemNumber = 1;
    private int totalNumItems = 0;
    private double priceTotal = 0.00;
    private ArrayList<Item> shoppingCart = new ArrayList<Item>();

    private double itemPriceRAM;
    private Item itemRAM;

    class Item
    {
        String ID;
        String description;
        double pricePerItem;
        int quantity;
        double discount;
        String totalPrice;
        String fullDescription;

        Item(String ID, String descr, double pricePer, int qnty,
             double discnt, String totalPrice, String fullDescr)
        {
            this.ID = ID;
            this.description = descr;
            this.pricePerItem = pricePer;
            this.quantity = qnty;
            this.discount = discnt;
            this.totalPrice = totalPrice;
            this.fullDescription = fullDescr;
        }

        public String getFullDescription()
        {
            return fullDescription;
        }
    }

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

    public void processItem() throws Exception
    {
        String numItemsStr = null;
        String itemID;
        String itemQuantityStr;
        int numItems;
        int itemQuantity;

        while (true)
        {
            try
            {
                numItemsStr = numItemsTextField.getText();
                itemID = itemIDTextField.getText();
                itemQuantityStr = itemQuantityTextField.getText();
            }
            catch (NullPointerException ex)
            {
                popUpMsg("Make sure to fill out all necessary boxes.", "Nile Dot Com - ERROR", 0);
                break;
            }

            try
            {
                numItems = Integer.parseInt(numItemsStr);
                itemQuantity = Integer.parseInt(itemQuantityStr);
            }
            catch (NumberFormatException ex)
            {
                popUpMsg("Make sure to fill all boxes correctly.", "Nile Dot Com - ERROR", 0);
                break;
            }

            if (numItems <= 0 || itemQuantity <= 0)
            {
                popUpMsg("Please enter a number greater than zero", "Nile Dot Com - ERROR", 0);
                break;
            }

            this.totalNumItems = numItems;
            numItemsTextField.setEnabled(false);
            checkInventory(itemID, itemQuantity);

            break;
        }
    }

    private void popUpMsg(String msg, String title, int type)
    {
        // Error msg
        if (type == 0)
        {
            JOptionPane.showMessageDialog(websiteFrame, msg, title, JOptionPane.ERROR_MESSAGE);
            itemIDTextField.setText("");
            itemQuantityTextField.setText("");
        }

        // Confirm item pop up
        else if (type == 1)
            JOptionPane.showMessageDialog(websiteFrame, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void checkInventory(String itemID, int itemQuantity)
    {
        boolean inDatabase = false;
        try (Scanner input = new Scanner(Paths.get("inventory.txt")))
        {
            while (input.hasNext())
            {
                String databaseLine = null;
                databaseLine = input.nextLine();
                String[] tempItemDetails = databaseLine.split(", ");

                if (tempItemDetails[0].equalsIgnoreCase(itemID))
                {
                    inDatabase = true;

                    if (tempItemDetails[2].equals("true"))
                    {
                        double itemPrice = Double.parseDouble(tempItemDetails[3]);
                        double subtotalPrice = itemPrice * itemQuantity;
                        int discountRate = checkForDiscount(itemQuantity);

                        if (discountRate != 0)
                        {
                            subtotalPrice = applyDiscount(subtotalPrice, discountRate);
                        }

                        // Holds info about item temporarily after user presses Process Item.
                        String itemInfo = itemID + " " + tempItemDetails[1] + " " + "$" +
                                String.format("%.2f", itemPrice) + " " + itemQuantity + " " +
                                discountRate + "%" + " " + "$" + String.format("%.2f", subtotalPrice);

                        itemInfoLabel.setText("Item #" + itemNumber + " info: ");
                        itemInfoTextField.setText(itemInfo);
                        itemPriceRAM = subtotalPrice;
                        processItemButton.setEnabled(false);
                        confirmItemButton.setEnabled(true);

                        itemRAM = new Item(itemID, tempItemDetails[1], itemPrice, itemQuantity,
                                (discountRate * 0.01), "$" +
                                String.format("%.2f", subtotalPrice), itemInfo);
                    }

                    else
                    {
                        popUpMsg("Sorry... that item (ID " + itemID + ") is out of stock, please try another item",
                                "Nile Dot Com - ERROR", 0);
                    }

                    break;
                }
            }
            if (!inDatabase)
                popUpMsg("Item ID " + itemID + " does not exist.", "Nile Dot Com - ERROR", 0);
        }
        catch (FileNotFoundException ex)
        {
            popUpMsg("Error, database not found", "Nile Dot Com - ERROR", 0);
        }
        catch (IOException ex)
        {
            popUpMsg("Unexpected error.", "Nile Dot Com - ERROR", 0);
        }
    }

    public void confirmItem()
    {
        if (shoppingCart.isEmpty())
        {
            viewOrderButton.setEnabled(true);
            finishOrderButton.setEnabled(true);
        }

        popUpMsg("Item #" + itemNumber + " accepted. Added to your cart.",
                "Nile Dot Com - Item Confirmed", 1);

        itemNumber++;
        shoppingCart.add(itemRAM);

        priceTotal += itemPriceRAM;
        orderSubtotalTextField.setText(String.format("$%.2f", priceTotal));
        updateLabelsForNextItem();
        confirmItemButton.setEnabled(false);
        processItemButton.setEnabled(true);

        // If the user reached the last item. Disable user from adding more items.
        if (itemNumber > totalNumItems)
        {
            itemIDTextField.setText("");
            itemIDTextField.setEnabled(false);
            itemIDLabel.setText("");
            itemQuantityTextField.setText("");
            itemQuantityTextField.setEnabled(false);
            itemQuantityLabel.setText("");

            processItemButton.setText("Process Item");
            processItemButton.setEnabled(false);
            confirmItemButton.setText("Confirm Item");
            confirmItemButton.setEnabled(false);
        }
    }

    public void viewOrder()
    {
        popUpMsg(getShoppingCartInfo(), "Nile Dot Com - Current Shopping Cart Status", 1);
    }

    public String getShoppingCartInfo()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shoppingCart.size(); i++)
            sb.append(i + 1).append(". ").append(shoppingCart.get(i).getFullDescription()).append("\n");

        return sb.toString();
    }

    public void finishOrder()
    {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        TimeZone timeZone = TimeZone.getDefault();
        String finalInvoice = createFinalInvoice(date, time, timeZone);

        popUpMsg(finalInvoice, "Nile Dot Com - Final Invoice", 1);
        saveTransaction(date, time, timeZone);
        newOrder();
    }

    public void saveTransaction(LocalDate date, LocalTime time, TimeZone timeZone)
    {
        String transactionID = generateTransactionID(date, time, timeZone);
        String zone = timeZone.getDisplayName(true, 0);
        String datePattern = "hh:mm:ss a";
        String timeStr = time.format(DateTimeFormatter.ofPattern(datePattern)) + " " + zone;
        String dateStr = date.getMonthValue() + "/" + date.getDayOfMonth() + "/" + date.getYear();

        try
        {
            FileWriter fw = new FileWriter("transaction.txt", true);
            BufferedWriter output = new BufferedWriter(fw);
            for (int i = 0; i < shoppingCart.size(); i++)
            {
                Item item = shoppingCart.get(i);
                output.write(transactionID + ", " + item.ID + ", " + item.description + ", "
                + item.pricePerItem + ", " + item.quantity + ", " + item.discount + ", "
                + item.totalPrice + ", " + dateStr + ", " + timeStr + "\n");
            }
            output.close();
        }
        catch (Exception ex)
        {
            popUpMsg("Error occurred trying to save your transaction.", "Nile Dot Com - ERROR", 0);
        }
    }
    
    public void newOrder()
    {
        itemNumber = 1;
        totalNumItems = 0;
        priceTotal = 0.00;
        shoppingCart.clear();
        clearAllTextFields();
        changeLabelNumbers();

        processItemButton.setEnabled(true);
        confirmItemButton.setEnabled(false);
        viewOrderButton.setEnabled(false);
        finishOrderButton.setEnabled(false);
    }

    public String createFinalInvoice(LocalDate date, LocalTime time, TimeZone timeZone)
    {
        String datePattern = "hh:mm:ss a";

        String invoice = "Date: " + date.getMonthValue() + "/" + date.getDayOfMonth() + "/"
                + date.getYear() + ", " + time.format(DateTimeFormatter.ofPattern(datePattern))
                + " " + timeZone.getDisplayName(true, 0) + "\n\n"
                + "Number of line items: " + shoppingCart.size() + "\n\n"
                + "Item# / ID / Title / Price / Qty / Disc % / Subtotal:\n\n"
                + getShoppingCartInfo() + "\n\n" + "Order subtotal: $" + String.format("%.2f", priceTotal)
                + "\n\n" + "Tax rate:     6%\n\nTax amount   $" + String.format("%.2f", (priceTotal * 0.06))
                + "\n\n" + "Order total:   $" + String.format("%.2f", (priceTotal + (priceTotal * 0.06)))
                + "\n\n" + "Thanks for shopping at Nile Dot Com!";

        return invoice;
    }

    public String generateTransactionID(LocalDate date, LocalTime time, TimeZone timeZone)
    {
        int day = date.getDayOfMonth(), month = date.getMonthValue(), year = date.getYear();
        int hour = time.getHour(), minute = time.getMinute();

        StringBuilder sb = new StringBuilder();
        // These statements are put in for better formatting so the transactionID will
        // always be the same length. ie: 05 & 15 instead of 5 & 15.
        if (day < 10)
            sb.append(0);
        sb.append(day);

        if (month < 10)
            sb.append(0);
        sb.append(month);

        sb.append(year);
        if (hour < 10)
            sb.append(0);
        sb.append(hour);

        if (minute < 10)
            sb.append(0);
        sb.append(minute);

        return sb.toString();
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
    }

    public void updateLabelsForNextItem()
    {
        itemIDLabel.setText("Enter item ID for Item #" + itemNumber + ": ");
        itemIDTextField.setText("");
        itemQuantityLabel.setText("Enter quantity for Item #" + itemNumber + ": ");
        itemQuantityTextField.setText("");
        orderSubtotalLabel.setText("Order subtotal for " + (itemNumber - 1) + " item(s): ");
        processItemButton.setText("Process Item #" + itemNumber);
        confirmItemButton.setText("Confirm Item #" + itemNumber);
    }

    public void clearAllTextFields()
    {
        numItemsTextField.setEnabled(true);
        numItemsTextField.setText("");
        itemIDTextField.setEnabled(true);
        itemIDTextField.setText("");
        itemQuantityTextField.setEnabled(true);
        itemQuantityTextField.setText("");
        itemInfoTextField.setText("");
        orderSubtotalTextField.setText("");
    }

    public void changeLabelNumbers()
    {
        itemIDLabel.setText("Enter item ID for Item #" + itemNumber + ": ");
        itemQuantityLabel.setText("Enter quantity for Item #" + itemNumber + ": ");
        itemInfoLabel.setText("Item #" + itemNumber + " info: ");
        orderSubtotalLabel.setText("Order subtotal for " + (itemNumber - 1) + " item(s): ");
        processItemButton.setText("Process Item #1");
        confirmItemButton.setText("Confirm Item #1");
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
        itemIDLabel = new JLabel("Enter item ID for Item #" + itemNumber +
                ": ", SwingConstants.RIGHT);
        itemIDLabel.setBounds(10,40, 230, 25);
        websitePanel.add(itemIDLabel);
    }

    public void setUpItemQuantityLabel()
    {
        itemQuantityLabel = new JLabel("Enter quantity for Item #" + itemNumber +
                ": ", SwingConstants.RIGHT);
        itemQuantityLabel.setBounds(10, 60, 230, 25);
        websitePanel.add(itemQuantityLabel);
    }

    public void setUpItemInfoLabel()
    {
        itemInfoLabel = new JLabel("Item #" + itemNumber + " info: ", SwingConstants.RIGHT);
        itemInfoLabel.setBounds(10, 80, 230, 25);
        websitePanel.add(itemInfoLabel);
    }

    public void setUpOrderSubtotalLabel()
    {
        orderSubtotalLabel = new JLabel("Order subtotal for " + totalNumItems +
                " item(s): ", SwingConstants.RIGHT);
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
        websitePanel.add(orderSubtotalTextField);
    }

    //******************** Setup for Buttons ********************

    public void setUpProcessItemButton()
    {
        processItemButton = new JButton("Process Item #" + itemNumber);

        Dimension tempCoords = processItemButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

        processItemButton.setBounds(10, 150, width, height);
        websitePanel.add(processItemButton);
    }

    public void setUpConfirmItemButton()
    {
        confirmItemButton = new JButton("Confirm Item #" + itemNumber);

        Dimension tempCoords = confirmItemButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

        confirmItemButton.setBounds(159, 150, width, height);

        confirmItemButton.setEnabled(false);
        websitePanel.add(confirmItemButton);
    }

    public void setUpViewOrderButton()
    {
        viewOrderButton = new JButton("View Order");

        Dimension tempCoords = viewOrderButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

        viewOrderButton.setBounds(311, 150, width, height);

        viewOrderButton.setEnabled(false);
        websitePanel.add(viewOrderButton);
    }

    public void setUpFinishOrderButton()
    {
        finishOrderButton = new JButton("Finish Order");

        Dimension tempCoords = finishOrderButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

        finishOrderButton.setBounds(428, 150, width, height);
        finishOrderButton.setEnabled(false);
        websitePanel.add(finishOrderButton);
    }

    public void setUpNewOrderButton()
    {
        newOrderButton = new JButton("New Order");

        Dimension tempCoords = newOrderButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

        newOrderButton.setBounds(554, 150, width, height);
        websitePanel.add(newOrderButton);
    }

    public void setUpExitButton()
    {
        exitButton = new JButton("Exit");

        Dimension tempCoords = exitButton.getPreferredSize();
        int width = (int)Math.round(tempCoords.getWidth());
        int height = (int)Math.round(tempCoords.getHeight());

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
                    confirmItem();
                }

                else if (actionEventObject == viewOrderButton)
                {
                    viewOrder();
                }

                else if (actionEventObject == finishOrderButton)
                {
                    finishOrder();
                }

                else if (actionEventObject == newOrderButton)
                {
                    newOrder();
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
