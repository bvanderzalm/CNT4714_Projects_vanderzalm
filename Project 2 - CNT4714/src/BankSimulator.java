/*  Name: Bradley Vanderzalm
    Course: CNT 4714 Fall 2021
    Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
    Due Date: October 3, 2021
 */

public class BankSimulator
{
    public void run()
    {
        printTableHeaders();
        setUpAndStartThreads();
    }

    public void printTableHeaders()
    {
        System.out.println("Deposit Threads\t\t\t\tWithdrawal Threads\t\t\t        Balance");
        System.out.println("---------------\t\t\t\t------------------\t\t\t-----------------------");
    }

    public void setUpAndStartThreads()
    {
        // Create bank account that'll be shared for all threads
        SavingsAccount account = new SavingsAccount();

        // Start Deposit Threads
        Deposit D1 = new Deposit(account, "D1");
        D1.start();
        Deposit D2 = new Deposit(account, "D2");
        D2.start();
        Deposit D3 = new Deposit(account, "D3");
        D3.start();
        Deposit D4 = new Deposit(account, "D4");
        D4.start();
        Deposit D5 = new Deposit(account, "D5");
        D5.start();
        Deposit D6 = new Deposit(account, "D6");
        D6.start();

        // Start Withdraw Threads
        Withdraw W1 = new Withdraw(account, "W1");
        W1.start();
        Withdraw W2 = new Withdraw(account, "W2");
        W2.start();
        Withdraw W3 = new Withdraw(account, "W3");
        W3.start();
        Withdraw W4 = new Withdraw(account, "W4");
        W4.start();
        Withdraw W5 = new Withdraw(account, "W5");
        W5.start();
        Withdraw W6 = new Withdraw(account, "W6");
        W6.start();
        Withdraw W7 = new Withdraw(account, "W7");
        W7.start();
        Withdraw W8 = new Withdraw(account, "W8");
        W8.start();
        Withdraw W9 = new Withdraw(account, "W9");
        W9.start();

    }

    public static void main(String [] args)
    {
        BankSimulator bankSim = new BankSimulator();
        bankSim.run();
    }
}
