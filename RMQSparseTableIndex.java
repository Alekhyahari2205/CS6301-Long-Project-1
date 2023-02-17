package lp1;

public class RMQSparseTableIndex implements RMQStructure{

    int n;
    int[][] st;

    protected int highestK(int n) {
        int k = 0;
        int k2 = 1;
        while (k2 <= n) {
            k++;
            k2 *= 2;
        }
        return k - 1;
    }

    protected int twoK(int k) {
        int result = 1;
        for (int i = 0; i < k; i++) result *= 2;
        return result;
    }

    @Override
    public void preProcess(int[] arr) {
        n = arr.length;
        int K = highestK(n) + 1;
        st = new int[n][K];
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < n; i++) {
                if (k == 0)
                    st[i][k] = i;
                else if (i + twoK(k - 1) < n)
                    st[i][k] = arr[st[i][k - 1]] < arr[st[i + twoK(k - 1)][k - 1]] ? st[i][k-1] : st[i+twoK(k-1)][k-1];
            }
        }
    }

    public int query(int[] arr, int i, int j) {
        int k = highestK(j - i + 1);
        return Math.min(arr[st[i][k]], arr[st[j - twoK(k) + 1][k]]);
    }
}
