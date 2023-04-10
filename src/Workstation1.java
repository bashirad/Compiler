import java.util.ArrayList;
import java.util.List;

public class Workstation1 {
    private Node root;
    private Node current;

    public Workstation1() {
        this.root = null;
        this.current = null;
    }

    public void addNode(String kind, String label, Tokens token) {
        Node n = new Node();
        n.name = label;
        if (this.root == null) {
            this.root = n;
            n.parent = null;
        } else {
            n.parent = current;
            current.children.add(n);
        }
        if (!kind.equals("leaf")) {
            this.current = n;
        } else {
            n.token = token;
        }
    }

    public void moveUp() {
        this.current = this.current.parent;
    }

    private static class Node {
        private String name;
        private Node parent;
        private Tokens token;
        private List<Node> children;

        public Node() {
            this.name = "";
            this.parent = null;
            this.children = new ArrayList<>();
            this.token = null;
        }
    }
}
