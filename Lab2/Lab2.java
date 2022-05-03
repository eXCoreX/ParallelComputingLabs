/* package whatever; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;
import java.time.*;

/* Name of the class has to be "Main" only if the class is public. */
class Ideone
{
	public static void main (String[] args) throws java.lang.Exception
	{
		(new Ideone()).RealMain();
	}
	
    private void RealMain() throws java.lang.Exception
    {
        int[] arr = new int[100000000];
        Random random = new Random();

        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = random.nextInt();
        }

        FindMain(arr, 1);
        FindMain(arr, 2);
        FindMain(arr, 3);
        FindMain(arr, 8);
        FindMain(arr, 9);
    }
    
    private int min = Integer.MAX_VALUE;

    private void FindMain(int[] arr, int threadsNum) throws java.lang.Exception
    {
        LocalTime startTime = LocalTime.now();

        Thread[] threads = new Thread[threadsNum];
        
        min = Integer.MAX_VALUE;

        Object _lock = new Object();

        for (int threadIdx = 0; threadIdx < threads.length; threadIdx++)
        {
            var threadIdxLocal = threadIdx;
            threads[threadIdx] = new Thread(() ->
            {
                for (int i = arr.length * threadIdxLocal / threadsNum; i < arr.length * (threadIdxLocal + 1) / threadsNum; i++)
                {
                    if (arr[i] < min)
                    {
                        synchronized (_lock)
                        {
                            if (arr[i] < min)
                            {
                                min = arr[i];
                            }
                        }
                    }
                }
            });

            threads[threadIdx].run();
        }

        for (Thread item : threads)
        {
            item.join();
        }

       LocalTime endTime = LocalTime.now();

       System.out.println(threadsNum + " threads: min is " + min + ": "+ Duration.between(startTime, endTime).toMillis() + "ms");
    }
}