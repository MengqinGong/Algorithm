import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private byte[] status;
    private WeightedQuickUnionUF sites;
    private int size;
    private int virtualTop;
    private int virtualBottom;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n should be positive integer");
        status = new byte[n * n + 2];
        for (int i = 0; i < n * n + 2; i++) status[i] = 0;
        sites = new WeightedQuickUnionUF(n * n + 2);
        size = n;
        virtualTop = n * n;
        status[virtualTop] = 1;
        virtualBottom = n * n + 1;
        status[virtualBottom] = 1;
    }

    private int xyTo1D(int x, int y) {
        return (x - 1) * size + y - 1;
    }

    private void validate(int i, int j) {
        if (i <= 0 || i > size || j <= 0 || j > size) throw new IndexOutOfBoundsException("index " +
                i + " or " + j + " is not between 1 and " + size);
    }

    public void open(int i, int j) {
        validate(i, j);
        int index = xyTo1D(i, j);
        if (!isOpen(i, j)) {
            status[index] = 1;
            if (i == 1) sites.union(index, virtualTop);
            if (i == size) sites.union(index, virtualBottom);
            if (i >= 2 && isOpen(i - 1, j)) sites.union(xyTo1D(i - 1, j), index);
            if (i < size && isOpen(i + 1, j)) sites.union(xyTo1D(i + 1, j), index);
            if (j >= 2 && isOpen(i, j - 1)) sites.union(xyTo1D(i, j - 1), index);
            if (j < size && isOpen(i, j + 1)) sites.union(xyTo1D(i, j + 1), index);
        }
    }

    public boolean isOpen(int i, int j) {
        validate(i, j);
        return status[xyTo1D(i, j)] == 1;
    }

    public boolean isFull(int i, int j) {
        validate(i, j);

        if (!isOpen(i, j)) {
            return false;
        }

        return sites.connected(xyTo1D(i, j), virtualTop);
    }

    public boolean percolates() {
        return sites.connected(virtualTop, virtualBottom);
    }

    public static void main(String[] args) {
        // test client (optional)
        new Percolation(2);
    }
}
