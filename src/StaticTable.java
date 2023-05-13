import java.util.ArrayList;

public class StaticTable {
    private ArrayList<String[]> table;

    public StaticTable() {
        this.table = new ArrayList<>();
    }

    public void addEntry(String temp, String variable, int address) {
        String[] entry = {temp, variable, Integer.toHexString(address)};
        this.table.add(entry);
    }

    public void printTable() {
        System.out.println("Temp\tVariable\tAddress");
        for (String[] entry : this.table) {
            System.out.println(entry[0] + "\t" + entry[1] + "\t\t" + entry[2]);
        }
    }
}
