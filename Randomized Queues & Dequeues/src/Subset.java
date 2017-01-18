import edu.princeton.cs.algs4.StdIn;

/**
 * Created by MengqinGong on 9/20/16.
 */
public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        String[] s = StdIn.readAllStrings();

        RandomizedQueue<String> rq = new RandomizedQueue<>();

        for (String string : s) {
            rq.enqueue(string);
        }

        for (int i = 0; i < k; i++) {
            System.out.println(rq.dequeue());
        }


    }
}
