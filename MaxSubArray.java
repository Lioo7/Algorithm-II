import java.util.Arrays;

public class MaxSubArray {
    public static int[] best(int[] arr){
        int len = arr.length;
        boolean flag = true;

        //===case #1===

        // iterates over the array and checks if it contains a negative number
        for (int i = 0; i < len; i++) {
            if (arr[i] < 0){
                flag = false;
                break;
            }
        }
        // if all the number in the array are not negative then return the original array
        if(flag) return arr;

        //===case #2===

        // create a matrix
        int[][] mat = new int[len][len];

        // fill the main diagonal
        for (int i = 0; i < len; i++) {
            mat[i][i] = arr[i];
        }

        // fill the rest of the matrix
        for (int i = 0; i < len; i++) {
            for (int j = i+1; j < len; j++) {
                mat[i][j] = mat[i][j-1] + mat[j][j]; // sum the previous cell and the cell at the main diagonal
            }
        }

        // search for the max value in the matrix
        int max = arr[0];
        int subSize = 0;
        int firstIndex = 0;
        for (int i = 0; i < len; i++) {
            for (int j = i+1; j < len; j++) {
                if(mat[i][j] > max){
                    max = mat[i][j];
                    subSize = j-i+1;
                    firstIndex = i;
                }
            }
        }
        
        // build the max sub array 
        int[] ans = new int[subSize];
        
        // fill the max sub array
        int j = 0;
        int k = firstIndex;
        int count = 0;

        while (count < subSize){
            ans[j] = arr[k];
            j++;
            k++;
            count++;
        }

        // print the matrix
//        for (int i=0; i<mat.length; i++){
//            System.out.println(Arrays.toString(mat[i]));
//        }

        return ans;
    }

    public static void main(String[] args) {
        int[] arr = {-10, 2, 4, 3, -3, 2};
        int[] arr2 = {10, 2, 4, 3, 3};
        int[] arr3 = {-10, -2, -4, -3, -6};

        // print the answer
        System.out.println(Arrays.toString(best(arr)));
    }
}
