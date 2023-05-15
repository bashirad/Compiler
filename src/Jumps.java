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
        return jumpTable.get(jumpName);
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
