package astgrep;

import io.github.zheaoli.astgrep.Node;
import io.github.zheaoli.astgrep.Root;
import io.github.zheaoli.astgrep.arguments.Core;
import io.github.zheaoli.astgrep.arguments.Rule;

import io.github.zheaoli.astgrep.position.Range;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Objects;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleTest {

    private Node root;

    @BeforeAll
    public void initTestInstance() throws Exception {
        String sourceCode = "function test() {\n" +
                "  let a = 123\n" +
                "  let b = 456\n" +
                "  let c = 789\n" +
                "}";
        root = Root.of(sourceCode, "javascript").root();
        assert root != null;
    }


    @Test
    public void testIsLeaf() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        assert !newNode.isLeaf();
        Rule rule1 = new Rule();
        rule1.setPattern("123");
        findArgument.setRule(rule1);
        Node newNode1 = root.find(findArgument);
        assert newNode1 != null;
        assert newNode1.isLeaf();
    }

    @Test
    public void testMatches() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        Rule rule1 = new Rule();
        Rule rule2 = new Rule();
        Rule rule3 = new Rule();
        Rule rule4 = new Rule();
        rule1.setKind("lexical_declaration");
        rule2.setKind("number");
        rule3.setPattern("let a = 123");
        rule4.setPattern("let b = 456");
        findArgument.setRule(rule1);
        assert newNode.matches(rule1);
        assert !newNode.matches(rule2);
        assert newNode.matches(rule3);
        assert !newNode.matches(rule4);
    }

    @Test
    public void testIsNamed() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        assert newNode.isNamed();
        Rule rule1 = new Rule();
        rule1.setPattern("123");
        findArgument.setRule(rule1);
        Node newNode1 = root.find(findArgument);
        assert newNode1 != null;
        assert newNode1.isNamed();
    }

    @Test
    public void testKind() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        assert Objects.equals(newNode.kind(), "lexical_declaration");
        Rule rule1 = new Rule();
        rule1.setPattern("123");
        findArgument.setRule(rule1);
        Node newNode1 = root.find(findArgument);
        assert newNode1 != null;
        assert newNode1.kind().equals("number");
    }

    @Test
    public void testText() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        assert Objects.equals(newNode.text(), "let a = 123");
        Rule rule1 = new Rule();
        rule1.setPattern("123");
        findArgument.setRule(rule1);
        Node newNode1 = root.find(findArgument);
        assert newNode1 != null;
        assert newNode1.text().equals("123");
    }

    @Test
    public void testInside() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        Rule rule1 = new Rule();
        rule1.setKind("function_declaration");
        Rule rule2 = new Rule();
        rule2.setKind("function_expression");
        assert newNode.inside(rule1);
        assert !newNode.inside(rule2);
    }

    @Test
    public void testHas() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        Rule rule1 = new Rule();
        rule1.setPattern("123");
        Rule rule2 = new Rule();
        rule2.setKind("number");
        Rule rule3 = new Rule();
        rule3.setKind("function_expression");
        assert newNode.has(rule1);
        assert newNode.has(rule2);
        assert !newNode.has(rule3);
    }

    @Test
    public void testPrecedes() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B\n");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        Rule rule1 = new Rule();
        rule1.setPattern("let b = 456\n");
        Rule rule2 = new Rule();
        rule2.setPattern("let b = 456\n");
        Rule rule3 = new Rule();
        rule3.setPattern("notExist");
        assert newNode.precedes(rule1);
        assert newNode.precedes(rule2);
        assert !newNode.precedes(rule3);
    }

    @Test
    public void testFollows() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("let b = 456\n");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        Rule rule1 = new Rule();
        rule1.setPattern("let a = 123\n");
        Rule rule2 = new Rule();
        rule2.setPattern("let c = 789\n");
        assert newNode.follows(rule1);
        assert !newNode.follows(rule2);
    }

    @Test
    public void testGetMatch() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B\n");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        Node matchNode = newNode.getMatch("A");
        assert matchNode != null;
        assert matchNode.text().equals("a");
        Range range=matchNode.range();
        assert range.getStart().getLine()==1;
        assert range.getStart().getColumn()==6;
    }

    @Test
    public void testGetMultiMatch() throws Exception {
        Core findArgument = new Core();
        Rule rule = new Rule();
        rule.setPattern("function test() { $$$STMT }");
        findArgument.setRule(rule);
        Node newNode = root.find(findArgument);
        assert newNode != null;
        List<Node> matchNode = newNode.getMultipleMatches("STMT");
        rule.setPattern("let a = 123");
        findArgument.setRule(rule);
        Node newNode1 = root.find(findArgument);
        assert newNode1 != null;
        assert matchNode != null;
        assert matchNode.size() == 3;
        assert matchNode.get(0).equals(newNode1);
    }
}
