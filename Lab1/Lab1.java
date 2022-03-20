/* package whatever; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class Ideone
{
	public static void main (String[] args) throws java.lang.Exception
	{
        BreakThread breakThread = new BreakThread();

        new WorkerThread(1, 2, breakThread).start();
        new WorkerThread(2, 6, breakThread).start();
        new WorkerThread(3, 9, breakThread).start();

        new Thread(breakThread).start();
	}
}

class BreakThread implements Runnable {
    private boolean stopSignalled = false;

    @Override
    public void run() {
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stopSignalled = true;
    }

    public synchronized boolean isStopSignalled() {
        return stopSignalled;
    }
}

class WorkerThread extends Thread {
    private final int id;
    private final int step;
    private final BreakThread breakThread;

    public WorkerThread(int id, int step, BreakThread breakThread) {
        this.id = id;
        this.step = step;
        this.breakThread = breakThread;
    }

    @Override
    public void run() {
        long sum = 0;
        long current = 0;

        while (!breakThread.isStopSignalled()) {
            current += step;
            sum += current;
        }

        System.out.println("Thread " + id + ": Sum from 0" + " to " + current + " by " + step + " (" + current / step + " elements) is " + sum);
    }
}