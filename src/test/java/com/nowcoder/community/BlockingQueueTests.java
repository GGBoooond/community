package com.nowcoder.community;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author wxx
 * @version 1.0
 */
public class BlockingQueueTests {
    public static void main(String[] args) {
            BlockingQueue<Integer> queue=new ArrayBlockingQueue<>(10);
            new Thread(new Producter(queue)).start();
            new Thread(new Consumer(queue)).start();
            new Thread(new Consumer(queue)).start();
            new Thread(new Consumer(queue)).start();
        }
    }


class Producter implements Runnable{
    private BlockingQueue<Integer> blockingQueue=null;
    Producter(BlockingQueue<Integer> blockingQueue){
        this.blockingQueue=blockingQueue;
    }

    @Override
    public void run() {
            for(int i=0;i<100;i++){
                try {
                    Thread.sleep(20);
                    blockingQueue.put(i);
                    System.out.println(Thread.currentThread().getId()+"当前进程正在生产"+blockingQueue.size());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

    }
}
class Consumer implements Runnable{
    private BlockingQueue<Integer> blockingQueue=null;
    Consumer(BlockingQueue<Integer> blockingQueue){
        this.blockingQueue=blockingQueue;
    }


    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(new Random().nextInt(1000));
                blockingQueue.remove();
                System.out.println(Thread.currentThread().getId()+"当前进程正在消费"+blockingQueue.size());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}