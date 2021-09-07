/**
 * Algorithms #2
 * Bonus assignment #1
 * Goal: find the minimum cost path in an undirected weighted graph
 * ID: *********
 **/

// import

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class AllDistances {

    //==================================================Variables=======================================================

    private final int[] v_weights; // an array that represents the weight of the vertices
    private final int[][] e_weights; // an adjacency matrix that represents the weight of the edges
    int[] v_plus_e_weights; // an array that that represents the shortest path from the first V to all the others
    private int[][] distance_matrix; // a matrix that represents the distance between all the vertices
    private int[][] parent_mat; // a matrix that represents the parent of each vertex in the graph
    private final int inf = 1000000; // a variable that represents disjointed vertices
    private int length; // a variable that represents the number of vertices in the graph(|V|)
    private boolean isDistanceMatrixFull; // a variable that indicates if the distance matrix is full

    //=================================================Constructor======================================================

    public AllDistances(int[] vertices_weights, int[][] edges_weights) {
        this.length = vertices_weights.length;
        this.v_weights = vertices_weights;
        this.e_weights = edges_weights;
        this.v_plus_e_weights = new int[length];
        this.parent_mat = new int[length][length];
        this.distance_matrix = new int[length][length];
        this.isDistanceMatrixFull = false;
    }


    //================================================Main-Functions====================================================

    // complexity: O(|E*V| + |V^2| * log|V|)
    public int[][] distance_matrix() {
        run_Dijkstra();
        // iterates through all the vertices
//        for (int i = 0; i < length; i++) {
//            // calls Dijkstra to calculates the distance from this vertex to all the others
//            Dijkstra(i);
//            int[] distance_arr;
//            distance_arr = this.v_plus_e_weights;
//            // fills the triangular matrix
//            for (int j = i; j < length; j++) {
//                distance_matrix[i][j] = distance_matrix[j][i] = distance_arr[j];
//            }
//        }
        this.isDistanceMatrixFull = true;
        return distance_matrix;
    }

    // complexity: O(|E+V| * log |V|)
    public int distance(int src, int dest) {
//        // checks if the input is valid
//        isValid(src, dest);
        // adjusts the variables
        src--;
        dest--;
        int ans;
        // an edge case: both of the vertices are equal
        // complexity: O(1)
        if (src == dest) {
            // the distance is equal to the weight of the src vertex
            ans = v_weights[src];
        }
        // if we created the distance matrix already, takes the value form the [src][dest] cell
        // complexity: O(1)
        else if (isDistanceMatrixFull) {
            ans = distance_matrix[src][dest]; // adjusts the variables
        }
        // otherwise, calls Dijkstra
        // complexity: O(|E+V| * log |V|)
        else {
            int[] distance_arr;
            Dijkstra(src);
            distance_arr = this.v_plus_e_weights;
            ans = distance_arr[dest];
        }
        return ans;
    }

    // complexity: O(|E*V| + |V^2| * log|V|)
    public String path(int src, int dest) {
        String path;
        // an edge case: both of the vertices are equal and valid
        if (src == dest && src > 0 && src <= length) {
            path = Integer.toString(src);
        }
        // if there is a path between both of the vertices, then return it
        else if (distance(src, dest) < inf) {
            path = Integer.toString(dest);
            // concatenates the path
            path = concatenate_path((src - 1), (dest - 1), path); // adjusts the variables
        }
        // if there is *not* any path between both of the vertices, then return "null"
        else {
            path = "";
        }

        return path;
    }

    //===============================================Utility-Functions==================================================

    /**
     * The Dijkstra's algorithm is an algorithm for finding the shortest paths between nodes in a graph.
     * For a given source node in the graph, the algorithm finds the shortest path between that node and every other.
     * And updates the v_plus_e_weights array.
     *
     * @param src_vertex - the source vertex
     */
    private void Dijkstra(int src_vertex) {
        // fills all the indexes of the array to infinity
        Arrays.fill(v_plus_e_weights, inf);
        // creating an HashSet which will contain the visited vertices
        HashSet<Integer> visited = new HashSet<>(length);
        // fills the first index of the array with the weight of the src vertex
        v_plus_e_weights[src_vertex] = v_weights[src_vertex];

        // a comparator which compares between two indexes(of vertices, base on their weight.
        Comparator<Integer> WeightComparator = (index_1, index_2) -> v_plus_e_weights[index_1] - v_plus_e_weights[index_2];

        // Creating an empty priority queue
        PriorityQueue<Integer> pq = new PriorityQueue<>(length, WeightComparator);
        // adds the src vertex to the queue
        pq.add(src_vertex);
        // runs while the queue is not empty
        while (!pq.isEmpty()) {
            // removes the element from the top of the queue
            int current = pq.poll();
            // iterates through all the neighbors of the current vertex
            for (int neighbor = 0; neighbor < length; neighbor++) {
                // checks if it's a neighbor and if is not visited yet
                if (neighbor != current && e_weights[current][neighbor] != inf && !visited.contains(neighbor)) {
                    // calculates the weight
                    int src, dest, edge, weight;
                    if (v_plus_e_weights[current] != inf) {
                        src = v_plus_e_weights[current];
                    } else {
                        src = v_weights[current];
                    }
                    dest = v_weights[neighbor];
                    edge = e_weights[current][neighbor];
                    weight = src + edge + dest;
                    if (weight < v_plus_e_weights[neighbor]) {
                        // updates the weight
                        v_plus_e_weights[neighbor] = weight;
                        // adds the current vertex to be the parent of its neighbor
                        parent_mat[src_vertex][neighbor] = current;
//                        System.out.println("son: " + (neighbor + 1) + " ->  parent: " + (current + 1));
                        // adds the neighbor to the queue
                        pq.add(neighbor);
                    }
                }
            }
            // marks this current vertex as visited
            visited.add(current);
        }
    }

    /**
     * Complexity: o(n^3)
     * The function marks all the vertices in the matrix with a direct or indirect connection
     */
//    private void floydWarshall() {
//
//        // calculates direct connections
//        for (int i = 0; i < length; i++) {
//            for (int j = 0; j < length; j++) {
//                // Checks if the source and the destination are equals
//                if (i == j) {
//                    distance_matrix[i][j] = v_weights[i];
////                    parent_mat[i][j] = inf;
//                }
//                // Checks if there is a direct connection between both of the vertices
//                else if (e_weights[i][j] != inf) {
//                    distance_matrix[i][j] = v_weights[i] + e_weights[i][j] + v_weights[j];
//                    parent_mat[i][j] = i;
//                } else {
//                    distance_matrix[i][j] = inf;
//                }
//            }
//        }
//
//        // calculates indirect connections
//        for (int k = 0; k < length; k++) {
//            for (int i = 0; i < length; i++) {
//                for (int j = 0; j < length; j++) {
//                            /* Checks if there is an indirect connection between both of the vertices
//                    and there is a direct connection and if this is not the main diagonal
//                     */
//                    if ((distance_matrix[i][k] != inf && distance_matrix[k][j] != inf && i != j)) {
//                        int distance = distance_matrix[i][k] + distance_matrix[k][j] - v_weights[k];
//                        if (distance < distance_matrix[i][j]) {
//                            distance_matrix[i][j] = distance;
//                            parent_mat[i][j] = k;
//                        }
//                    }
//                }
//            }
//        }
//    }

    private String concatenate_path(int src, int dest, String path) {
        int current = dest;
        int next;

//        System.out.println();
//                for (int i = 0; i < length; i++) {
//            System.out.println(Arrays.toString(parent_mat[i]));
//        }

        while (current != src) {
            // concatenates the parents of the current vertex to the path according to the parent_arr
            next = parent_mat[src][current];
//            System.out.println("current: " + current + " next: " + next);
            path = "-" + path;
            path = Integer.toString(parent_mat[src][current] + 1) + path; // adjusts the variable
            current = next;
        }

        return path;
    }

//    private int count_edges() {
//        int count = 0;
//        // going through the original (triangular) matrix
//        for (int i = 0; i < length; i++) {
//            for (int j = i + 1; j < length; j++) {
//                if (e_weights[i][j] != inf) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }

//    void best_algo() {
//        // if |E| > |V| then use Floyd
//        if (count_edges() > length) {
//            floydWarshall();
//        }
//        // otherwise use Dijkstra
//        else {
//            run_Dijkstra();
//        }
//    }

    private void run_Dijkstra() {
        // iterates through all the vertices
        for (int i = 0; i < length; i++) {
            // calls Dijkstra to calculates the distance from this vertex to all the others
            Dijkstra(i);
            int[] distance_arr;
            distance_arr = this.v_plus_e_weights;
            // fills the triangular matrix
            for (int j = i; j < length; j++) {
                distance_matrix[i][j] = distance_matrix[j][i] = distance_arr[j];
            }
        }
    }


//    private void isValid(int src, int dest) {
//        if (src < 1 || src > length || dest < 1 || dest > length) {
//            throw new IllegalArgumentException("Invalid input!\n" +
//                    "Both of the vertices have to be in the range between 1 to " + length + ".");
//        }
//    }

    //====================================================Main==========================================================

    public static void main(String[] args) {
//
//        //=============================================Basic-Test=======================================================
        final int inf = 1000000;
////        int[] vertices_weight = {2, 4, 3, 6};
////        int[][] edges_weight = {{0, 18, 5, inf},
////                                {18, 0, 2, 3},
////                                {5, 2, 0, inf},
////                                {inf, 3, inf, 0}};
////
////        AllDistances a = new AllDistances(vertices_weight, edges_weight);
//////         distance_matrix
////        int[][] ans = a.distance_matrix();
////        for (int i = 0; i < vertices_weight.length; i++) {
////            System.out.println(Arrays.toString(ans[i]));
////        }
//        // distance
////        System.out.println("the distance is: " + a.distance(1, 4));
////        System.out.println("the distance is: " + a.distance(2, 2));
//        // path
////        System.out.println("the path is: " + a.path(1, 4));
////        System.out.println("the path is: " + a.path(2, 2));
//
//        //==============================================Emanuel-Test====================================================
//
////        int vertices_w[] = {1, 3, 15, 2, 8, 8, 5, 6};
////        int[][] edges_w = {
////                {0, 5, inf, inf, inf, inf, 4, inf},
////                {5, 0, inf, inf, inf, 6, 8, inf},
////                {inf, inf, 0, 6, inf, inf, 2, 5},
////                {inf, inf, 6, 0, 5, inf, 18, inf},
////                {inf, inf, inf, 5, 0, 4, 3, inf},
////                {inf, 6, inf, inf, 4, 0, inf, inf},
////                {4, 8, 2, 18, 3, inf, 0, 7},
////                {inf, inf, 5, inf, inf, inf, 7, 0}
////        };
////        AllDistances ad = new AllDistances(vertices_w, edges_w);
//        // result:
//        /*
//        [1, 9, 27, 28, 21, 23, 10, 23]
//        [9, 3, 33, 34, 27, 17, 16, 29]
//        [27, 33, 15, 23, 33, 45, 22, 26]
//        [28, 34, 23, 2, 15, 27, 23, 34]
//        [21, 27, 33, 15, 8, 20, 16, 29]
//        [23, 17, 45, 27, 20, 8, 28, 41]
//        [10, 16, 22, 23, 16, 28, 5, 18]
//        [23, 29, 26, 34, 29, 41, 18, 6]
//        */
//
////        //-----Simulation-------
////        long startTime = System.nanoTime();
////        //Floyd Warshal
////        //Floyd Warshall
////        for (int i = 0; i < 1000000; i++) {
////        ad.distance_matrix();
////        }
////        long endTime = System.nanoTime();
////        long timeElapsed = endTime - startTime;
////
////        for (int i = 0; i < ad.length; i++) {
////            System.out.println(Arrays.toString(ad.distance_matrix()[i]));
////        }
////        System.out.println("Floyd Warshall - Execution time in milliseconds: " + timeElapsed / 1000000);
//
////        System.out.println(ad.distance(1, 4)); // 28
////        System.out.println(ad.distance(2, 2)); // 3
////        System.out.println(ad.path(1, 4)); // 1-7-5-4
////        System.out.println(ad.path(2, 5)); // 2-7-5
////        System.out.println(ad.path(5, 4)); // 5-4
////        System.out.println(ad.path(1, 5)); // 1-7-5
////        System.out.println(ad.path(6, 2)); // 6-2
////        System.out.println(ad.path(5, 8));// 5-7-8
////        System.out.println(ad.path(4, 8)); // 4-3-8
////        System.out.println(ad.path(2, 2)); // 2
//
//        //================================================Zero-Test=====================================================
//        int[] vertices_weight = {2, 4, 3, 6};
//        int[][] edges_weight = {{0, 18, 5, inf},
//                {18, 0, 2, inf},
//                {5, 2, 0, inf},
//                {inf, 3, inf, 0}};
//
//        AllDistances ad = new AllDistances(vertices_weight, edges_weight);
//
//        ad.distance_matrix();
//        for (int i = 0; i < ad.length; i++) {
//            System.out.println(Arrays.toString(ad.distance_matrix()[i]));
//        }
//
//        System.out.println(ad.distance(1, 4)); // inf
//        System.out.println(ad.distance(2, 2)); // 4
//        System.out.println(ad.path(1, 4)); // ""
//        System.out.println(ad.path(4, 1)); // ""
//        System.out.println(ad.path(3, 3)); // 3
//        System.out.println(ad.path(3, 2)); // 3-2
//        System.out.println(ad.path(4, 3));// ""
//        System.out.println(ad.path(2, 2)); // 2
//        System.out.println(ad.path(4, 2)); // ""
//    }
        int[] vertices_weight2 = {1, 0, 1, 0, 1, 1};
        int[][] edges_weight2 = {{1, inf, 1, inf, inf, inf},
                {inf, 0, inf, 3, 10, inf},
                {1, inf, 1, inf, inf, 1},
                {inf, inf, inf, 0, inf, inf},
                {10, inf, inf, inf, 1, 1},
                {inf, inf, 1, inf, 1, 1}};

        AllDistances ad2 = new AllDistances(vertices_weight2, edges_weight2);
        System.out.println(ad2.path(1, 4));
    }
}


