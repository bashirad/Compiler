public class Memory {
    private String[][] memoryArray;
    private int stackPointer;
    private int heapPointer;
    private int zFlag;

    public Memory() {
        this.memoryArray = new String[32][8];
        this.stackPointer = 0;
        this.heapPointer = 0;
        this.zFlag = 0;
    }

    public void initializeMem (String str) {
        // fill the array with hex numbers
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 8; j++) {
                memoryArray[i][j] = str; // this sets the value to a hex number
            }
        }
    }
    public void printMemory(){
        // print the array
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.printf("%3s ", memoryArray[i][j]);
            }
            System.out.println();
        }

    }

    public void setZFlag(int set) {
        this.zFlag = set;
    }

    public void addToStack(String value) {
        if (this.stackPointer < memoryArray.length) {
            int row = this.stackPointer / 8;
            int col = this.stackPointer % 8;
            this.memoryArray[row][col] = value;
            this.stackPointer++;
        } else {
            throw new RuntimeException("Stack overflow!");
        }
    }

    public void addToHeap(String value) {
        if (this.heapPointer < 32 * 8) {
            if (this.heapPointer < this.stackPointer) {
                int row = this.heapPointer / 8 + 32;
                int col = this.heapPointer % 8;
                this.memoryArray[row][col] = value;
                this.heapPointer++;
            } else {
                throw new RuntimeException("Heap overflow!");
            }
        } else {
            throw new RuntimeException("Heap overflow!");
        }
    }


    public int getCurrentStackLocation() {
        return this.stackPointer;
    }

    public int getCurrentHeapLocation() {
        return this.heapPointer;
    }
}
