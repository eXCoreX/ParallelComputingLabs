/* package whatever; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;
import java.time.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/* Name of the class has to be "Main" only if the class is public. */
class Ideone
{
	public static void main(String[] args) {
        Ideone main = new Ideone();
        int storageSize = 4;
        int itemNumbers = 40;
        main.starter(storageSize, itemNumbers, 2, 5);
    }

    private void starter(int storageSize, int itemNumbers, int producers, int consumers) {
        Manager manager = new Manager(storageSize);
        
        for(int i = 0; i < producers; i++) {
        	new Producer(i * itemNumbers / producers, (i + 1) * itemNumbers / producers, manager);
        }
        
        for(int i = 0; i < consumers; i++) {
        	new Consumer((i + 1) * itemNumbers / consumers - i * itemNumbers / consumers, manager);
        }
    }
}

class Consumer implements Runnable {
    private final int itemCount;
    private final Manager manager;

    public Consumer(int itemCount, Manager manager) {
        this.itemCount = itemCount;
        this.manager = manager;

        new Thread(this).start();
    }

    @Override
    public void run() {
        for (int i = 0; i < itemCount; i++) {
            String item;
            try {
                manager.empty.acquire();
                Thread.sleep(10);
                manager.access.acquire();

                item = manager.storage.get(0);
                manager.storage.remove(0);
                System.out.println("Consumer " + Thread.currentThread().getId() + " took " + item);

                manager.access.release();
                manager.full.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Producer implements Runnable{
    private final int fromId;
    private final int toId;
    private final Manager manager;

    public Producer(int fromId, int toId, Manager manager) {
        this.fromId = fromId;
        this.toId = toId;
        this.manager = manager;

        new Thread(this).start();
    }

    @Override
    public void run() {
        for (int i = fromId; i < toId; i++) {
            try {
                manager.full.acquire();
                manager.access.acquire();

                manager.storage.add("item " + i);
                System.out.println("Producer " + Thread.currentThread().getId() + " added item " + i);

                manager.access.release();
                manager.empty.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Manager {

    public Semaphore access;
    public Semaphore full;
    public Semaphore empty;

    public ArrayList<String> storage = new ArrayList<>();

    public Manager(int storageSize) {
        access = new Semaphore(1);
        full = new Semaphore(storageSize);
        empty = new Semaphore(0);
    }
}