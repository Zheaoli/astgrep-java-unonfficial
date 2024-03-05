package astgrep;

import io.github.zheaoli.astgrep.Root;

public class TestMain {
  public static void main(String[] args) {
    String s = "print(\"hello\")";
    Root root = Root.of(s, "python");
    System.out.println(root.print());
  }
}
