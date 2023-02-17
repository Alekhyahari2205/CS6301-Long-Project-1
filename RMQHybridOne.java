package lp1;

public class RMQHybridOne implements RMQStructure {
    
    protected int block_size;
	protected int[] block_minima;
	protected int[][] sparse_table;
	
	public RMQHybridOne() {
		
	}

    @Override
    public void preProcess(int[] arr) {
        int length = arr.length;
		block_size = (int)(Math.log(length) / Math.log(2));
		int no_blocks = (int) Math.ceil((double)(length)/block_size);
		block_minima = new int[no_blocks];
		
		for(int i = 0, b = 0; i < length; i += block_size, b++) {
			int min = arr[i];
			for(int j = 0; j < Math.min(i+block_size, length); j++) {
				min = Math.min(min, arr[j]);
			}
			block_minima[b] = min;
		}
		
		// Construct sparse table for block minima
		sparseTable(block_minima, no_blocks);
    }

    @Override
    public int query(int[] arr, int i, int j) {
        if(i==j) {
			return arr[i];
		}
		
		int left_top_index = i/block_size;
		int right_top_index = j/block_size;
		
		
		return 0;
    }

    public void sparseTable(int[] arr, int arr_size) {
		int rows = arr_size;
		double log_size = Math.log(arr_size) / Math.log(2);
		int columns = (int)log_size + 1;
		
		sparse_table = new int[rows][columns];
		
		// Dynamic programming for building sparse table
		// Initializing the first column
		for(int i = 0; i < rows; i++) {
			sparse_table[i][0] = arr[i];
		}
		
		for(int j = 1; j < columns; j++) {
			for(int i=0; i < rows; i++) {
				int interval = (int) (i + Math.pow(2, j-1));
				if(interval < rows && sparse_table[interval][j-1] != 0) {
					sparse_table[i][j] = Math.min(sparse_table[i][j-1], sparse_table[interval][j-1]);
				}
			}
		}
	}
	
}
