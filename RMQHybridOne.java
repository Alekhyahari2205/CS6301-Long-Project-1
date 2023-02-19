package lp1;

/**
 * Top level uses sparse table
 * No any preprocess on block level.
 */
public class RMQHybridOne implements RMQStructure {

	protected int block_size;
	protected int[] block_minima;
	protected int[][] sparse_table;
    RMQSparseTable rmqBlock;

    public RMQHybridOne() {
    }

    protected double getBlockSize(double value) {
        return Math.log(value) / Math.log(2);
    }

    // Used to construct sparse table
    protected double log2(double value) {
        return Math.log(value) / Math.log(2);
    }

    @Override
    public void preProcess(int[] arr) {
    	int length = arr.length;
		block_size = (int)(Math.log(length) / Math.log(2));
		int no_blocks = (int) Math.ceil((double)(length)/block_size);
		block_minima = new int[no_blocks];

        // Scan arr to get topArr
		for(int i = 0, b = 0; i < length; i += block_size, b++) {
			int min = arr[i];
			for(int j = i; j < Math.min(i+block_size, length); j++) {
				min = Math.min(min, arr[j]);
			}
			block_minima[b] = min;
		}
		

        rmqBlock = new RMQSparseTable();
        rmqBlock.preProcess(block_minima);

    }

    @Override
    public int query(int[] arr, int i, int j)  {
        int left_top_index = i/block_size;
		int right_top_index = j/block_size;

        // Get block level min left & right
        int minLeftBlock = getMinLeftBlockLevel(arr, i, left_top_index);
        int minRightBlock = getMinRightBlockLevel(arr, j, right_top_index);
        
        int minimum = Math.min(minLeftBlock, minRightBlock);
        
        if (left_top_index + 1 > right_top_index) {
        	return minimum;
        }

        int minTopLevel = getTopLevelMin(arr, left_top_index + 1, right_top_index - 1);
        
        minimum = Math.min(minimum, minTopLevel);
        
        return minimum;
    }

    // Get the top level minimum of blocks between i and j (exclusive)
    protected int getTopLevelMin(int[] arr, int i, int j) {
    	return rmqBlock.query(arr, i, j);
    }

    // Get the block level minimum of i block
    protected int getMinLeftBlockLevel(int[] arr, int index, int block_index) {
    	int end_index = (block_index + 1) * block_size - 1;
    	int minimum = arr[index];
    	
    	for(int i = index; i <= end_index; i++) {
    		minimum = Math.min(minimum, arr[i]);
    	}
    	
    	return minimum;
    }

    // Get the block level minimum of j block
    protected int getMinRightBlockLevel(int[] arr, int index, int block_index) {
    	int start_index = (block_index) * block_size;
    	int minimum = arr[start_index];
    	
    	for(int i = start_index; i <= index; i++) {
    		minimum = Math.min(minimum, arr[i]);
    	}
    	
    	return minimum;
    }
}
