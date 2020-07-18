import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**************************************
 *  @Description This class tests an N by N grid to see if there are enough open
 *  locations to connect the top of the grid with the bottom of the grid.
 *
 * @author Ahmed AbdulAzeem
 * @version 1.0
 *
 *
 ***************************************/

public class Percolation {
    private final WeightedQuickUnionUF ufHelper;
    private final int N;          //Grid side length
    private final int firstReserved, secondReserved;  //represents indices to the virtual objects connected to top and bottom row
    private final boolean[][] grid;  //represents is site opened or close
    private final boolean OPEN = true, FULL = false;

    public Percolation(int n) {
        N = n;
        firstReserved = N * N;
        secondReserved = firstReserved + 1;
        ufHelper = new WeightedQuickUnionUF(secondReserved + 1); //reserve N*N sites plus 2 for virtual connection to the top and the bottom
        grid = new boolean[N][N];


    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        row = row - 1;
        col = col - 1;
        validate(row, col);
        int index = xyTo1D(row, col);
        grid[row][col] = OPEN;
        if (row == 0) { // If it's on the top row, connect to imaginary site at 0.
            ufHelper.union(0, xyTo1D(row, col));
        }
        if (row == N - 1) { // If it's on the bottom row, connect to imaginary
            // site at (N*N) + 1.
            ufHelper.union((N * N) + 1, xyTo1D(row, col));
        }
        if ((row + 1) < N) { // Make sure we don't fall off the grid
            if (grid[row + 1][col] == OPEN)
                ufHelper.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }
        if ((row - 1) >= 0) { // Make sure we don't fall off the grid
            if (grid[row - 1][col] == OPEN)
                ufHelper.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        }
        if ((col + 1) < N) { // Make sure we don't fall off the grid
            if (grid[row][col + 1] == OPEN)
                ufHelper.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }
        if ((col - 1) >= 0) { // Make sure we don't fall off the grid
            if (grid[row][col - 1] == OPEN)
                ufHelper.union(xyTo1D(row, col), xyTo1D(row, col - 1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        row = row - 1;
        col = col - 1;
        validate(row, col);
        return (grid[row][col] == OPEN);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        row = row - 1;
        col = col - 1;
        validate(row, col);
        return (grid[row][col] == FULL);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == OPEN)
                    count++;
            }
        }
        return count;
    }
    

    // does the system percolate?
    public boolean percolates() {
        return ufHelper.connected(0, secondReserved);
    }

    //convert 2-d array indices to 1-d indices
    private int xyTo1D(int i, int j) {
        return ((i * N) + j) + 1;
    }

    private void validate(int x, int y) {
        if (x < 0 || x > N || y < 0 || y > N) {
            throw new IndexOutOfBoundsException("one of the indexes is out of bounds");
        }
    }

    private int getN() {         // Convenience method. Returns N.
        return N;
    }

    public static void main(String[] args) {
        final int TESTS = 1000;
        final int GRID_SIZE = 20;

        System.out.println("\n***Percolation: Monte Carlo Simulation ***\n");

        Percolation perc = new Percolation(GRID_SIZE);
        System.out.println("Successfully created Percolation object.");
        System.out.println("N: " + perc.getN());
        System.out.println();

        System.out.println("Starting to open random sites...");
        int row, col, ct;
        double sum = 0.0;
        for (int i = 0; i < TESTS; i++) {
            ct = 0;
            perc = new Percolation(GRID_SIZE);
            while (!perc.percolates()) {
                row = StdRandom.uniform(perc.getN()) + 1;
                col = StdRandom.uniform(perc.getN()) + 1;
                if (row >= 0 && col >= 0) {
                    if (perc.isFull(row, col)) {
                        perc.open(row, col);
                        ct++;
                    }
                }

                // System.out.println("Count: " + ct);
            }
            sum += ct;
        }
        System.out.println("After " + TESTS + " attempts, the average number of sites");
        System.out.printf("opened was %.2f", sum / TESTS);
        System.out.printf(" or %.2f", ((sum / TESTS) / (GRID_SIZE * GRID_SIZE)) * 100);
        System.out.println("%.");
        System.out.println("\nDone.\n");
    }
}
