/**
 * Algorithms #2
 * Bonus assignment #3
 * Goal: Find an Euler path in a given graph
 * ID: *********
 **/

import java.util.*;

public class Euler {

    //================================================Variables===================================================
    private boolean[][] adj_matrix;
    private int length; // a variable that represents the number of vertices in the graph(|V|)
    private boolean is_checked; // a variable that indicates if we already checked for euler path
    private boolean has_path; // a variable that indicates if we have an euler path
    private boolean is_generated_path; // a variable that indicates if we already generates a path
    private HashSet<Integer> visited; // a HashSet which contains all the visited vertices
    private int source; // the source will be a vertex with an odd degree
    private int arr_degrees[]; // an array that represents the degree of the vertices in the graph
    private String path; // a string that represents the Euler path
    private ArrayList<Integer>[] neighbours; // an array of arraylist that contains the neighbors list of each vertex
    private HashMap<Integer, Set<Integer>> map;

    //===============================================Constructor==================================================
    public Euler(boolean[][] adj_matrix) {
        this.adj_matrix = adj_matrix;
        this.length = adj_matrix.length;
        this.visited = new HashSet<>(length);
        this.source = -1; // -1 means that all the vertices have even degree
        this.arr_degrees = new int[length];
        this.path = "";
        this.neighbours = new ArrayList[length];
        this.map = new HashMap<Integer, Set<Integer>>(length);
    }

    //==============================================Main-Functions================================================

    /**
     * This function checks if there is an Euler path in the graph
     *
     * @return true if there is
     * <p>
     * complexity: O(n^2)
     */
    public boolean has_euler_path() {

        if (this.length < 1) { // if the graph is an empty, then there is not path
            this.is_checked = true;
            this.has_path = false;

        }

        else if (this.length == 1) { // if the graph has only one vertex, then there is a path
            this.is_checked = true;
            this.has_path = true;
        }

        else if (!is_checked) { // if did not checked already
            boolean condition_1 = has_odd_vertices(); // there are exactly zero or two vertices with odd degree in the graph
            boolean condition_2 = false; // there is only one component in the graph
            if (condition_1) {
                BFS(0);
                condition_2 = (visited.size() == length); // all the vertices in the graph have been traversed
            }

            this.has_path = (condition_1 && condition_2); // if both of the conditions are true so we have an Euler path
            this.is_checked = true;
        }

        return this.has_path;
    }

    /**
     * This function returns an Euler path if exists one in the graph
     *
     * @return a string of an Euler path
     */
    public String euler_path() {

        if (this.length < 1) { // if the graph is an empty, then return an empy string
            this.is_generated_path = true;
            this.path = "";
            return this.path;
        }

        else if (this.length == 1) { // if the graph has only one vertex, then return 0
            this.is_generated_path = true;
            this.path = "0";
            return this.path;
        }

       else if (!is_generated_path && has_euler_path()) { // if there is an euler path and we did not generated the path already
            // creates a queue which will contain the vertices that have at least one neighbour(degree > 0)
            Stack<Integer> stack = new Stack<>();
            if (this.source != -1) { // if there is a vertex with an odd degree then adds its to the stack
                stack.push(this.source);
            } else { // otherwise adds the first vertex
                stack.push(0);
            }

            while (!stack.isEmpty()) {
                int current = stack.peek(); // takes the first vertex from the stack
                if (arr_degrees[current] == 0) { // checks if the degree of the vertex is equals to zero
                    // removes the vertex from the stack and concatenates it to the path
                    if (path == "") {
                        this.path += stack.pop();
                    } else {
                        this.path = this.path + "->" + stack.pop();
                    }
                } else { // otherwise adds one of it's neighbour to the stack
                    // get iterator
                    Iterator<Integer> it = this.map.get(current).iterator();
                    if (it.hasNext()) {
                        int nei = it.next();
                        stack.push(nei);
                        it.remove(); // deletes the edge
                        // second side
                        this.map.get(nei).remove(current);
                        // decreases the degree of the vertices which connect to the edge
                        arr_degrees[current]--;
                        arr_degrees[nei]--;
                    }
                }
            }
        }

        this.is_generated_path = true;
        return this.path;
    }

    //==============================================Utility-Functions=============================================

