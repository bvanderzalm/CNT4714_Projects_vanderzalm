/*  Name: Bradley Vanderzalm
    Course: CNT 4714 Fall 2021
    Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
    Due Date: October 3, 2021
 */

import java.util.Random;

public class Withdraw implements Runnable
{
    // Created shared account since savingsAccount implements Buffer.
    private Buffer sharedAccount;
    private String threadName;

    // Constructor
    public Withdraw(Buffer account, String name)
    {
        this.sharedAccount = account;
        this.threadName = name;
    }

    public void run()
    {
        // Generate random number for cash withdrawn
        Random generator = new Random();

        while (true)
        {
            int cashAmount = generator.nextInt(50);

            // Thread shouldn't withdraw $0 (requirements).
            if (cashAmount == 0)
                cashAmount = 1;

            // Withdraw cash from the account and yield the thread.
            sharedAccount.withdrawCash(cashAmount, threadName);
            Thread.yield();
        }
    }
}

