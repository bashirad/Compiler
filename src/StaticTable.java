import java.util.ArrayList;
public class StaticTable {
    private final ArrayList<String[]> table;

    public StaticTable() {
        table = new ArrayList<>();
    }

    public void addEntry(String temp, String variable, int address) {
        String[] entry = {temp, variable, Integer.toHexString(address)};
        this.table.add(entry);
    }

    public String[] getVariableTempAddress(String variable) {
        String[] result = new String[2];
        for (String[] entry : this.table) {
            if (entry[1].equals(variable)) {
                result[0] = entry[0].substring(0, 2);
                result[1] = entry[0].substring(2, 4);
                return result;
            }
        }
        return null; // variable not found
    }


    public boolean containsVariable(String variable) {
        for (String[] entry : this.table) {
            if (entry[1].equals(variable)) {
                return true;
            }
        }
        return false;
    }

    public void printTable() {
        String horizontalLine = "+--------+--------------+---------+";
        System.out.println(horizontalLine);
        System.out.format("| %-6s | %-12s | %-6s |%n", "Temp", "Variable", "Address");
        System.out.println(horizontalLine);
        for (String[] entry : this.table) {
            System.out.format("| %-6s | %-12s | %-6s  |%n", entry[0], entry[1], entry[2]);
        }
        System.out.println(horizontalLine);
    }
    public int getNumTempVars() {
        int count = 0;
        for (String[] entry : this.table) {
            if (entry[0].startsWith("T")) {
                count++;
            }
        }
        return count;
    }

    public void updateAddress(int newAddress) {
        for (String[] entry : this.table) {
            entry[2] = Integer.toHexString(newAddress).toUpperCase();
            newAddress++;
        }
    }
    public int getAddress(String temp) {
        for (String[] entry : this.table) {
            if (entry[0].equals(temp)) {
                return Integer.parseInt(entry[2], 16);
            }
        }
        return -1; // variable not found
    }


}
