/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package buffer;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 *
 * @author jacopo.careddu
 */
public class Buffer {

    private static final BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(1);
    private static final Random random = new Random();

    public static void main(String[] args) {
        Thread producerThread = new Thread(ProducerConsumerExample::producer);
        Thread consumerThread1 = new Thread(ProducerConsumerExample::consumer1);
        Thread consumerThread2 = new Thread(ProducerConsumerExample::consumer2);

        producerThread.start();
        consumerThread1.start();
        consumerThread2.start();
    }

    private static void producer() {
        try {
            while (true) {
                int randomNumber = random.nextInt(10) + 1;
                buffer.put(randomNumber);
                System.out.println("Producer produced: " + randomNumber);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void consumer1() {
        try {
            while (true) {
                int number = buffer.take();
                if (number >= 1 && number <= 5) {
                    System.out.println("Consumer 1 consumed: " + number);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void consumer2() {
        try {
            while (true) {
                int number = buffer.take();
                if (number >= 6 && number <= 10) {
                    System.out.println("Consumer 2 consumed: " + number);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

}
