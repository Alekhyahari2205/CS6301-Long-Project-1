package lp1;

public class RMQFullTable implements RMQStructure {
    int[][] fullTable;
    @Override
    public void preProcess(int[] arr) {
        int n = arr.length;
        fullTable = new int[n][n];
        for (int i = 0; i < n; i++) {
            fullTable[i][i] = arr[i];
            for (int j = i+1; j <n ;j++) {
                fullTable[i][j] = Math.min(arr[j], fullTable[i][j-1]);
            }
        }
    }

    @Override
    public int query(int[] arr, int i, int j) {
        return fullTable[i][j];
    }
}
