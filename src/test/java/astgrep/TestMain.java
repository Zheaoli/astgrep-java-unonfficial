package astgrep;

import io.github.zheaoli.astgrep.Node;
import io.github.zheaoli.astgrep.Root;

public class TestMain {
    public static void main(String[] args) {
        String s = "print(\"hello\")";
        Root root = Root.of(s, "python");
        System.out.println(root.print());
        Node node = root.root();
        System.out.println("isLeaf: " + node.isLeaf());
        System.out.println("isNamed: " + node.isNamed());
        System.out.println("isNamedLeaf: " + node.isNamedLeaf());
        System.out.println("kind: " + node.kind());
        System.out.println("text: " + node.text());
    }
}
