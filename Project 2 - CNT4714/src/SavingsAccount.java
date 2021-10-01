/*  Name: Bradley Vanderzalm
    Course: CNT 4714 Fall 2021
    Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
    Due Date: October 3, 2021
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SavingsAccount implements Buffer
{
    private int balance = 0;
    private Lock accessLock = new ReentrantLock();
    private Condition canDeposit = accessLock.newCondition();
    private Condition canWithdraw = accessLock.newCondition();

    public void withdrawCash(int amount, String threadName)
    {
        accessLock.lock();

        try
        {
            // Check to see if there is enough money in the account to make the withdrawal.
            if ((balance - amount) >= 0)
            {
                balance = balance - amount;
                System.out.print("\t\t\t\t\t\tThread " + threadName + " withdraws $" + amount);
                System.out.print("\t\t\t(-) Balance is $" + balance);
                System.out.println();
//                System.out.println("Withdrew $" + amount + " from account with thread " + threadName + ". Balance is $" + balance);
            }

            // If there isn't enough cash in the account, block the withdrawal.
            else
            {
//                System.out.println("BLOCKED not enough cash. Balance is $" + balance + " can't withdraw $" + amount);
                System.out.print("\t\t\t\t\t\tThread " + threadName + " withdraws $" + amount);
                System.out.print("\t\t\t(******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!!");
                System.out.println();
                canDeposit.signalAll();
                canWithdraw.await();
            }
        }

        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        finally
        {
            accessLock.unlock();
        }
    }

    public void depositCash(int amount, String threadName)
    {
        accessLock.lock();

        try
        {
            // Add cash into account regardless of the current balance.
            balance = balance + amount;
//            System.out.println("Deposited $" + amount + " with thread " + threadName + ". Balance is $" + balance);
            System.out.print("Thread " + threadName + " deposits $" + amount);
            System.out.print("\t\t\t\t\t\t\t\t\t(+) Balance is $" + balance);
            System.out.println();
            canWithdraw.signalAll();
            canDeposit.await();
        }

        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }

        finally
        {
            accessLock.unlock();
        }
    }
}
