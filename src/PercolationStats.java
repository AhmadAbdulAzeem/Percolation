import edu.princeton.cs.algs4.StdRandom;

/**************************************
 *  To perform a series of computational experiments on class Percolation
 *
 * @author Ahmed AbdulAzeem
 *
 * @version 1.0
 **************************************/

public class PercolationStats {
    private final int N;    //Grid side length
    private final int T;  //number of trials

    public PercolationStats(int n, int trials) {
        this.N = n;
        this.T = trials;
    }

    // sample mean of percolation threshold
    public double mean() {
        int row, col, ct;
        Percolation perc = new Percolation(N);
        double mean = 0.0;
        for (int i = 0; i < T; i++) {
            ct = 0;
            perc = new Percolation(N);
            while (!perc.percolates()) {
                row = StdRandom.uniform(N) + 1;
                col = StdRandom.uniform(N) + 1;
                if (row >= 0 && col >= 0) {
                    if (perc.isFull(row, col)) {
                        perc.open(row, col);

                    }
                }

                // System.out.println("Count: " + ct);
            }
            mean += ((perc.numberOfOpenSites() * 1.0) / (N * N));
        }
        return mean / T;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double mean = mean();
        double temp = 0.0;
        double sSquare = 0.0;
        int row, col, ct;
        Percolation perc = new Percolation(N);
        for (int i = 0; i < T; i++) {
            ct = 0;
            perc = new Percolation(N);
            while (!perc.percolates()) {
                row = StdRandom.uniform(N) + 1;
                col = StdRandom.uniform(N) + 1;
                if (row >= 0 && col >= 0) {
                    if (perc.isFull(row, col)) {
                        perc.open(row, col);

                    }
                }

                // System.out.println("Count: " + ct);
            }
            temp = ((perc.numberOfOpenSites() * 1.0) / (N * N));
            sSquare += Math.pow((temp - mean), 2);
        }
        return Math.sqrt(sSquare / (T - 1));
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double x = mean();
        double s = stddev();
        return x - ((1.96 * s) / (Math.sqrt(T)));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double x = mean();
        double s = stddev();
        return x + ((1.96 * s) / (Math.sqrt(T)));
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats t = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("Mean                    = " + t.mean());
        System.out.println("stddev                  = " + t.stddev());
        System.out.println("95% confidence interval = " + "[" + t.confidenceLo() + ", " + t.confidenceHi() + "]");
    }
}
