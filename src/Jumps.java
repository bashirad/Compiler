import java.util.HashMap;
import java.util.Map;

public class Jumps {
    private Map<String, Integer> jumpTable;

    public Jumps() {
        jumpTable = new HashMap<>();
    }

    public void addJump(String jumpName, int distance) {
        jumpTable.put(jumpName, distance);
    }

    public int getJumpDistance(String jumpName) {
        if (jumpTable.get(jumpName) < 10 ) {
            return jumpTable.get(jumpName);
        }
        return jumpTable.get(jumpName);
    }

    public void setJumpDistance(String jumpName, int distance) {
        jumpTable.remove(jumpName);
         jumpTable.put(jumpName, distance);
    }

    public void printTable() {
        System.out.println("+------------------+----------+");
        System.out.println("| Jump Name        | Distance |");
        System.out.println("+------------------+----------+");
        for (Map.Entry<String, Integer> entry : jumpTable.entrySet()) {
            System.out.printf("| %-16s | %8d |%n", entry.getKey(), entry.getValue());
        }
        System.out.println("+------------------+----------+");
    }


    public boolean hasJump(String jameName) {
        return jumpTable.containsKey(jameName);
    }

    public void removeJump(String jameName) {
        jumpTable.remove(jameName);
    }

    public int size() {
        return jumpTable.size();
    }
}
