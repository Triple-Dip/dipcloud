package org.tripledip.dipcloud.network.behavior;

import org.tripledip.dipcloud.network.contract.Connector;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ben on 3/1/15.
 */
public class OutBox<T> {

    private final Connector<T> connector;
    private final Queue<T> toSend;
    private final Lock lock;
    private final Condition notEmpty;

    public OutBox(Connector<T> connector) {
        this.connector = connector;
        this.toSend = new ConcurrentLinkedQueue<>();
        this.lock = new ReentrantLock(false);
        this.notEmpty = lock.newCondition();
    }

    public void add(T item) {
        toSend.add(item);

        lock.lock();
        notEmpty.signal();
        lock.unlock();
    }

    public boolean sendNext() throws InterruptedException, IOException {
        T next = toSend.poll();

        if (null == next) {
            return false;
        }

        connector.write(next);
        return true;
    }

    public int sendAll() throws InterruptedException, IOException {
        int sent = 0;
        while (sendNext()) {
            sent++;
        }
        return sent;
    }

    public Runnable getRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                sendForever();
            }
        };
    }

    private void sendForever() {
        while (true) {
            try {
                this.sendAll();
                lock.lockInterruptibly();
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                    return;
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                return;
            } catch (IOException e) {
                return;
            }
        }
    }
}
