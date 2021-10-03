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
        // Create shared/synced bank account.
        SavingsAccount bankAccount = new SavingsAccount();

        // Create new thread pool with 15 threads (6 Deposit, 9 Withdraw)
        ExecutorService app = Executors.newFixedThreadPool(15);

        try
        {
            // Try to start 6 Deposit threads,
            // passing the shared/synced bank account, and the thread name
            Deposit D1 = new Deposit(bankAccount, "D1");
            app.execute(D1);
            Deposit D2 = new Deposit(bankAccount, "D2");
            app.execute(D2);
            Deposit D3 = new Deposit(bankAccount, "D3");
            app.execute(D3);
            Deposit D4 = new Deposit(bankAccount, "D4");
            app.execute(D4);
            Deposit D5 = new Deposit(bankAccount, "D5");
            app.execute(D5);
            Deposit D6 = new Deposit(bankAccount, "D6");
            app.execute(D6);

            // Try to start 9 Withdraw threads,
            // passing the shared/synced bank account, and the thread name.
            Withdraw W1 = new Withdraw(bankAccount, "W1");
            app.execute(W1);
            Withdraw W2 = new Withdraw(bankAccount, "W2");
            app.execute(W2);
            Withdraw W3 = new Withdraw(bankAccount, "W3");
            app.execute(W3);
            Withdraw W4 = new Withdraw(bankAccount, "W4");
            app.execute(W4);
            Withdraw W5 = new Withdraw(bankAccount, "W5");
            app.execute(W5);
            Withdraw W6 = new Withdraw(bankAccount, "W6");
            app.execute(W6);
            Withdraw W7 = new Withdraw(bankAccount, "W7");
            app.execute(W7);
            Withdraw W8 = new Withdraw(bankAccount, "W8");
            app.execute(W8);
            Withdraw W9 = new Withdraw(bankAccount, "W9");
            app.execute(W9);

        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void main(String [] args)
    {
        BankSimulator bankSim = new BankSimulator();
        bankSim.run();
    }
}
