package hse.java.lectures.lecture6.tasks.queue;

import java.util.LinkedList;
import java.util.Queue;

public class BoundedBlockingQueue<T> {
    private final Queue<T> q = new LinkedList<T>();
    private int size = 0;
    private final int capacity;
    private final Object monitor;

    public BoundedBlockingQueue(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
            monitor = new Object();
        } else {
            throw new IllegalArgumentException("Capacity is zero");
        }
    }

    public void put(T item) {
        if (item == null) {
            throw new NullPointerException("Item is null");
        }
        synchronized (monitor) {
            while (size == capacity) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            q.add(item);
            size++;
            monitor.notifyAll();
        }
    }

    public T take() {
        synchronized (monitor) {
            while (size == 0) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            T value = q.remove();
            size--;
            monitor.notifyAll();
            return value;
        }
    }

    public int size() {
        synchronized (monitor) {
            monitor.notify();
            return size;
        }
    }

    public int capacity() {
        return capacity;
    }
}
