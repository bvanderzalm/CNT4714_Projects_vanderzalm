/*  Name: Bradley Vanderzalm
    Course: CNT 4714 Fall 2021
    Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
    Due Date: October 3, 2021
 */

import java.util.Random;

public class Deposit implements Runnable
{
    // Created shared account since savingsAccount implements Buffer.
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
        // Note: the nextInt(...) lines are inside while loop so a
        //       thread doesn't have the same number each time.
        Random generator = new Random();

        try
        {
            while (true)
            {
                int cashAmount = generator.nextInt(250);

                // Thread shouldn't deposit $0 (requirements).
                if (cashAmount == 0)
                    cashAmount = 1;

                sharedAccount.depositCash(cashAmount, threadName);
                int sleepTime = generator.nextInt(350);

                // Thread shouldn't sleep for 0 amount of time either.
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
