import java.util.ArrayList;
import java.util.Objects;

public class Tree {
    // Attributes
    public Node root; // Note the NULL root node of this tree.
    public Node cur; // Note the EMPTY current node of the tree we're building.

    // Methods

    // Constructor
    public Tree() {
        this.root = null;
        this.cur = null;
    }

    //      this method clears the tree after use
    public void clear() {
        this.root = null;
        this.cur = null;
    }


    //     public void addNode(String name, String kind, Tokens token) {
    // Add a node: kind in {branch, leaf}.
    //     public void addNode(String name, String kind, Tokens token) {
    // Add a node: kind in {branch, leaf}.
    public void addNode(String name, String kind, Tokens token) {
        // Construct the node object.
        Node node = new Node(name, kind);

        // Check to see if it needs to be the root node.
        if (this.root == null) {
            // We are the root node.
            this.root = node;
            node.setParent(null);
        } else {
            // We are the children.
            // Make our parent the current node...
            node.setParent(this.cur);
            // ... and add ourselves (via the unfortunately-named
            // "add" function) to the children ArrayList of the current node.
            this.cur.addChild(node);
        }

        // If we are an interior/branch node, then...
        if (!kind.equals("leaf")) {
            // ... update the current node pointer to ourselves.
            this.cur = node;
        } else {
            // Add the token as a child node of the current leaf node.
            node.setToken(token);
        }
    }

    // Note that we're done with this branch of the tree...
    public void moveUp() {
        // ... by moving "up" to our parent node (if possible).
        if (this.cur.getParent() != null) {
            this.cur = this.cur.getParent();
        } else {
            // This really should not happen, but it will, of course.
            System.out.println("ERROR: can't move up to the parent node");
        }
    }
    // Node class
    public class Node {
        // Attributes
        private String name;
        private String kind;
        private ArrayList<Node> children;
        private Node parent;
        private Tokens token;



        // Constructor
        public Node(String name, String kind) {
            this.name = name;
            this.kind = kind;
            this.children = new ArrayList<Node>();
            this.parent = null;
            this.token = null;
        }

        // Getter and Setter methods
        public String getName() {
            return this.name;
        }

        public String getKind() {
            return this.kind;
        }

        public ArrayList<Node> getChildren() {
            return this.children;
        }

        public Node getParent() {
            return parent;
        }
        public Tokens getTokens() {
            return this.token;
        }

        public void addChild(Node node) {
            this.children.add(node);
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public void setToken(Tokens token) {
            this.token = token;
        }
    }


    // Return a string representation of the tree.
    public void print(Tree cst) {

        // Initialize the result string.
        StringBuilder traversalResult = new StringBuilder();

        if (cst.root != null) {
            // Recursive function to handle the expansion of the nodes.
            expand(cst.root, 0, traversalResult);

            // Return the result.
            System.out.println(traversalResult);
        } else {
            System.out.println("Root is null. I am sorry");
        }
    }

    private void expand(Node node, int depth, StringBuilder traversalResult) {

        // Space out based on the current depth so
        // this looks at least a little tree-like.
        traversalResult.append("-".repeat(Math.max(0, depth)));

        // If there are no children (i.e., leaf nodes)...
        if (Objects.equals(node.getKind(), "leaf")) {
            // ... note the leaf node.
            traversalResult.append("[ ").append(node.getName()).append(" ]");
            traversalResult.append("\n");
        } else {
            // There are children, so note these interior/branch nodes and ...
            traversalResult.append("<").append(node.getName()).append("> \n");
            // ... recursively expand them.
            for (int i = 0; i < node.getChildren().size(); i++) {
                expand(node.getChildren().get(i), depth + 1, traversalResult);
            }
        }
    }




}


    /**
     *
     *
     * OLD VERSION - KEEP BEFORE IMPLEMENTING THE NEW ONE
     * • build the symbol table (a tree of hash tables)
     * • check scope
     * • check type
     */


