import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Stack;

/**
 * Created by MengqinGong on 10/7/16.
 */
public class KdTree {

    private static final boolean VERTICAL = false;
    private static final boolean HORIZONTAL = true;
    private Node root;
    private int size;
    private Stack<Point2D> pointsInRect;
    private Point2D nearestPoint;
    private double shortestDistance;

    private static class Node implements Comparable<Node> {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
        private boolean orientation;

        public Node(Point2D p, boolean orientation) {
            this.p = p;
            this.orientation = orientation;
        }

        public int compareTo(Node that) {
            if (that == null) throw new NullPointerException("No compare to a NULL point");
            return p.compareTo(that.p);
        }
    }

    public KdTree() {
        root = null;
        size = 0;
        pointsInRect = new Stack<>();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("Please do not insert NULL point");
        if (contains(p)) return;

        root = put(root, p);
    }

    private Node put(Node h, Point2D p) {
        if (h == root && h == null) {
            Node toBeAdded = new Node(p, VERTICAL);
            size++;
            toBeAdded.rect = new RectHV(0, 0, 1, 1);
            return toBeAdded;
        }

        if ((h.orientation == VERTICAL && p.x() < h.p.x()) ||
                (h.orientation == HORIZONTAL && p.y() < h.p.y())) {
            if (h.lb == null) {
                Node toBeAdded = new Node(p, !h.orientation);
                if (h.orientation == VERTICAL)
                    toBeAdded.rect = new RectHV(h.rect.xmin(), h.rect.ymin(), h.p.x(), h.rect.ymax());
                else toBeAdded.rect = new RectHV(h.rect.xmin(), h.rect.ymin(), h.rect.xmax(), h.p.y());
                h.lb = toBeAdded;
                size++;
                return h;
            } else h.lb = put(h.lb, p);
        }

        if ((h.orientation == VERTICAL && p.x() >= h.p.x()) ||
                (h.orientation == HORIZONTAL && p.y() >= h.p.y())) {
            if (h.rt == null) {
                Node toBeAdded = new Node(p, !h.orientation);
                if (h.orientation == VERTICAL)
                    toBeAdded.rect = new RectHV(h.p.x(), h.rect.ymin(), h.rect.xmax(), h.rect.ymax());
                else toBeAdded.rect = new RectHV(h.rect.xmin(), h.p.y(), h.rect.xmax(), h.rect.ymax());
                h.rt = toBeAdded;
                size++;
                return h;
            } else h.rt = put(h.rt, p);
        }

        return h;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("You want me to check NULL point?");
        Node current = root;
        while (current != null) {
            if (current.p.equals(p)) return true;
            if (current.orientation == VERTICAL) {
                if (p.x() < current.p.x()) current = current.lb;
                else current = current.rt;
            }
            else {
                if (p.y() < current.p.y()) current = current.lb;
                else  current = current.rt;
            }
        }
        return false;
    }

    public void draw() {
        if (root != null) drawNode(root);
    }

    private void drawNode(Node h) {

        if (h.lb != null) drawNode(h.lb);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        h.p.draw();

        StdDraw.setPenRadius(0.002);
        if (h.orientation == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(h.p.x(), h.rect.ymin(), h.p.x(), h.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(h.rect.xmin(), h.p.y(), h.rect.xmax(), h.p.y());
        }

        if (h.rt != null) drawNode(h.rt);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("No NULL rectangular:)");
        if (root != null) {
            pointsInRect = new Stack<>();
            rangeSearch(root, rect);
        }
        return pointsInRect;
    }

    private void rangeSearch(Node h, RectHV rect) {
        if (rect.contains(h.p)) pointsInRect.push(h.p);
        if (h.orientation == VERTICAL) {
            if (h.lb != null && rect.xmin() < h.p.x()) rangeSearch(h.lb, rect);
            if (h.rt != null && rect.xmax() >= h.p.x()) rangeSearch(h.rt, rect);
        } else {
            if (h.lb != null && rect.ymin() < h.p.y()) rangeSearch(h.lb, rect);
            if (h.rt != null && rect.ymax() >= h.p.y()) rangeSearch(h.rt, rect);
        }
        /*if (h.lb != null && h.lb.rect.intersects(rect)) rangeSearch(h.lb, rect);
        if (h.rt != null && h.rt.rect.intersects(rect)) rangeSearch(h.rt, rect);*/
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("Please no NULL point");
        if (root != null) {
            nearestPoint = root.p;
            shortestDistance = root.p.distanceSquaredTo(p);
            nearest(root, p);
        }
        return nearestPoint;
    }

    private void nearest(Node h, Point2D p) {

        if (h.p.distanceSquaredTo(p) < shortestDistance) {
            shortestDistance = h.p.distanceSquaredTo(p);
            nearestPoint = h.p;
        }

        if ((h.orientation == VERTICAL && p.x() <= h.p.x()) ||
                (h.orientation == HORIZONTAL && p.y() <= h.p.y())) {
            if (h.lb != null && h.lb.rect.distanceSquaredTo(p) < shortestDistance) nearest(h.lb, p);
            if (h.rt != null && h.rt.rect.distanceSquaredTo(p) < shortestDistance) nearest(h.rt, p);
        } else if ((h.orientation == VERTICAL && p.x() > h.p.x()) ||
                (h.orientation == HORIZONTAL && p.y() > h.p.y())) {
            if (h.rt != null && h.rt.rect.distanceSquaredTo(p) < shortestDistance) nearest(h.rt, p);
            if (h.lb != null && h.lb.rect.distanceSquaredTo(p) < shortestDistance) nearest(h.lb, p);
        }

    }


    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        System.out.println(kdTree.isEmpty());
        System.out.println(kdTree.size());

        Point2D p1 = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4, 0.7);
        Point2D p5 = new Point2D(0.9, 0.6);

        kdTree.insert(p1);
        System.out.println(kdTree.isEmpty());
        System.out.println(kdTree.size());

        kdTree.insert(p2);
        System.out.println(kdTree.contains(p2));
        System.out.println("is empty? " + kdTree.isEmpty());
        System.out.println("size now: " + kdTree.size());


        kdTree.insert(p3);
        kdTree.insert(p4);
        kdTree.insert(p5);

        RectHV rect01 = new RectHV(0.1, 0.8, 0.2, 0.9);


        for (Point2D point : kdTree.range(rect01))
            System.out.println(point);

        System.out.println(kdTree.nearest(new Point2D(0.5, 0.5)));


    }
}
