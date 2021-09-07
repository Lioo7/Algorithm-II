import java.util.Arrays;
//==============================================Tirgul 1============================================================
public class BottlesProblem {
    //the index calculation for rows, converts from 2D to an array
    private static int indexRow(int i, int j, int n){
        return (n + 1) * i + j;
    }

    // Complexity: o(n^2)
    public static int[][] matrix_create(int m, int n){ // m = small bottle(rows), n = big bottle(columns)
        int dim = (m+1)*(n+1); 						// matrix dimension
        int [][]mat = new int[dim][dim];

        for (int i=0; i<=m; i++){
            for (int j=0; j<=n; j++){
                int index = indexRow(i,j,n);
                mat[index][indexRow(0,j,n)] = 1;    // 1. m empty         (i, j)  --> (0, j)
                mat[index][indexRow(m,j,n)] = 1;		 // 2. m full          (i, j) -->  (m, j)
                mat[index][indexRow(i,0,n)] = 1;   // 3. n empty          (i, j) -->  (i, 0)
                mat[index][indexRow(i,n,n)] = 1;		// 4. n full           (i, j) -->  (i, n)
                // 5. fills the water from m to n  	     (i, j) --> (max(0, i + j - n), min(n, i + j))
                int indexTo = indexRow(Math.max(0,i+j-n),Math.min(n,i+j) ,n);
                mat[index][indexTo] = 1;
                // 6. fills the water from n to m        (i, j) --> (min(m, i + j), max(0, i + j - m))
                indexTo = indexRow(Math.min(m,i+j),Math.max(0,j+i-m) ,n);
                mat[index][indexTo] = 1;
            }
        }
//         prints the matrix
        for (int i=0; i<dim; i++){
            System.out.println(Arrays.toString(mat[i]));
        }
        return mat;
    }

    public static void main(String[] args) {
        matrix_create(1, 2);
    }
}
