import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MyProblem {
    public static int sumTimes;
    public static HashMap<Integer, Output> outputs = new HashMap<>(); //Global HashMap with the Output objects.

    public static void main(String[] args) throws InterruptedException {
        int numThreads = Integer.parseInt(args[0]);
        sumTimes = Integer.parseInt(args[1]);

        List<Thread> threadList = new ArrayList<>(numThreads); //ArrayList for the created Threads.

        //Create and start Threads. As much as selected by the argument value.
        for (int i = 0; i < numThreads; ++i) {
            threadList.add(i, new Thread(new MyThread(), Integer.toString(i)));
            threadList.get(i).start();
        }

        //Randomly sleep main thread
        Random rand = new Random();
        Thread.sleep(rand.nextInt(1000));

        //Interrupt threads after main sleep
        for (Thread t : threadList) {
            t.interrupt();
            MyProblem.outputs.put(Integer.parseInt(t.getName()), new Output(System.currentTimeMillis()));
        }

        //Check threads end
        for (Thread t : threadList) { //For each thread, wait until is ended.
            try {
                t.join(); //join() is a function that waits until Thread t is finished.
                System.out.println("T" + t.getName() + outputs.get(Integer.parseInt(t.getName())).toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //Then, this foreach ends when every thread is ended.

        System.out.println("\nPI=3.1415926535897932384626433832795028");

        //Print final message
        System.out.println("\nProgram of exercise 4 has terminated.");
    }
}

class MyThread implements Runnable {
    private ThreadLocal<Double> pi = ThreadLocal.withInitial(() -> 0.0); //Setting up the initial value

    //Code going to be executed by the thread.
    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() + " started. MySum=" + pi.get());

        while (!Thread.currentThread().isInterrupted()) {
            for (int i = 3; i < 1000000; i += 2) {
                pi.set(pi.get() - 1.0 / i);
                i += 2;
                pi.set(pi.get() + 1.0 / i);
            }
            pi.set(pi.get() + 1.0);
            pi.set(pi.get() * 4.0);
        }

        if (Thread.currentThread().isInterrupted()) {
            MyProblem.outputs.get(Integer.parseInt(Thread.currentThread().getName())).setInterrupted(System.currentTimeMillis());
        }

        System.out.println("Thread " + Thread.currentThread().getName() + " finished. MySum=" + pi.get());
    }
}

//Class used for storing each Thread timing.
class Output {
    private long sentInterrupt, interrupted;

    Output(long sentInterrupt) {
        this.sentInterrupt = sentInterrupt;
        this.interrupted = 0;
    }

    public void setInterrupted(long interrupted) {
        this.interrupted = interrupted;
    }

    @Override
    public String toString() {
        String info = "sentInterrupt: " + sentInterrupt + " - interrupted: " + interrupted;
        String results = "\n\ts-i: " + (interrupted - sentInterrupt);
        return info + results;
    }
}

/*
//EXERCISE B)
//Custom Thread class implementing Runnable.
class MyThread implements Runnable {
    private Integer mySum = 0;

    //Code going to be executed by the thread.
    @Override
    public void run() {
        for (int i = 0; i < MyProblem.sumTimes; i++) {
            System.out.println("Thread " + Thread.currentThread().getName() + " started. MySum=" + mySum);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mySum += i;
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " finished. MySum=" + mySum);
    }
}
*/

/*
//EXERCISE C)
class MyThread implements Runnable {
    private ThreadLocal<Integer> mySum = ThreadLocal.withInitial(() -> 0); //Setting up the initial value

    //Code going to be executed by the thread.
    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getName() + " started. MySum=" + mySum.get());
        for (int i = 0; i < MyProblem.sumTimes; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mySum.set(mySum.get() + i);
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " finished. MySum=" + mySum.get());
    }
}*/
