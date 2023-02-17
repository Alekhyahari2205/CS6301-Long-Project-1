package lp1;

public class RMQSparseTable implements RMQStructure {
    private int[][] sparseTable;

    @Override
    public void preProcess(int[] arr) {
        int n = arr.length;
        // find max range size possible, i.e. k
        int k = (int) Math.floor(Math.log(n) / Math.log(2));
        sparseTable = new int[n][k+1];
        //fill the sparse table
        // range of 2^0 for indices 0...n-1
        for(int i = 0; i < n; i++) {
            sparseTable[i][0] = arr[i];
        }
        // range from 2^1 to 2^k
        for (int j = 1; j <= k; j++) {
            int i = 0;
            int range = pow(j);
            while (i+range <= n) {
                sparseTable[i][j] = Math.min(sparseTable[i][j-1], sparseTable[i+range-pow(j-1)][j-1]);
                i++;
            }
        }
//        printSparse(sparseTable);
        return;
    }

    private void printSparse(int[][] sparseTable) {
        for (int i = 0; i < sparseTable.length; i++) {
            System.out.print(i + ":\t");
            for (int j = 0; j< sparseTable[0].length; j++)
                System.out.print(sparseTable[i][j] + "\t");
            System.out.println("");
        }
    }

    private int pow(int x) {
        return (int) Math.pow(2, x);
    }

    @Override
    public int query(int[] arr, int i, int j) {
        int queryRange = j - i + 1;
        int k = (int) Math.floor(Math.log(queryRange)/Math.log(2));
        int res = Math.min(sparseTable[i][k], sparseTable[j-pow(k)+1][k]);
        return res;
    }
}
