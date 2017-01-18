import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] thresholds;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("size and number of " +
                "trials should be positive integers");
        thresholds = new double[trials];
        for (int i = 0; i < thresholds.length; i++) {
            thresholds[i] = simulation(n);
        }
    }

    public static void main(String[] args) {

        PercolationStats percStats = new PercolationStats(100, 100);
        System.out.println("mean = " + percStats.mean());
        System.out.println("stddev = " + percStats.stddev());
        System.out.println("confidenceLo = " + percStats.confidenceLo());
        System.out.println("confidenceHi = " + percStats.confidenceHi());

    }

    private double simulation(int n) {
        int count = 0;
        Percolation perc = new Percolation(n);
        while (!perc.percolates()) {
            int i = StdRandom.uniform(1, n + 1);
            int j = StdRandom.uniform(1, n + 1);
            if (!perc.isOpen(i, j)) {
                perc.open(i, j);
                count++;
            }
        }
        return (double) count / (n * n);
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLo() {
        double mu = mean();
        double sigma = stddev();

        return (mu - 1.96 * sigma / Math.sqrt(thresholds.length));
    }

    public double confidenceHi() {
        double mu = mean();
        double sigma = stddev();

        return (mu + 1.96 * sigma / Math.sqrt(thresholds.length));
    }

}