    /**
     * This function checks if there are exactly zero or two vertices with odd degree in the graph
     *
     * @return true if there are
     * <p>
     * complexity: O(n^2)
     */
    private boolean has_odd_vertices() {
        int count_vertices = 0;
        int count_degree = 0;

        // Traversing the matrix and count how many vertices have an odd degree
        for (int i = 0; i < length; i++) {
            Set<Integer> nei_set = new HashSet<Integer>();
            for (int j = 0; j < length; j++) {
                if (adj_matrix[i][j] == true) {  // if there is an edge between vertex 'i' and vertex 'j'
                    nei_set.add(j);
                    map.put(i, nei_set); // adds vertex j to be a neighbour of i
                    count_degree++; // then increase the degree
                }
            }
            this.arr_degrees[i] = count_degree; // updates the degree of the current vertex

            if (count_degree % 2 == 1) { // if i's degree is odd
                count_vertices++; // then count it
                if (this.source == -1) // if the out source is equals to -1
                {
                    this.source = i; // updates the source to be the vertex with the odd degree
                }
            }
            count_degree = 0;
        }

        return (count_vertices == 0 || count_vertices == 2);
    }

    /**
     * The Breadth-first search (BFS) is an algorithm for traversing or searching
     * tree or graph data structures. It starts at the given node in the graph,
     * and explores all of the neighbor nodes at the present depth prior to moving on
     * to the nodes at the next depth level.
     *
     * @param root - the source vertex
     *             <p>
     *             complexity: O(|V|+|E|)
     */
    private void BFS(int root) {
        // clears the visited HashSet.
        visited.clear();

        // creates a queue which will contain the vertices that need to traverse (by their order).
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(root);

        /*
        While the queue is not empty, the algorithm takes the first vertex and traverses all its neighbors.
        If this neighbor is not yet visited, it adds to the queue and marks as visited.
        After the algorithm finishes gaining with all the neighbors, it marks the current node as visited
        and continues to the next node in the queue.
        */
        while (!queue.isEmpty()) {
            int current = queue.remove();
//            for (int i = 0; i < length; i++) {
//                if (adj_matrix[current][i] == true) {
//                    if (!visited.contains(i)) {
//                        queue.add(i);
//                        visited.add(i);
//                    }
//                }

            // get iterator
            Iterator<Integer> it = this.map.get(current).iterator();
            while (it.hasNext()) {
                int nei = it.next();
                if (!visited.contains(nei)) {
                    queue.add(nei);
                    visited.add(nei);
                }

                visited.add(current);
            }
        }
    }


