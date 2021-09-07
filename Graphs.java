import java.util.Arrays;

public class Graphs { // *Undirected graphs*
    //==============================================Tirgul 2============================================================
    /**
     * Complexity: o(n^3)
     * The function marks all the vertices in the matrix with a direct or indirect connection
     * @param adjacencyMatrix
     */
    public static void floydWarshall(int[][] adjacencyMatrix){
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                for (int k = 0; k < adjacencyMatrix.length; k++) {
                    // Checks if there is a direct or indirect connection between both of the vertices
                    if(adjacencyMatrix[i][k]==1 && adjacencyMatrix[k][j]==1 || adjacencyMatrix[i][j]==1)
                        adjacencyMatrix[i][j] = 1;
                }
            }
        }
    }

    private static void print(int[][] matrix){
        // prints the matrix
        for (int i=0; i<matrix.length; i++){
            System.out.println(Arrays.toString(matrix[i]));
        }
    }

    /**
     * Complexity: o()
     * The function arrange the given matrix
     * @param adjacencyMatrix
     */
    public static void arrange(int[][] adjacencyMatrix){
    }

    /**
     * Complexity: o(1)
     * @param adjacencyMatrix
     * @param src
     * @param dest
     * @return True if there is a path between given vertices
     */
    public static boolean path(int[][] adjacencyMatrix, int src, int dest){
        floydWarshall(adjacencyMatrix);
        if(adjacencyMatrix[src][dest] == 1) return true;
        else return false;
    }

    /**
     * Complexity: o(n)
     * @param adjacencyMatrix
     * @return True if the given matrix is connected
     */
    public static boolean isConnected(int[][] adjacencyMatrix){
        boolean ans = true;
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if(adjacencyMatrix[0][i] != 1){
                ans = false;
                break;
            }
        }
        return ans;
    }

    /**
     * Complexity: o(n^2)
     * @param adjacencyMatrix
     * @return How many connected components there are in the given matrix
     */
    public static int connectedComponents(int[][] adjacencyMatrix){
        floydWarshall(adjacencyMatrix);
        int[] arr = new int[adjacencyMatrix.length];
        int ans = 0;

        for (int i = 0; i < arr.length; i++) {
            if(arr[i] == 0){
                findNeighbours(adjacencyMatrix, arr, i);
                ans++;
            }
        }
        return ans;
    }

    /**
     * Complexity: o(n)
     * The function finds all the neighbors of the given vertex and marks in the arr
     * @param adjacencyMatrix
     * @param arr - the utility array
     * @param i - the current vertex in the arr
     */
    private static void findNeighbours(int[][] adjacencyMatrix, int[] arr, int i) {
        for (int j = 0; j < adjacencyMatrix.length; j++) {
            if(adjacencyMatrix[i][j] == 1){
                arr[j] = 1;
            }
        }
    }

    /**
     * Complexity: o(n^2)
     * @param adjacencyMatrix
     * @return a string of all the connected components there are in the given matrix
     */
    public static String verticesComponents(int[][] adjacencyMatrix){
        int[] arr = new int[adjacencyMatrix.length];
        String ans = "| ";

        for (int i = 0; i < arr.length; i++) {
            if(arr[i] == 0){
                ans += findNeighboursString(adjacencyMatrix, arr, i);
                ans += " | ";
            }
        }
        return ans;
    }

    /**
     * Complexity: o(n)
     * The function finds all the neighbors of the given vertex, marks in the arr
     * and adds the vertex to the given string
     * @param adjacencyMatrix
     * @param arr - the utility array
     * @param i - the current vertex in the arr
     * @return a string of all the connected components there are in the given vertex
     */
    private static String findNeighboursString(int[][] adjacencyMatrix, int[] arr, int i) {
        String ans = "";
        for (int j = 0; j < adjacencyMatrix.length; j++) {
            if(adjacencyMatrix[i][j] == 1){
                arr[j] = 1;
                ans += String.valueOf(j);
            }
        }
        return ans;
    }

    /**
     * Complexity: o(n^3)
     * @param originalMat
     * @return a matrix that contains all the paths
     */
    public static String[][] pathsMatrix(int[][] originalMat){
        String[][] pathsMat = new String[originalMat.length][originalMat.length];
        String temp;

        for (int i = 0; i < originalMat.length; i++) {
            for (int j = 0; j < originalMat.length; j++) {
                    // Checks if the source and the destination are equals
                    if(i == j){
                        temp = String.valueOf(i);
                        pathsMat[i][j] = temp;
                    }
                    // Checks if there is a direct connection between both of the vertices
                    else if(originalMat[i][j]==1){
                        temp = String.valueOf(i) + "->" +String.valueOf(j);
                        pathsMat[i][j] = temp;
                    }
                    else{
                        pathsMat[i][j] = "";
                    }
            }
        }

        for (int i = 0; i < originalMat.length; i++) {
            for (int j = 0; j < originalMat.length; j++) {
                for (int k = 0; k < originalMat.length; k++) {
                    /* Checks if there is an indirect connection between both of the vertices
                    and there is a direct connection and if this is not the main diagonal
                     */
                    if (i!=j && originalMat[i][k]==1 && originalMat[k][j]==1 && originalMat[i][j]!=1) {
                        pathsMat[i][j] += pathsMat[i][k] + "->" + pathsMat[k][j].substring(3);
                        originalMat[i][j] = 1;
                    }
                }
            }
        }

        // prints the matrix
        for (int i=0; i<pathsMat.length; i++){
            System.out.println(Arrays.toString(pathsMat[i]));
        }
        return pathsMat;
    }

    private static int indexRow(int i, int j, int n) {return (n + 1) * i + j;}
    private static int k_to_i(int k, int n) {return k/(n+1);}
    private static int k_to_j(int k, int n) {return k%(n+1);}

    /**
     * Complexity:
     * @param src_x the x of the source
     * @param src_y the y of the source
     * @param dest_x the x of the destination
     * @param dest_y the y of the destination
     * @param small_bottle the size of the small bottle
     * @param big_bottle the size of the big bottle
     * @return true iff there is a path, if yes print the path
     */
    public static boolean bottlesPath(int src_x, int dest_x, int src_y, int dest_y, int small_bottle, int big_bottle) {
        // Creates a new matrix according to the given bottles
        int[][] originalMat = BottlesProblem.matrix_create(small_bottle, big_bottle);
        // Creates the paths matrix
        String[][] pathsMat = pathsMatrix(originalMat);
        boolean ans = false;
        String bottle_path = "";
        int k1, k2;
        // Converts the src and the dest to indexes
        k1 = indexRow(src_x, dest_x, big_bottle);
        k2 = indexRow(src_y, dest_y, big_bottle);

        // Checks if there is a path in the paths matrix according to the indexes
        if(pathsMat[k1][k2] != ""){
            ans = true;
            String path = pathsMat[k1][k2];
            // Converts the matrix path to a bottle path
            System.out.println("matrix path: " + path);
            /*
            Goes over the path and takes only the numbers
            Converts each number back from k to i and j
             */
            for (int i = 0; i < path.length(); i++) {
                if(path.charAt(i) != '-' && path.charAt(i) != '>'){
                    String num = "";
                    while(i < path.length() && path.charAt(i) != '-'){
                        num += path.charAt(i++);
                    }
                    int left_bottle = k_to_i( Integer.parseInt(num), big_bottle);
                    int right_bottle = k_to_j( Integer.parseInt(num), big_bottle);
                    bottle_path += "[" + left_bottle + "," + right_bottle + "]" + "->";
                }
            }
            System.out.println("bottles path: " + bottle_path.substring(0, bottle_path.length()-2));
        }
        else{
            System.out.println("Not exist any path");
        }

        return ans;
    }

    //==============================================Tirgul 3============================================================

    /**
     *
     * @param adjacencyMatrix
     * @param weightArr
     * @return
     */
    public static int[][] distanceFindingWeightedVertices(int[][] adjacencyMatrix, int[] weightArr){
        int len = adjacencyMatrix.length; // create a new matrix
        int[][] distanceMatrix = new int[len][len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                // if there is an edge, calculates the weight of both of the vertices
                if(adjacencyMatrix[i][j] == 1){
                    distanceMatrix[i][j] = weightArr[i] + weightArr[j];
                }
            }
        }

        // ruining the FW algorithm on the distanceMatrix
        floydWarshallDistMat(distanceMatrix);

        // fixing the matrix

        return distanceMatrix;
    }

    private static void floydWarshallDistMat(int[][] distanceMatrix){
        // complexity: n^3
        int inf = Integer.MAX_VALUE;
        int len = distanceMatrix.length;

        for (int k = 0; k < len; k++) {
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    if(distanceMatrix[i][k] != inf && distanceMatrix[k][j] != inf)
                    distanceMatrix[i][j] = Math.min(distanceMatrix[i][k] + distanceMatrix[k][j], distanceMatrix[i][j]);
                }
            }
        }
    }

    //==============================================Tirgul 4============================================================
