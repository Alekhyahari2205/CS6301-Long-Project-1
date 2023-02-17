package lp1;

import java.util.Arrays;

public class RMQBlock implements RMQStructure {

    int blockSize;
    int[] blockMinima;


    @Override
    public void preProcess(int[] arr) {
        int len = arr.length;
        blockSize = (int) Math.sqrt(len);
        int numOfBlocks = len / blockSize;
        if (len % blockSize != 0) numOfBlocks++;
        blockMinima = new int[numOfBlocks];
        Arrays.fill(blockMinima, Integer.MAX_VALUE);
        for (int i = 0, idx; i < len; i++) {
            idx = i / blockSize;
            blockMinima[idx] = Math.min(blockMinima[idx], arr[i]);
        }
    }

    @Override
    public int query(int[] arr, int i, int j) {
        int min = Integer.MAX_VALUE;
        int blockIndex = i / blockSize;
        if (i > blockIndex * blockSize) {
            for (int k = i; k < (blockIndex+1) * blockSize; k++) min = Math.min(arr[k], min);
            blockIndex++;
        }
        while((blockIndex+1) * blockSize -1 <= j)
            min = Math.min(blockMinima[blockIndex++], min);
        if (blockIndex * blockSize <= j)
            for (int k = blockIndex*blockSize; k <=j; k++) min = Math.min(min, arr[k]);
        return min;
    }
}
