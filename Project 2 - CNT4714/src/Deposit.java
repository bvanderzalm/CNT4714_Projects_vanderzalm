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
}
