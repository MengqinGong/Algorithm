import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

/**
 * Created by MengqinGong on 10/6/16.
 */
public class PointSET {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private SET<Node> pointSet;
    private Node root;

    private class Node implements Comparable<Node> {
        private Point2D point;
        private Node left, right;
        private boolean color;
        public int compareTo(Node that) {
            if (that == null) throw new NullPointerException("No compare to a NULL point");
            return this.point.compareTo(that.point);
        }
        public Node(Point2D point, boolean color) {
            this.point = point;
            this.color = color;
        }
    }

    public PointSET() {
        pointSet = new SET<>();
        root = null;
    }

    public boolean isEmpty() {
        return pointSet.size() == 0;
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("Inserted a NULL point");
        if (contains(p)) return;

        root = put(root, p);
        root.color = BLACK;
    }

    private Node put(Node h, Point2D p) {
        if (h == null) {
            Node toBeAdded = new Node(p, RED);
            pointSet.add(toBeAdded);
            return toBeAdded;
        }

        int cmp = p.compareTo(h.point);
        if (cmp > 0) h.right = put(h.right, p);
        if (cmp < 0) h.left = put(h.left, p);
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h);
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left)  &&  isRed(h.right))     flipColor(h);

        return h;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("No NULL input please");
        Node current = root;
        while (current != null) {
            int cmp = p.compareTo(current.point);
            if (cmp == 0) return true;
            if (cmp > 0) current = current.right;
            if (cmp < 0) current = current.left;
        }
        return false;
    }


    public void draw() {
        for (Node node : pointSet) {
            node.point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("Rectangular passed in is NULL");
        SET<Point2D> pointsInside = new SET<>();
        for (Node node : pointSet) {
            if (rect.contains(node.point)) pointsInside.add(node.point);
        }
        return pointsInside;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("Please input a valid point");
        Point2D nearestP = null;
        if (root != null) {
            nearestP = root.point;
            double minDistance = root.point.distanceSquaredTo(p);
            for (Node node : pointSet) {
                if (node.point.distanceSquaredTo(p) < minDistance) {
                    nearestP = node.point;
                    minDistance = node.point.distanceSquaredTo(p);
                }
            }
        }
        return nearestP;
    }

    private boolean isRed(Node h) {
        if (h == null) return false;
        return h.color;
    }

    private Node rotateLeft(Node h) {
        assert (h != null) && isRed(h.right);
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColor(Node h) {
        assert (h != null) && (h.right != null) && (h.left != null);
        assert !isRed(h) && isRed(h.right) && isRed(h.left);
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    public static void main(String[] args) {
        Point2D p1 = new Point2D(0, 0);
        Point2D p2 = new Point2D(0.4, 0.3);
        Point2D p3 = new Point2D(0.1, 0.4);
        Point2D p4 = new Point2D(0.6, 0.5);
        Point2D p5 = new Point2D(0.8, 0.6);

        PointSET points = new PointSET();
        System.out.println(points.isEmpty());
        System.out.println(points.size());

        points.insert(p2);
        points.insert(p3);
        points.draw();

        points.insert(p1);

        points.insert(p4);
        points.insert(p5);

        System.out.println(points.isEmpty());
        System.out.println(points.size());

        points.draw();

        RectHV rec = new RectHV(0.4, 0.3, 0.8, 0.6);

        rec.draw();

        for (Point2D point : points.range(rec)) {
            System.out.println(point);
        }

        Point2D p6 = new Point2D(0.7, 0.5);
        System.out.println(points.nearest(p6));

    }
}
