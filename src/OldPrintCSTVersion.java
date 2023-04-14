public class OldPrintCSTVersion extends Tree {
    public OldPrintCSTVersion(Tree myTree) {
        // Copy the nodes of the original tree to the new tree
        this.root = myTree.root;
        this.cur = myTree.cur;
    }

    // Return a string representation of the tree.
    public void print() {

        Tree cst = new Tree();

        // Initialize the result string.
        StringBuilder traversalResult = new StringBuilder();

        if (this.root != null) {
            // Recursive function to handle the expansion of the nodes.
            expand(this.root, 0, traversalResult);

            // Return the result.
            System.out.println(traversalResult);
        } else {
            System.out.println("Root is null. I am sorry");
        }
    }

    private void expand(Node node, int depth, StringBuilder traversalResult) {
        // Space out based on the current depth so
        // this looks at least a little tree-like.
        for (int i = 0; i < depth; i++) {
            traversalResult.append("-");
        }

        // If there are no children (i.e., leaf nodes)...
        if (node.getChildren() == null) {
            // ... note the leaf node.
            traversalResult.append("[").append(node.getName()).append("]");
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
