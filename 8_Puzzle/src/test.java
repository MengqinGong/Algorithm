import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by MengqinGong on 9/30/16.
 */
public class test {

    public Iterable<Integer> whatever() {
        ArrayList<Integer> s = new ArrayList<>(3);
        System.out.println("Initial size of s: " + s.size());
        s.add(3, 3);
        s.add(2, 2);
        s.add(1, 1);
        return s;
    }

    public static void main(String[] args) {
        test t = new test();

        for (int num : t.whatever()) System.out.println(num);
    }
}
