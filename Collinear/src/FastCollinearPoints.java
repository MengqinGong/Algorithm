import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by MengqinGong on 9/28/16.
 */
public class FastCollinearPoints {

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

    private void findLineSegment(Point[] points, Point[] pointsCOPY, int num, int i, int j) {
        Point[] contPoints = new Point[num + 1];
        contPoints[0] = points[i];
        for (int k = 0; k < num; k++) contPoints[k + 1] = pointsCOPY[j - k];

        Arrays.sort(contPoints, new Comparator<Point>() {
            public int compare(Point o1, Point o2) {
                return o1.compareTo(o2);
            }
        });

        if (points[i] == contPoints[0]) {
            ls.add(new LineSegment(contPoints[0], contPoints[num]));
        }
    }

    public FastCollinearPoints(Point[] points) {

        checkArgument(points);

        int num;
        Point[] pointsCOPY = new Point[points.length];
        System.arraycopy(points, 0, pointsCOPY, 0, points.length);

        for (int i = 0; i < points.length; i++) {
            num = 1;
            Arrays.sort(pointsCOPY, points[i].slopeOrder());
            for (int j = 0; j < points.length - 1; j++) {
                if (points[i].slopeTo(pointsCOPY[j]) == points[i].slopeTo(pointsCOPY[j + 1])) {
                    if (j == points.length - 2 && num >= 2) {

                        findLineSegment(points, pointsCOPY, num + 1, i, j+1);

                    } else num++;
                }
                else {
                    if (num >= 3) {

                        findLineSegment(points, pointsCOPY, num, i, j);

                        num = 1;
                    } else num = 1;
                }
            }
        }
    }


    public int numberOfSegments() {
        return ls.size();
    }

    public LineSegment[] segments() {

        return ls.toArray(new LineSegment[ls.size()]);

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
