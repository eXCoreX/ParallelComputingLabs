using System.Threading;

class Program
{
    private bool stopSignalled = false;

    public static void Main(string[] args)
    {
        new Program().RealMain();
    }

    private void RealMain()
    { 
        new Thread(() =>
        {
            ComputeSum(2);
        }).Start();

        new Thread(() =>
        {
            ComputeSum(6);
        }).Start();

        new Thread(() =>
        {
            ComputeSum(9);
        }).Start();

        new Thread(() =>
        {
            Thread.Sleep(2000);
            stopSignalled = true;
        }).Start();
    }

    private void ComputeSum(int step)
    {
        long sum = 0;
        long current = 0;

        while (!stopSignalled)
        {
            current += step;
            sum += current;
        }

        Console.WriteLine($"Thread {Environment.CurrentManagedThreadId}: Sum from 0 to {current} by {step} ({current / step} elements) is {sum}");
    }
}