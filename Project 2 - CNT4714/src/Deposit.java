/*  Name: Bradley Vanderzalm
    Course: CNT 4714 Fall 2021
    Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
    Due Date: October 3, 2021
 */

import java.util.Random;

public class Deposit extends Thread
{
    private Buffer sharedAccount;
    private String threadName;

    // Constructor
    public Deposit(Buffer account, String name)
    {
        this.sharedAccount = account;
        this.threadName = name;
    }

    public void run()
    {
        // Generate random number for cash deposited and sleep time.
        Random generator = new Random();

        try
        {
            while (true)
            {
                int cashAmount = generator.nextInt(250);
                // Thread shouldn't deposit $0
                if (cashAmount == 0)
                    cashAmount = 1;

                sharedAccount.depositCash(cashAmount, threadName);
                int sleepTime = generator.nextInt(350);
                if (sleepTime == 0)
                    sleepTime = 1;

                Thread.sleep(sleepTime);
            }
        }

        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }
}
