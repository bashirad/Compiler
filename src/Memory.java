import java.util.Objects;

public class Memory {
    private int heapStartCol;
    private int heapStartRow;
    public String[][] memoryArray;
    private int stackPointer;
    private int heapPointer;
    private int zFlag;

    public Memory() {
        this.memoryArray = new String[32][8];
        this.stackPointer = 0;
        this.heapPointer = 0;
        this.zFlag = 0;
        this.heapStartRow = 31;
        this.heapStartCol = 7;
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
        if (this.stackPointer < 256) {
            int row = this.stackPointer / 8;
            int col = this.stackPointer % 8;
            this.memoryArray[row][col] = value;
            this.stackPointer++;

        } else {
            throw new RuntimeException("Stack overflow!");
        }
    }



    public String [] addToHeap(String str) {
        String [] result = new String [2];
        int stringLength = str.length() + 1;
        int space = 0;
        int beginRow = 0;
        int beginCol = 0;

        for (int i = heapStartRow; i > 0; i--) {
            for (int j = heapStartCol; j > 0; j--) {
                if (Objects.equals(memoryArray[i][j], "00")) {
                    space++;
                    if (space == stringLength) {
                        beginRow = i;
                        beginCol = j;
                        break;
                    }
                }
            }
        }

        result[0] = String.valueOf(beginRow);
        result[1] = String.valueOf(beginCol);
        
        return result;
    }

    public void copyStringToMemory(String str, String beginRow, String beginCol) {
        int currentRow = Integer.parseInt(beginRow);
        int currentCol = Integer.parseInt(beginCol);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            String hex = Integer.toHexString((int) c).toUpperCase();
            memoryArray[currentRow][currentCol] = hex;
            currentCol++;
            if (currentCol == 8) {
                currentRow++;
                currentCol = 0;
            }
        }
        memoryArray[currentRow][currentCol] = "00";

    }

    public String getHexStringFromMemoryAddress(int row, int col) {
        int address = row * 8 + col;
        String hexString = Integer.toHexString(address).toUpperCase();
        return hexString;
    }
    public String getStringAddress(String value) {
            // Convert the string to a hex string
            String hexString = "";
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                String hex = Integer.toHexString((int) c).toUpperCase();
                hexString += hex;
            }
            hexString += "00";

            // Add the hex string to the heap
           // return addToHeap(hexString);
        return "test";
        }


    public int getCurrentStackLocation() {
        return this.stackPointer;
    }

    public int getCurrentHeapLocation() {
        return this.heapPointer;
    }
}
