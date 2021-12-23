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
        int[][] graph = {
                {0,5,7,7,8,10,3,1,2,4,4,9,10},
                {2,0,7,8,10,3,7,3,5,6,1,8,6},
                {2,10,0,10,4,5,2,1,10,10,6,7,6},
                {8,3,7,0,7,6,3,6,6,4,7,7,4},
                {7,5,1,5,0,9,4,6,3,5,9,6,10},
                {8,3,3,10,1,0,3,5,4,2,4,0,2},
                {9,9,7,3,7,5,0,8,10,2,8,7,1},
                {4,8,5,5,7,5,1,0,2,7,10,1,3},
                {8,7,2,3,5,3,4,9,0,5,1,4,5},
                {2,9,3,9,1,9,8,1,6,0,8,9,2},
                {5,1,8,9,6,6,9,9,3,10,0,6,5},
                {2,6,1,9,4,9,9,10,6,4,6,0,1},
                {9,7,5,9,10,3,5,9,4,1,10,2,0}
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
        System.out.println("Time taken = "+(endTime-startTime));

    }
}
