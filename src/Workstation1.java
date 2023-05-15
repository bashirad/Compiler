import java.util.ArrayList;
import java.util.List;

public class Workstation1 {

    /*
            while (row >= 0) {
            if (this.memoryArray[row][col].equals("00")) {
                // Check if there's enough space in the heap
                int remaining = str.length();
                for (int i = col; i < 8 && remaining > 0; i++) {
                    if (this.memoryArray[row][i].equals("00")) {
                        remaining--;
                    } else {
                        break;
                    }
                }
                for (int i = row - 1; i >= 0 && remaining > 0; i--) {
                    for (int j = 7; j >= 0 && remaining > 0; j--) {
                        if (this.memoryArray[i][j].equals("00")) {
                            remaining--;
                        } else {
                            remaining = -1;
                            break;
                        }
                    }
                }

                // If there's enough space, write the string to the heap and return its address
                if (remaining == 0) {
                    if (str.length() +1 > 7) {
                        row--;
                    } else {
                        col = col - str.length();
                    }
                    for (int i = 0; i < str.length(); i++) {
                        this.memoryArray[row][col] = Integer.toHexString((int) str.charAt(i)).toUpperCase();
                        col++;
                        if (col == 8) {
                            row--;
                            col = 0;
                        }
                    }
                    this.memoryArray[row][col] = "00";

                    int stringLength = str.length() + 1;
                    if (col > stringLength) {
                        col = col - stringLength;
                    } else {
                        row--;
                        stringLength = stringLength - 8;
                    }
                    this.heapStartRow = row - str.length()+1;
                    this.heapStartCol = col;
                    int address = row * 8 + col + 256;
                    return Integer.toHexString(address).toUpperCase();
                }
            }
            col--;
            if (col < 0) {
                row--;
                col = 7;
            }
        }
     */



}
