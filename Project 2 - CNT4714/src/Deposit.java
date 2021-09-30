/*  Name: Bradley Vanderzalm
    Course: CNT 4714 Fall 2021
    Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
    Due Date: October 3, 2021
 */

public class Deposit implements Runnable
{
    private SavingsAccount sharedAccount;
    private String threadName;

    public Deposit(SavingsAccount account, String name)
    {
        this.sharedAccount = account;
        this.threadName = name;
    }

    public void run()
    {
        int cashAmount = 250; // Random between 1 to 250
        int sleepTime = 8000; // random for a couple milliseconds

        try
        {
            while (true)
            {
                Thread.sleep(sleepTime);
                sharedAccount.deposit(cashAmount, threadName);
            }
        }

        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }
}
