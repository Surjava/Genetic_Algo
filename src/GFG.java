import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Java implementation of the approach
class GFG
{

    // Function to find the minimum weight
    // Hamiltonian Cycle
    static int tsp(int[][] graph, boolean[] v,
                   int currPos, int n,
                   int count, int cost, int ans)
    {

        // If last node is reached and it has a link
        // to the starting node i.e the source then
        // keep the minimum value out of the total cost
        // of traversal and "ans"
        // Finally return to check for more possible values
        if (count == n && graph[currPos][0] > 0)
        {
            ans = Math.min(ans, cost + graph[currPos][0]);
            return ans;
        }

        // BACKTRACKING STEP
        // Loop to traverse the adjacency list
        // of currPos node and increasing the count
        // by 1 and cost by graph[currPos,i] value
        for (int i = 0; i < n; i++)
        {
            if (v[i] == false && graph[currPos][i] > 0)
            {

                // Mark as visited
                v[i] = true;
                ans = tsp(graph, v, i, n, count + 1,
                        cost + graph[currPos][i], ans);

                // Mark ith node as unvisited
                v[i] = false;
            }
        }
        return ans;
    }

    // Driver code
    public static void main(String[] args)
    {

        // n is the number of nodes i.e. V
        int n = 13;
        long startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//        int[][] graph = {
//                {0,5,7,7,8,3,3,1,2,4,4,9,3},
//                {2,0,7,8,3,3,7,3,5,6,1,8,6},
//                {2,3,0,3,4,5,2,1,3,3,6,7,6},
//                {8,3,7,0,7,6,3,6,6,4,7,7,4},
//                {7,5,1,5,0,9,4,6,3,5,9,6,3},
//                {8,3,3,3,1,0,3,5,4,2,4,3,2},
//                {9,9,7,3,7,5,0,8,3,2,8,7,1},
//                {4,8,5,5,7,5,1,0,2,7,3,1,3},
//                {8,7,2,3,5,3,4,9,0,5,1,4,5},
//                {2,9,3,9,1,9,8,1,6,0,8,9,2},
//                {5,1,8,9,6,6,9,9,3,3,0,6,5},
//                {2,6,1,9,4,9,9,3,6,4,6,0,1},
//                {9,7,5,9,3,3,5,9,4,1,3,2,0}
//        };

        int graph[][] = {
                {0,33,49,36,30,48,87,61,59,68,62,91,45},
                {16,0,72,98,48,51,50,87,34,6,92,73,35},
                {74,17,0,33,66,82,60,25,18,81,69,23,89},
                {52,22,58,0,82,63,63,2,52,44,23,88,34},
                {51,76,88,68,0,55,25,74,81,72,49,85,73},
                {64,62,56,12,75,0,73,47,44,33,3,88,60},
                {59,1,78,96,22,28,0,53,79,52,25,70,63},
                {54,97,50,23,37,32,26,0,36,66,7,99,22},
                {71,54,63,77,26,26,98,98,0,96,87,14,75},
                {84,95,82,14,90,73,93,10,9,0,35,93,76},
                {42,29,94,7,97,35,40,48,43,70,0,73,63},
                {48,1,47,2,70,38,90,29,6,71,11,0,43},
                {69,61,60,6,1,21,94,98,93,61,43,47,0}

        };

        // Boolean array to check if a node
        // has been visited or not
        boolean[] v = new boolean[n];

        // Mark 0th node as visited
        v[0] = true;
        int ans = Integer.MAX_VALUE;

        // Find the minimum weight Hamiltonian Cycle
        ans = tsp(graph, v, 0, n, 1, 0, ans);

        // ans is the minimum weight Hamiltonian Cycle
        System.out.println(ans);
        long endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        System.out.println("Time taken = "+(endTime-startTime)+" seconds");

    }
}
