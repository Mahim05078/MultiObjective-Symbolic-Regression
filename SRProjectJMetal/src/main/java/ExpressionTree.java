import java.util.Stack;
import java.util.Map;
import java.util.HashMap;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Function;

public class ExpressionTree {
    private Node root;

    private static final Map<String, DoubleBinaryOperator> binaryOperators;
    private static final Map<String, Function<Double, Double>> unaryOperators;

    static {
        binaryOperators = new HashMap<>();
        binaryOperators.put("+", (a, b) -> a + b);
        binaryOperators.put("-", (a, b) -> a - b);
        binaryOperators.put("*", (a, b) -> a * b);
        binaryOperators.put("/", (a, b) -> a / b);
        binaryOperators.put("^", Math::pow);

        unaryOperators = new HashMap<>();
        unaryOperators.put("sin", Math::sin);
        unaryOperators.put("cos", Math::cos);
    }

    public ExpressionTree(String expression) {
        buildTree(expression);
    }

    private class Node {
        String data;
        Node left;
        Node right;

        Node(String data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private void buildTree(String expression) {
        String[] tokens = expression.split(" ");
        Stack<Node> stack = new Stack<>();

        for (String token : tokens) {
            Node newNode = new Node(token);

            if (isOperator(token)) {
                Node right = stack.pop();
                Node left = stack.pop();
                newNode.right = right;
                newNode.left = left;
            }
            stack.push(newNode);
        }
        root = stack.pop();
    }

    public void simplify() {
        root = simplify(root);
    }

    private Node simplify(Node node) {
        if (node == null) {
            return null;
        }

        if (isOperator(node.data)) {
            node.left = simplify(node.left);
            node.right = simplify(node.right);

            if (node.left != null && node.right != null) {
                double leftValue = parseOperand(node.left.data);
                double rightValue = parseOperand(node.right.data);

                if (isNumeric(node.left.data) && isNumeric(node.right.data)) {
                    double result = performOperation(node.data, leftValue, rightValue);
                    return new Node(String.valueOf(result));
                }
            }
        }
        return node;
    }

    public void inOrderTraversal() {
        inOrderTraversal(root);
    }

    private void inOrderTraversal(Node node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.print(node.data + " ");
            inOrderTraversal(node.right);
        }
    }

    public void preOrderTraversal() {
        preOrderTraversal(root);
    }

    private void preOrderTraversal(Node node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preOrderTraversal(node.left);
            preOrderTraversal(node.right);
        }
    }

    public void postOrderTraversal() {
        postOrderTraversal(root);
    }

    private void postOrderTraversal(Node node) {
        if (node != null) {
            postOrderTraversal(node.left);
            postOrderTraversal(node.right);
            System.out.print(node.data + " ");
        }
    }

    private double evaluate(Node node, double x, double y) {
        if (node == null) {
            return 0;
        }

        if (isOperator(node.data)) {
            if (binaryOperators.containsKey(node.data)) {
                double leftValue = evaluate(node.left, x, y);
                double rightValue = evaluate(node.right, x, y);
                return binaryOperators.get(node.data).applyAsDouble(leftValue, rightValue);
            } else if (unaryOperators.containsKey(node.data)) {
                double operandValue = evaluate(node.left, x, y);
                return unaryOperators.get(node.data).apply(operandValue);
            }
        } else if ("x".equals(node.data)) {
            return x;
        } else if ("y".equals(node.data)) {
            return y;
        } else {
            return parseOperand(node.data);
        }
        return 0;
    }

    public double evaluate(double x, double y) {
        return evaluate(root, x, y);
    }

    private boolean isOperator(String token) {
        return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token)
                || "^".equals(token) || "sin".equals(token) || "cos".equals(token);
    }

    private boolean isNumeric(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double parseOperand(String token) {
        if (isNumeric(token)) {
            return Double.parseDouble(token);
        } else {
            return Double.NaN; // Handle variables or other cases
        }
    }

    private double performOperation(String operator, double left, double right) {
        switch (operator) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "*":
                return left * right;
            case "/":
                return left / right;
            default:
                return Double.NaN; // Handle unsupported operators
        }
    }

    public static void main(String[] args) {
        ExpressionTree tree = new ExpressionTree("x 2 ^ sin x y * +");
        System.out.println("In-order traversal:");
        tree.inOrderTraversal();
        System.out.println();

        System.out.println("Pre-order traversal:");
        tree.preOrderTraversal();
        System.out.println();

        System.out.println("Post-order traversal:");
        tree.postOrderTraversal();
        System.out.println();

        double x = 1.0;
        double y = 2.0;
        double result = tree.evaluate(x, y);
        System.out.println("Evaluation at x=" + x + ", y=" + y + ": " + result);
    }
}
