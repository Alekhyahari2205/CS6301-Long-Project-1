package lp1;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;

public class RMQFischerHeun implements RMQStructure {
    int blockSize;
    RMQSparseTable rmqBlock;
    HashMap<Integer, RMQSparseTableIndex> rmqs;
    HashMap<Integer, int[]> blocks;
    int[] encoding;

    private int cartesianEncoding(int[] block) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        int encoding = 0;
        for (int num : block) {
            if (stack.isEmpty()) {
                stack.push(num);
                encoding |= 1;
            }  else {
                if (stack.peekLast() < num) {
                    stack.push(num);
                    encoding = encoding << 1;
                    encoding |= 1;
                } else {
                    while(!stack.isEmpty() && stack.peekLast() > num) {
                        stack.pop();
                        encoding = encoding << 1;
                    }
                    stack.push(num);
                    encoding = encoding << 1;
                    encoding |= 1;
                }
            }
        }
        return encoding;
    }

    @Override
    public void preProcess(int[] arr) {
        int len = arr.length;
        blockSize = (int) (Math.log(len)/(2 * Math.log(4)));
        int nBlock = len / blockSize;
        if (len % blockSize != 0) nBlock++;
        int[] blockMinima = new int[nBlock];

        Arrays.fill(blockMinima, Integer.MAX_VALUE);
        for (int i = 0, idx; i < len; i++) {
            idx = i / blockSize;
            blockMinima[idx] = Math.min(blockMinima[idx], arr[i]);
        }
        rmqBlock = new RMQSparseTable();
        rmqBlock.preProcess(blockMinima);
        encoding = new int[nBlock];
        rmqs = new HashMap<>(nBlock);
        blocks = new HashMap<>(nBlock);
        for (int i = 0; i < nBlock; i++) {
            blocks.put(i, Arrays.copyOfRange(arr, i * blockSize, ((i+1) * blockSize)));
            encoding[i] = cartesianEncoding(blocks.get(i));
            if (!rmqs.containsKey(encoding[i])) {
                RMQSparseTableIndex rmq = new RMQSparseTableIndex();
                rmq.preProcess(blocks.get(i));
                rmqs.put(encoding[i], rmq);
            }

        }
    }

    @Override
    public int query(int[] arr, int i, int j) {
        int min = Integer.MAX_VALUE;
        int blockIndex = i / blockSize;
        if (i > blockIndex * blockSize) {
            min = Math.min(rmqs.get(encoding[blockIndex]).query(blocks.get(blockIndex),i - blockIndex*blockSize, blockSize - 1), min);
            blockIndex++;
        }
        int left = blockIndex;
        int right = j / blockSize;

        if (right * blockSize <= j) {
            if (left <= right -1)
                min = Math.min(min, rmqBlock.query(arr, left, right-1));
            min = Math.min(min, rmqs.get(encoding[right]).query(blocks.get(right), 0, j - right*blockSize));
        } else {
            min = Math.min(min, rmqBlock.query(arr, left, right));
        }
        return min;
    }
}
