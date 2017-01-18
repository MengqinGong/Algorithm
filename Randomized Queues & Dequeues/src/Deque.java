import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by MengqinGong on 9/19/16.
 */
public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return first == null || last == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {

        if (item == null) throw new NullPointerException("Item to be added is NULL");

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (oldFirst != null) oldFirst.prev = first;
        first.prev = null;

        if (isEmpty()) last = first;

        size++;
    }

    public void addLast(Item item) {

        if (item == null) throw new NullPointerException("Item to be added is NULL");

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;

        if (isEmpty()) first = last;
        else if (oldLast != null) oldLast.next = last;

        size++;

    }

    public Item removeFirst() {

        if (isEmpty()) throw new NoSuchElementException("Deque is empty");

        Item item = first.item;
        first = first.next;
        if (first != null) first.prev = null;
        if (isEmpty()) last = null;
        size--;
        return item;
    }

    public Item removeLast() {

        if (isEmpty()) throw new NoSuchElementException("Deque is empty");

        Item item = last.item;
        last = last.prev;
        if (last != null) last.next = null;
        if (isEmpty()) first = null;
        size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new dequeIterator();
    }

    private class dequeIterator implements Iterator<Item> {

        private Node current = first;

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
        Deque<Integer> deque1 = new Deque<>();

        deque1.addFirst(0);
        deque1.addFirst(1);
        deque1.addFirst(2);
        deque1.addFirst(3);
        deque1.addFirst(4);
        deque1.addFirst(5);
        deque1.addFirst(6);
        deque1.addFirst(7);
        System.out.println(deque1.removeLast());
        deque1.addFirst(9);
        deque1.addFirst(10);
        System.out.println(deque1.removeLast());

    }

}
