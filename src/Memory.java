public class Memory {
    private int[][] stack;
    private int[][] heap;
    private int stackPointer;
    private int heapPointer;
    private int zFlag;

    public Memory() {
        this.stack = new int[32][8];
        this.heap = new int[32][8];
        this.stackPointer = 0;
        this.heapPointer = 0;
        this.zFlag = 0;
    }

    public void setZFlag(int set) {
        this.zFlag = set;
    }

    public void addToStack(int value) {
        if (this.stackPointer < 32 * 8) {
            int row = this.stackPointer / 8;
            int col = this.stackPointer % 8;
            this.stack[row][col] = value;
            this.stackPointer++;
        } else {
            throw new RuntimeException("Stack overflow!");
        }
    }

    public void addToHeap(int value) {
        if (this.heapPointer < 32 * 8) {
            int row = this.heapPointer / 8;
            int col = this.heapPointer % 8;
            this.heap[row][col] = value;
            this.heapPointer++;
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
