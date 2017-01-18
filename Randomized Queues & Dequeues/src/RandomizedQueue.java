import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by MengqinGong on 9/19/16.
 */


public class RandomizedQueue<Item> implements Iterable<Item> {
    private Node first;
    private int size;

    private class Node {
        Item item;
        Node next;
    }

    public RandomizedQueue() {
        first = null;
        size = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;

        size++;
    }

    private ReverseNode randomNode() {
        int n = StdRandom.uniform(size);
        ;
        Item item;
        Node prev;

        if (n == 0) {
            item = first.item;
            prev = null;
        } else {
            prev = first;
            while (n > 1) {
                prev = prev.next;
                n--;
            }
            item = prev.next.item;
        }

        ReverseNode reverse = new ReverseNode();
        reverse.item = item;
        reverse.prev = prev;

        return reverse;

    }

    private class ReverseNode {
        Item item;
        Node prev;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        ReverseNode rvNode = randomNode();
        Item item = rvNode.item;
        if (rvNode.prev == null) {
            first = first.next;
        } else {
            rvNode.prev.next = rvNode.prev.next.next;
        }
        size--;
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        ReverseNode sample = randomNode();
        return sample.item;
    }

    public Iterator<Item> iterator() {
        return new randomQueueIterator();
    }

    private class randomQueueIterator implements Iterator<Item> {

        private RandomizedQueue<Item> shuffle() {
            int n = size;
            Item[] a = (Item[]) new Object[n];
            Node start = first;
            while (n > 0) {
                a[size - n] = start.item;
                start = start.next;
                n--;
            }

            StdRandom.shuffle(a);

            RandomizedQueue<Item> newQueue = new RandomizedQueue<>();

            while (n < size) {
                newQueue.enqueue(a[n]);
                n++;
            }

            return newQueue;
        }

        RandomizedQueue<Item> shuffled = shuffle();

        private Node current = shuffled.first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

    }

    public static void main(String[] args) {

        RandomizedQueue<Integer> rq = new RandomizedQueue<>();

        System.out.println(rq.isEmpty());

        for (int i = 0; i < 10; i++) {
            rq.enqueue(i);
            System.out.println("size: " + rq.size());
        }

        System.out.println(rq.sample());

        System.out.println("size: " + rq.size());


        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());

        System.out.println("size: " + rq.size());

        for (int element : rq) {
            System.out.println(element);
        }

    }


}