    //===================================================Main=====================================================
    public static void main(String[] args) {

        boolean T = true;
        boolean F = false;
        boolean example_matrix[][] = {{F, T, T, T, F},
                {T, F, T, T, F},
                {T, T, F, T, T},
                {T, T, T, F, T},
                {F, F, T, T, F}};

        Euler e = new Euler(example_matrix);
        System.out.println("e.has_euler_path():" + e.has_euler_path());
        System.out.println(e.euler_path());
        //0 - 1 - 2 - 3
        boolean line[][] = {{F, T, F, F},
                {T, F, T, F},
                {F, T, F, T},
                {F, F, T, F}};
        Euler e2 = new Euler(line);
        System.out.println("e2.has_euler_path():" + e2.has_euler_path());
        System.out.println(e2.euler_path()); //Should print 0->1->2->3

        boolean empty[][] = {};
        Euler e3 = new Euler(empty);
        System.out.println("e3.has_euler_path():" + e3.has_euler_path());
        System.out.println("e3.euler_path():" + e3.euler_path());

        boolean matrix_1x1[][] = {{F}};
        Euler e4 = new Euler(matrix_1x1);
        System.out.println("e4.has_euler_path():" + e4.has_euler_path());
        System.out.println("e4.euler_path():" + e4.euler_path());

        boolean star[][] = {{F, T, T, T},
                {T, F, F, F},
                {T, F, F, F},
                {T, F, F, F}};
        Euler e5 = new Euler(star);
        System.out.println("e5.has_euler_path():" + e5.has_euler_path());
        System.out.println("e5.euler_path():" + e5.euler_path());

        boolean cycle[][] = {{F, T, F, T},
                {T, F, T, F},
                {F, T, F, T},
                {T, F, T, F}};
        Euler e6 = new Euler(cycle);
        System.out.println("e5.has_euler_path():" + e6.has_euler_path());
        System.out.println("e5.euler_path():" + e6.euler_path()); //Should print 0->3->2->1->0

        // 0 - 1
        boolean two_vertices[][] = {{F, T}, {T, F}};
        Euler e7 = new Euler(two_vertices);
        System.out.println("e5.has_euler_path():" + e7.has_euler_path());
        System.out.println("e5.euler_path():" + e7.euler_path()); //Should print 0->1


        //=======================================================================================================

//        //test 1 - Graph with 10000 Nodes , ~200000 Edges:
//        boolean[][] graph_10000 = Create_10000_Graph();
//        long start = System.currentTimeMillis();
//        Euler euler_10000 = new Euler(graph_10000);
//        for (int i = 0; i < 10; i++) {
//            String A = euler_10000.euler_path();
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("time for 10000 nodes graph is : " + (end - start) / 1000);
//
//
//        //test 2 - Graph with 30000 Nodes , ~500000 Edges:
//        boolean[][] graph_30000 = Create_30000_Graph();
//        long start_1 = System.currentTimeMillis();
//        Euler euler_30000 = new Euler(graph_30000);
//        String A = euler_30000.euler_path();
//        long end_1 = System.currentTimeMillis();
//        System.out.println("time for 30000 nodes graph is : " + (end_1 - start_1) / 1000);
//
//        //test 3 - Graph with 50000 Nodes , ~1000000 Edges
//        boolean[][] graph_50000 = Create_50000_Graph();
//        long start_2 = System.currentTimeMillis();
//        Euler euler_50000 = new Euler(graph_50000);
//        String B = euler_50000.euler_path();
//        long end_2 = System.currentTimeMillis();
//        System.out.println("time for 50000 nodes graph is : " + (end_2 - start_2) / 1000);
//
//        //test 4
//        boolean[][] big_one = new boolean[60002][60002];
//        for (int i = 1; i < 11; i++) {
//            big_one[0][i] = true;
//            big_one[i][0] = true;
//        }
//        for (int i = 1; i < 59991; i++) {
//            big_one[i][i + 10] = true;
//            big_one[i + 10][i] = true;
//        }
//        for (int i = 59991; i <= 60000; i++) {
//            big_one[i][60001] = true;
//            big_one[60001][i] = true;
//        }
//        big_one[1][12] = true;
//        big_one[12][1] = true;
//        long start_3 = System.currentTimeMillis();
//        Euler big = new Euler(big_one);
//        for (int i = 0; i < 1; i++) {
//            String C = big.euler_path();
//        }
//        long end_3 = System.currentTimeMillis();
//        System.out.println("time for 60000 nodes graph is : " + (end_3 - start_3) / 1000);
    }

    public static boolean[][] Create_10000_Graph() {
        boolean[][] ans = new boolean[10000][10000];
        int[] degrees = new int[10000];
        for (int k = 0; k < 200000; k++) {
            int i = (int) (Math.random() * 10000);
            int j = (int) (Math.random() * 10000);
            if (i != j && !ans[i][j]) {
                ans[i][j] = true;
                ans[j][i] = true;
                degrees[i]++;
                degrees[j]++;
            }
        }
        //if the graph isn't connected - add 1000 edges
        while (!is_connected(ans)) {
            for (int k = 0; k < 1000; k++) {
                int i = (int) (Math.random() * 10000);
                int j = (int) (Math.random() * 10000);
                if (i != j && !ans[i][j]) {
                    ans[i][j] = true;
                    ans[j][i] = true;
                    degrees[i]++;
                    degrees[j]++;
                }
            }
        }
        //make sure that the graph contains exactly 2 nodes with odd degree
        int odd_nodes = 0;
        Stack<Integer> odd_deg = new Stack<>();
        for (int i = 0; i < 10000; i++) {
            if (degrees[i] % 2 != 0) {
                odd_nodes++;
                odd_deg.push(i);
            }
        }
        while (odd_nodes > 2) {
            int v = odd_deg.pop();
            int u = odd_deg.pop();
            if (!ans[v][u]) {
                odd_nodes -= 2;
                ans[v][u] = true;
                ans[u][v] = true;
                degrees[u]++;
                degrees[v]++;
            } else {
                odd_nodes -= 2;
                ans[v][u] = false;
                ans[u][v] = false;
                degrees[u]--;
                degrees[v]--;
            }
        }
        return ans;
    }


