import java.util.Objects;

public class CodeGenerator {

    public static void main(String[] args) {

    }

    public static void init_code_generator (Tree ast, SymbolTableTree symbolTableTree) {

    }

    public void generateCode() {
        // Traverse the AST and generate op codes
        //traverseAST(this.ast.getRoot(), 0);
    }

    private void traverseAST(Tree.Node node, int scopeId) {
        if (Objects.equals(node.getName(), "leaf")) {
            // Process leaf node (symbol)
            //Symbol symbol = symbolTableTree.getSymbolTable(scopeId).getSymbol(node.getName());
            // TODO: generate op codes for symbol
        } else {
            // Process non-leaf node (operator)
            /*if (operator != null) {

                // TODO: generate op codes for operator
            } else {
                // TODO: handle error (invalid operator)
            }*/
            // Process children nodes (expressions)
            for (int i = 0; i < node.getChildren().size(); i++) {
                traverseAST(node.getChildren().get(i), scopeId);
            }
        }
    }


    private void setZFlag(int value) {
        Memory memory = new Memory();
        if (value == 0) {
            memory.setZFlag(1);
        }
    }


}
