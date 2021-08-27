import javax.swing.*;

public class NileDotCom
{
    private static JPanel websitePanel;
    private static JFrame websiteFrame;

    private static JLabel numItemsLabel;
    private static JLabel itemIDLabel;
    private static JLabel itemQuantityLabel;
    private static JLabel itemInfoLabel;
    private static JLabel orderSubtotalLabel;

    private static JTextField numItemsTextField;
    private static JTextField itemIDTextField;
    private static JTextField itemQuantityTextField;
    private static JTextField itemInfoTextField;
    private static JTextField orderSubtotalTextField;

    private static JButton processItemButton;
    private static JButton confirmItemButton;
    private static JButton viewOrderButton;
    private static JButton finishOrderButton;
    private static JButton newOrderButton;
    private static JButton exitButton;

    public void runWebsite()
    {
        openPrimaryGUI();
        setUpLabelInstructions();
        setUpUserTextFields();
        setUpButtons();
        websiteFrame.setVisible(true);
    }

    public void openPrimaryGUI()
    {
        websitePanel = new JPanel();
        websiteFrame = new JFrame();
        websiteFrame.setSize(800, 400);
        websiteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        websiteFrame.setTitle("Nile Dot Com - Bradley Vanderzalm");
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

    public static void main(String [] args)
    {
        NileDotCom nile = new NileDotCom();
        nile.runWebsite();
    }
}
