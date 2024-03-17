package astgrep;

import io.github.zheaoli.astgrep.Node;
import io.github.zheaoli.astgrep.Root;
import io.github.zheaoli.astgrep.arguments.Core;
import io.github.zheaoli.astgrep.arguments.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TraversalTest {

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
    public void testGetRoot() throws Exception {

        Root newRoot = root.getRoot();
        assert newRoot != null;
        assert newRoot.filename().equals("anonymous");
    }

    @Test
    public void testFindAll() throws Exception {
        Rule rule = new Rule();
        rule.setPattern("let $N = $V");
        Core core = new Core();
        core.setRule(rule);
        List<Node> nodes = root.findAll(core);
        ArrayList<String> texts = new ArrayList<String>() {
            {
                add("a");
                add("b");
                add("c");
            }
        };
        assert nodes.size() == 3;
        for (int i = 0; i < nodes.size(); i++) {
            Node tempNode = nodes.get(i).getMatch("N");
            assert tempNode != null;
            assert tempNode.text().equals(texts.get(i));
        }
    }

    @Test
    public void testField() throws Exception {
        Rule rule = new Rule();
        rule.setKind("variable_declarator");
        Core core = new Core();
        core.setRule(rule);
        Node node = root.find(core);
        assert node != null;
        assert node.field("name").text().equals("a");
        assert node.field("value").text().equals("123");
        Node tempNode = node.field("notexist");
        assert tempNode == null;
    }

    @Test
    public void testParent() throws Exception {
        Rule rule = new Rule();
        rule.setKind("variable_declarator");
        Core core = new Core();
        core.setRule(rule);
        Node node = root.find(core);
        assert node != null;
        Node parentNode = node.parent();
        assert parentNode != null;
        assert parentNode.kind().equals("lexical_declaration");
        assert root.parent() == null;
    }

    @Test
    public void testChild() throws Exception {
        Rule rule = new Rule();
        rule.setKind("variable_declarator");
        Core core = new Core();
        core.setRule(rule);
        Node node = root.find(core);
        assert node != null;
        assert node.child(0).text().equals("a");
        assert node.child(2).text().equals("123");
        assert node.child(3) == null;
    }

    @Test
    public void testChildren() throws Exception {
        Rule rule = new Rule();
        rule.setKind("variable_declarator");
        Core core = new Core();
        core.setRule(rule);
        Node node = root.find(core);
        assert node != null;
        List<Node> children = node.children();
        assert children.size() == 3;
        assert children.get(0).text().equals("a");
        assert children.get(2).text().equals("123");
        assert children.get(0).children().isEmpty();
    }

    @Test
    public void testAncestors() throws Exception {
        Rule rule = new Rule();
        rule.setKind("variable_declarator");
        Core core = new Core();
        core.setRule(rule);
        Node node = root.find(core);
        assert node != null;
        List<Node> ancestors = node.ancestors();
        assert ancestors.size() == 4;
        assert root.ancestors().isEmpty();
    }

    @Test
    public void testNext() throws Exception {
        Rule rule = new Rule();
        rule.setPattern("let a = $A\n");
        Core core = new Core();
        core.setRule(rule);
        Node node = root.find(core);
        assert node != null;
        Node neighbor = node.next();
        assert neighbor != null;
        assert neighbor.text().equals("let b = 456");
        rule.setPattern("let c = $A\n");
        Node node2 = root.find(core);
        assert node2.next().next() == null;
    }

    @Test
    public void testNextAll() throws Exception {
        Rule rule = new Rule();
        rule.setPattern("let a = $A\n");
        Core core = new Core();
        core.setRule(rule);
        Node node = root.find(core);
        assert node != null;
        List<Node> nextAll = node.nextAll();
        assert nextAll.size() == 3;
        assert nextAll.get(0).nextAll().size() == 2;
        assert nextAll.get(2).nextAll().isEmpty();
    }

    @Test
    public void testPrev() throws Exception {
        Rule rule = new Rule();
        rule.setPattern("let c = $A\n");
        Core core = new Core();
        core.setRule(rule);
        Node node = root.find(core);
        assert node != null;
        Node neighbor = node.prev();
        assert neighbor != null;
        assert neighbor.text().equals("let b = 456");
        rule.setPattern("let a = $A\n");
        Node node2 = root.find(core);
        assert node2.prev().prev() == null;
    }

    @Test
    public void testPrevAll() throws Exception {
        Rule rule = new Rule();
        rule.setPattern("let c = $A\n");
        Core core = new Core();
        core.setRule(rule);
        Node node = root.find(core);
        assert node != null;
        List<Node> prevAll = node.prevAll();
        assert prevAll.size() == 3;
        assert prevAll.get(0).prevAll().size() == 2;
        assert prevAll.get(2).prevAll().isEmpty();
    }
}
