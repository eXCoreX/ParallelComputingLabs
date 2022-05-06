using System.Threading;

class Program
{
    public static void Main(string[] args)
    {
        new Program().RealMain();
    }

    private void RealMain()
    {
        var arr = new int[100000000];
        var random = new Random();

        for (int i = 0; i < arr.Length; i++)
        {
            arr[i] = random.Next();
        }

        FindMain(arr, 1);
        FindMain(arr, 2);
        FindMain(arr, 3);
        FindMain(arr, 8);
        FindMain(arr, 9);
    }

    private void FindMain(int[] arr, int threadsNum)
    {
        var startTime = DateTime.Now;

        var threads = new Thread[threadsNum];

        var min = int.MaxValue;
	var minIdx = -1;
        object _lock = new();

        for (int threadIdx = 0; threadIdx < threads.Length; threadIdx++)
        {
            var threadIdxLocal = threadIdx;
            threads[threadIdx] = new Thread(() =>
            {
                for (int i = arr.Length * threadIdxLocal / threadsNum; i < arr.Length * (threadIdxLocal + 1) / threadsNum; i++)
                {
                    if (arr[i] < min)
                    {
                        lock (_lock)
                        {
                            if (arr[i] < min)
                            {
                                min = arr[i];
				minIdx = i;
                            }
                        }
                    }
                }
            });

            threads[threadIdx].Start();
        }

        foreach (var item in threads)
        {
            item.Join();
        }

        var endTime = DateTime.Now;

        Console.WriteLine($"{threadsNum} threads: min is {min} (index {minIdx}): {(endTime - startTime).TotalMilliseconds}ms");
    }
}