//    /**
//     *
//     * @param matrix
//     * @return
//     */
//    public static boolean isNegativeCircle(int[][] matrix){
//        boolean ans = false;
//
//        if(!isDirected(matrix)){ // undirected, complexity: n^3
//            floydWarshall(matrix);
//            for (int i = 0; i < matrix.length; i++) {
//                for (int j = 0; j < matrix.length; j++) {
//                    if (matrix[i][j] < 0) return true;
//                }
//            }
//        }
//
//        else{ // directed, complexity: n^2
//            for (int i = 0; i < matrix.length; i++) {
//                for (int j = i; j < matrix.length; j++) {
//                    if (matrix[i][j] < 0) return true;
//                }
//            }
//        }
//
//        return ans;
//    }
//
//    private static boolean isDirected(int[][] matrix) {
//    }


    public static void main(String[] args) {
        int[][] mat = {{0,1,1,0,0,0,0},
                {1,0,1,0,0,0,0},
                {1,1,0,0,0,0,0},
                {0,0,0,0,1,0,1},
                {0,0,0,1,0,1,0},
                {0,0,0,0,1,0,1},
                {0,0,0,1,0,1,0}};

        int[][] matPaths = {{1,0,0,1,0,1,0},
                {0,1,1,0,1,0,1},
                {0,1,1,0,1,0,1},
                {1,0,0,1,0,1,0},
                {0,1,1,0,1,0,1},
                {1,0,0,1,0,1,0},
                {0,1,1,0,1,0,1}};

        int[][] threePaths = {{1,0,0},
                {0,1,0},
                {0,0,1},};

//              0---1
//             /     \
//            5       2
//             \     /
//              4---3
        int[][] matPaths3 = {{1,1,0,0,0,1},
                {1,1,1,0,0,0},
                {0,1,1,1,0,0},
                {0,0,1,1,1,0},
                {0,0,0,1,1,1},
                {1,0,0,0,1,1}};

        int inf = Integer.MAX_VALUE;

        int[][] matAJ = {{0,1,inf,1},
                         {1,0,1,inf},
                         {inf,1,0,1},
                         {1,inf,1,0}};

        int[] wArr = {4,3,2,5};

        //         4
        //    V0---→--V1
        //      |     |
        //   3  ↑     ↓ -9
        //	    |__←__|
        //     V3  1  V2
        //
        int[][] arr = {{0,4,Integer.MAX_VALUE,Integer.MAX_VALUE},
                {Integer.MAX_VALUE,0,-9,Integer.MAX_VALUE},
                {Integer.MAX_VALUE,Integer.MAX_VALUE,0,1},
                {3,Integer.MAX_VALUE,Integer.MAX_VALUE,0}};

        int[][] arr2 = {{0,0,0,0},
                {0,0,0,0},
                {0,0,0,0},
                {0,0,0,0}};


//        floydWarshall(matPaths3);
//        System.out.println(path(mat, 0, 1));
//        System.out.println(isConnected(mat));
//        System.out.println(connectedComponents(threePaths));
//        System.out.println(verticesComponents(threePaths));
//        pathsMatrix(matPaths3);
//        System.out.println(bottlesPath(0, 0, 1, 1, 1, 2));
//        System.out.println(isNegativeCircle(arr));
        print(distanceFindingWeightedVertices(matAJ, wArr));
//        floydWarshallDistMat(arr);
//        print(arr);
    }
}
