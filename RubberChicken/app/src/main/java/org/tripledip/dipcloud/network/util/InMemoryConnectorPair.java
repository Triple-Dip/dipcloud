package org.tripledip.dipcloud.network.util;

import org.tripledip.dipcloud.network.contract.Connector;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ben on 3/7/15.
 */
public class InMemoryConnectorPair<T> {

    private Random rand = new Random();

    protected Connector<T> aSendToBConnector;
    protected Connector<T> bSendToAConnector;

    private int jankMillis = 0;

    public InMemoryConnectorPair() {
        MessageQueue<T> aSendToBQueue = new MessageQueue<>();
        MessageQueue<T> bSendToAQueue = new MessageQueue<>();

        aSendToBConnector = new InMemoryConnector<>(bSendToAQueue, aSendToBQueue);
        bSendToAConnector = new InMemoryConnector<>(aSendToBQueue, bSendToAQueue);
    }

    public int getJankMillis() {
        return jankMillis;
    }

    public void setJankMillis(int jankMillis) {
        this.jankMillis = jankMillis;
    }

    public Connector<T> getASendToB() {
        return aSendToBConnector;
    }

    public Connector<T> getBSendToA() {
        return bSendToAConnector;
    }

    protected void awaitRandomJank() throws InterruptedException {
        if (jankMillis <=  0) {
            return;
        }

        int randomJank = rand.nextInt(jankMillis);
        Thread.sleep(randomJank);
    }

    private class InMemoryConnector<T> implements Connector<T> {

        private final MessageQueue<T> readQueue;
        private final MessageQueue<T> writeQueue;

        public InMemoryConnector(MessageQueue<T> readQueue, MessageQueue<T> writeQueue) {
            this.readQueue = readQueue;
            this.writeQueue = writeQueue;
        }

        @Override
        public T readNext() throws InterruptedException {
            T message = readQueue.readNext();

            return message;
        }

        @Override
        public void write(T outData) {
            writeQueue.write(outData);
        }
    }

    protected class MessageQueue<T> {
        private final Queue<T> written;
        private final Lock lock;
        private final Condition notEmpty;

        public MessageQueue() {
            this.written = new ConcurrentLinkedQueue<>();
            this.lock = new ReentrantLock(false);
            this.notEmpty = lock.newCondition();
        }

        public T readNext() throws InterruptedException {

            T message = written.poll();
            if (null != message) {
                awaitRandomJank();
                return message;
            }

            lock.lockInterruptibly();
            try {
                while (null == message) {
                    notEmpty.await();
                    message = written.poll();
                }
            } finally {
                lock.unlock();
            }

            awaitRandomJank();
            return message;
        }

        public void write(T outData) {
            written.add(outData);
            lock.lock();
            notEmpty.signal();
            lock.unlock();
        }
    }
}
