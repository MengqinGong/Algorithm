import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by MengqinGong on 9/28/16.
 */
public class BruteCollinearPoints {
    private int n = 0;
    private LineSegment[] lineSegments;
    private ArrayList<LineSegment> ls = new ArrayList<>();

    private void checkArgument(Point[] points) {
        if (points == null) throw new NullPointerException();
        for (Point point : points) {
            if (point == null) throw new NullPointerException();
        }
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++){
                if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException();
            }
        }
    }

    public BruteCollinearPoints(Point[] points) {

        checkArgument(points);

        Point[] fp;

        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {

                    if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])) {

                        for (int l = k + 1; l < points.length; l++) {

                            if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[l])) {
                                fp = new Point[4];
                                fp[0] = points[i];
                                fp[1] = points[j];
                                fp[2] = points[k];
                                fp[3] = points[l];

                                Arrays.sort(fp, new Comparator<Point>(){
                                    public int compare(Point o1, Point o2) {
                                        return o1.compareTo(o2);
                                    }
                                });

                                n++;
                                ls.add(new LineSegment(fp[0], fp[3]));
                            }
                        }
                    }

                }
            }
        }

    }

    public int numberOfSegments() {
        return n;
    }

    public LineSegment[] segments() {
        lineSegments = new LineSegment[n];
        for (int i = 0; i < n; i++) lineSegments[i] = ls.get(i);
        return lineSegments;
    }

    public static void main(String[] args){

        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }


}
