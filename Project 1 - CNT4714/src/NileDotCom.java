import javax.swing.*;

public class NileDotCom
{
    private static JPanel websitePanel;
    private static JFrame websiteFrame;

    private static JLabel numItems;
    private static JLabel itemID;
    private static JLabel itemQuantity;
    private static JLabel itemInfo;
    private static JLabel orderSubtotal;

    private static JTextField numItemsText;
    private static JTextField itemIDText;
    private static JTextField itemQuantityText;
    private static JTextField itemInfoText;
    private static JTextField orderSubtotalText;

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
        websiteFrame.add(websitePanel);

        websitePanel.setLayout(null);
    }

    public void setUpLabelInstructions()
    {

    }

    public void setUpUserTextFields()
    {

    }

    public void setUpButtons()
    {

    }

    public static void main(String [] args)
    {
        NileDotCom nile = new NileDotCom();
        nile.runWebsite();
    }
}
