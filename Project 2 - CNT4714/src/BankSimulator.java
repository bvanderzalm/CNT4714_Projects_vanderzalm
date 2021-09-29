/*  Name: Bradley Vanderzalm
    Course: CNT 4714 Fall 2021
    Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
    Due Date: October 3, 2021
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankSimulator
{
    public void run()
    {
        SavingsAccount account = new SavingsAccount();
        printTableHeaders();
        setUpAndStartThreads(account);
    }

    public void printTableHeaders()
    {
        System.out.println("Deposit Threads\t\t\t\tWithdrawal Threads\t\t\t        Balance");
        System.out.println("---------------\t\t\t\t------------------\t\t\t-----------------------");
    }

    public void setUpAndStartThreads(SavingsAccount account)
    {
        ExecutorService app = Executors.newFixedThreadPool(15);

        try
        {
            app.execute(new Deposit(account, "D1"));
            app.execute(new Deposit(account, "D2"));
            app.execute(new Deposit(account, "D3"));
            app.execute(new Deposit(account, "D4"));
            app.execute(new Deposit(account, "D5"));
            app.execute(new Deposit(account, "D6"));

            app.execute(new Withdraw(account, "W1"));
            app.execute(new Withdraw(account, "W2"));
            app.execute(new Withdraw(account, "W3"));
            app.execute(new Withdraw(account, "W4"));
            app.execute(new Withdraw(account, "W5"));
            app.execute(new Withdraw(account, "W6"));
            app.execute(new Withdraw(account, "W7"));
            app.execute(new Withdraw(account, "W8"));
            app.execute(new Withdraw(account, "W9"));
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        // Not needed since it's infinite loop anyway?
//        app.shutdown();

//        Deposit d1 = new Deposit(account, "d1");
//        Withdraw w1 = new Withdraw(account, "W1");
    }

    public static void main(String [] args)
    {
        BankSimulator bankSim = new BankSimulator();
        bankSim.run();
    }
}
