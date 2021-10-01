/*  Name: Bradley Vanderzalm
    Course: CNT 4714 Fall 2021
    Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
    Due Date: October 3, 2021
 */

public interface Buffer
{
    public void withdrawCash(int amount, String threadName);
    public void depositCash(int amount, String threadName);
}