    public static boolean[][] Create_30000_Graph() {
        boolean[][] ans = new boolean[30000][30000];
        int[] degrees = new int[30000];
        for (int k = 0; k < 500000; k++) {
            int i = (int) (Math.random() * 30000);
            int j = (int) (Math.random() * 30000);
            if (i != j && !ans[i][j]) {
                ans[i][j] = true;
                ans[j][i] = true;
                degrees[i]++;
                degrees[j]++;
            }
        }
        //if the graph isn't connected - add 1000 edges
        while (!is_connected(ans)) {
            for (int k = 0; k < 3000; k++) {
                int i = (int) (Math.random() * 30000);
                int j = (int) (Math.random() * 30000);
                if (i != j && !ans[i][j]) {
                    ans[i][j] = true;
                    ans[j][i] = true;
                    degrees[i]++;
                    degrees[j]++;
                }
            }
        }
        //make sure that the graph contains exactly 2 nodes with odd degree
        int odd_nodes = 0;
        Stack<Integer> odd_deg = new Stack<>();
        for (int i = 0; i < 30000; i++) {
            if (degrees[i] % 2 != 0) {
                odd_nodes++;
                odd_deg.push(i);
            }
        }
        while (odd_nodes > 2) {
            int v = odd_deg.pop();
            int u = odd_deg.pop();
            if (!ans[v][u]) {
                odd_nodes -= 2;
                ans[v][u] = true;
                ans[u][v] = true;
                degrees[u]++;
                degrees[v]++;
            } else {
                odd_nodes -= 2;
                ans[v][u] = false;
                ans[u][v] = false;
                degrees[u]--;
                degrees[v]--;
            }
        }
        return ans;
    }


    public static boolean[][] Create_50000_Graph() {
        boolean[][] ans = new boolean[50000][50000];
        int[] degrees = new int[50000];
        for (int k = 0; k < 1000000; k++) {
            int i = (int) (Math.random() * 50000);
            int j = (int) (Math.random() * 50000);
            if (i != j && !ans[i][j]) {
                ans[i][j] = true;
                ans[j][i] = true;
                degrees[i]++;
                degrees[j]++;
            }
        }
        //if the graph isn't connected - add 1000 edges
        while (!is_connected(ans)) {
            for (int k = 0; k < 3000; k++) {
                int i = (int) (Math.random() * 50000);
                int j = (int) (Math.random() * 50000);
                if (i != j && !ans[i][j]) {
                    ans[i][j] = true;
                    ans[j][i] = true;
                    degrees[i]++;
                    degrees[j]++;
                }
            }
        }
        //make sure that the graph contains exactly 2 nodes with odd degree
        int odd_nodes = 0;
        Stack<Integer> odd_deg = new Stack<>();
        for (int i = 0; i < 50000; i++) {
            if (degrees[i] % 2 != 0) {
                odd_nodes++;
                odd_deg.push(i);
            }
        }
        while (odd_nodes > 2) {
            int v = odd_deg.pop();
            int u = odd_deg.pop();
            if (!ans[v][u]) {
                odd_nodes -= 2;
                ans[v][u] = true;
                ans[u][v] = true;
                degrees[u]++;
                degrees[v]++;
            } else {
                odd_nodes -= 2;
                ans[v][u] = false;
                ans[u][v] = false;
                degrees[u]--;
                degrees[v]--;
            }
        }
        return ans;
    }

    public static boolean is_connected(boolean[][] mat) {
        int n = mat.length;
        int[] visit = new int[n];
        Queue<Integer> q = new LinkedList<>();
        q.add(0);
        int counter = 1;
        visit[0] = 1;
        while (!q.isEmpty()) {
            int v = q.poll();
            for (int u = 0; u < n; u++) {
                if (visit[u] == 0 && u != v && mat[v][u]) {
                    counter++;
                    visit[u] = 1;
                    q.add(u);
                }
            }
        }
        if (counter == n) {
            return true;
        }
        return false;
    }
}
