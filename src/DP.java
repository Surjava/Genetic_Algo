import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class DP {

    private final int N, start;
    private final double[][] distance;
    private List<Integer> tour = new ArrayList<>();
    private double minTourCost = Double.POSITIVE_INFINITY;
    private boolean ranSolver = false;

    public DP(double[][] distance) {
        this(0, distance);
    }

    public DP(int start, double[][] distance) {
        N = distance.length;

        if (N <= 2) throw new IllegalStateException("N <= 2 not yet supported.");
        if (N != distance[0].length) throw new IllegalStateException("Matrix must be square (n x n)");
        if (start < 0 || start >= N) throw new IllegalArgumentException("Invalid start node.");

        this.start = start;
        this.distance = distance;
    }

    // Returns the optimal tour for the traveling salesman problem.
    public List<Integer> getTour() {
        if (!ranSolver) solve();
        return tour;
    }

    // Returns the minimal tour cost.
    public double getTourCost() {
        if (!ranSolver) solve();
        return minTourCost;
    }

    // Solves the traveling salesman problem and caches solution.
    public void solve() {

        if (ranSolver) return;

        final int END_STATE = (1 << N) - 1;
        Double[][] memo = new Double[N][1 << N];

        // Add all outgoing edges from the starting node to memo table.
        for (int end = 0; end < N; end++) {
            if (end == start) continue;
            memo[end][(1 << start) | (1 << end)] = distance[start][end];
        }

        for (int r = 3; r <= N; r++) {
            for (int subset : combinations(r, N)) {
                if (notIn(start, subset)) continue;
                for (int next = 0; next < N; next++) {
                    if (next == start || notIn(next, subset)) continue;
                    int subsetWithoutNext = subset ^ (1 << next);
                    double minDist = Double.POSITIVE_INFINITY;
                    for (int end = 0; end < N; end++) {
                        if (end == start || end == next || notIn(end, subset)) continue;
                        double newDistance = memo[end][subsetWithoutNext] + distance[end][next];
                        if (newDistance < minDist) {
                            minDist = newDistance;
                        }
                    }
                    memo[next][subset] = minDist;
                }
            }
        }

        // Connect tour back to starting node and minimize cost.
        for (int i = 0; i < N; i++) {
            if (i == start) continue;
            double tourCost = memo[i][END_STATE] + distance[i][start];
            if (tourCost < minTourCost) {
                minTourCost = tourCost;
            }
        }

        int lastIndex = start;
        int state = END_STATE;
        tour.add(start);

        // Reconstruct TSP path from memo table.
        for (int i = 1; i < N; i++) {

            int index = -1;
            for (int j = 0; j < N; j++) {
                if (j == start || notIn(j, state)) continue;
                if (index == -1) index = j;
                double prevDist = memo[index][state] + distance[index][lastIndex];
                double newDist  = memo[j][state] + distance[j][lastIndex];
                if (newDist < prevDist) {
                    index = j;
                }
            }

            tour.add(index);
            state = state ^ (1 << index);
            lastIndex = index;
        }

        tour.add(start);
        Collections.reverse(tour);

        ranSolver = true;
    }

    private static boolean notIn(int elem, int subset) {
        return ((1 << elem) & subset) == 0;
    }

    // This method generates all bit sets of size n where r bits
    // are set to one. The result is returned as a list of integer masks.
    public static List<Integer> combinations(int r, int n) {
        List<Integer> subsets = new ArrayList<>();
        combinations(0, 0, r, n, subsets);
        return subsets;
    }

    // To find all the combinations of size r we need to recurse until we have
    // selected r elements (aka r = 0), otherwise if r != 0 then we still need to select
    // an element which is found after the position of our last selected element
    private static void combinations(int set, int at, int r, int n, List<Integer> subsets) {

        // Return early if there are more elements left to select than what is available.
        int elementsLeftToPick = n - at;
        if (elementsLeftToPick < r) return;

        // We selected 'r' elements so we found a valid subset!
        if (r == 0) {
            subsets.add(set);
        } else {
            for (int i = at; i < n; i++) {
                // Try including this element
                set |= 1 << i;

                combinations(set, i + 1, r - 1, n, subsets);

                // Backtrack and try the instance where we did not include this element
                set &= ~(1 << i);
            }
        }
    }

    public static void main(String[] args) {
        // Create adjacency matrix
        double[][] distanceMatrix = {{0,3,63,17,65,73,55,33,53,99,68,14,90,34,68,69,33,43,40,37,40,7,72,25,63,88,1,44,77,35},{38,0,93,35,85,63,40,6,26,70,64,52,59,43,29,91,40,4,80,89,26,28,65,91,35,32,15,77,51,58},{25,15,0,4,34,29,28,98,91,58,52,46,84,48,45,22,67,58,14,54,69,39,1,3,87,83,91,23,53,13},{69,24,74,0,11,16,88,41,59,41,47,76,96,64,63,89,69,22,41,3,38,40,57,56,9,49,61,52,79,48},{21,67,65,87,0,65,7,27,24,93,18,51,45,28,33,29,4,34,1,19,35,99,63,55,43,56,76,31,59,74},{53,55,53,17,2,0,65,45,83,27,21,79,54,71,78,84,46,84,66,75,46,97,85,8,21,12,9,42,29,29},{3,20,63,17,18,44,0,88,25,84,70,51,78,52,12,8,83,38,26,62,82,7,92,73,12,3,1,83,97,55},{25,11,9,39,23,20,87,0,35,33,57,64,27,56,18,77,23,64,72,4,15,20,3,25,10,24,22,54,42,65},{30,62,45,90,31,78,20,88,0,81,93,31,88,56,23,78,10,45,85,86,75,28,89,68,47,84,79,97,63,84},{72,7,38,68,1,35,76,67,92,0,12,20,20,64,7,22,75,72,23,59,14,72,63,18,90,35,51,83,22,92},{84,18,97,57,77,71,88,32,27,10,0,14,58,97,89,79,53,84,97,50,49,95,87,62,3,96,69,85,51,67},{93,75,38,35,37,44,57,54,74,10,95,0,21,85,87,9,43,37,22,1,62,57,62,2,40,98,36,51,18,89},{71,45,45,50,6,92,37,51,63,31,55,90,0,57,80,7,62,45,8,87,3,12,10,18,90,71,46,17,2,82},{35,52,46,71,44,37,75,76,15,11,48,20,8,0,96,49,76,70,99,38,76,55,82,11,57,22,5,73,42,12},{22,52,81,32,48,33,27,47,7,58,78,34,63,70,0,38,95,23,27,45,30,96,86,48,92,2,11,21,74,45},{75,17,88,34,34,14,51,11,21,1,81,90,73,7,98,0,4,52,3,1,7,93,24,1,80,73,47,32,32,82},{38,75,17,8,89,49,28,16,65,38,46,21,83,21,98,61,0,49,87,92,7,75,1,63,90,48,71,1,75,89},{84,7,53,88,19,50,52,30,40,92,65,11,49,73,85,20,31,0,69,68,64,80,35,23,35,62,79,90,5,21},{60,7,12,44,89,16,97,4,81,92,53,64,22,42,39,94,62,86,0,30,78,80,60,50,83,23,81,78,83,45},{19,2,53,61,99,29,38,40,48,68,80,58,2,16,26,64,26,32,85,0,97,11,65,82,33,65,83,79,47,46},{38,44,6,74,56,89,95,6,79,33,67,39,10,76,48,64,3,68,83,81,0,5,62,67,4,99,77,35,83,11},{47,92,30,94,5,48,5,85,10,77,64,61,77,9,66,67,36,17,78,41,78,0,44,26,12,61,22,68,13,45},{40,16,83,79,37,27,54,38,10,78,76,95,13,8,68,74,30,25,44,24,76,71,0,71,42,13,13,54,50,64},{61,68,29,81,3,92,11,17,56,52,25,45,52,80,79,35,82,15,34,46,2,22,53,0,89,57,48,5,86,24},{6,92,36,18,64,27,73,77,49,74,18,79,84,43,60,25,29,32,45,12,6,26,72,32,0,41,37,49,62,86},{29,17,20,82,99,50,86,24,5,73,80,35,71,96,97,34,95,54,83,74,50,52,87,33,50,0,77,16,98,47},{31,29,53,88,91,25,48,35,95,90,23,67,14,74,25,16,9,4,58,48,26,99,95,15,59,78,0,59,9,96},{24,98,86,58,84,56,53,98,76,61,80,11,22,16,49,36,96,88,60,82,41,46,31,34,29,89,68,0,10,44},{39,32,33,14,77,50,16,63,43,36,67,73,49,4,1,12,20,75,68,20,22,15,20,50,14,94,9,53,0,87},{31,39,58,44,47,96,92,62,50,9,33,56,79,9,33,66,39,7,77,49,23,91,73,52,65,48,60,2,61,0}};



        int startNode = 0;
        DP solver = new DP(startNode, distanceMatrix);

        System.out.println("Tour: " + solver.getTour());

        System.out.println("Tour cost: " + solver.getTourCost());
    }
}