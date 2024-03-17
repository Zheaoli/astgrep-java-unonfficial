package astgrep;

import io.github.zheaoli.astgrep.Node;
import io.github.zheaoli.astgrep.Root;
import io.github.zheaoli.astgrep.arguments.Core;
import io.github.zheaoli.astgrep.arguments.Rule;
import io.github.zheaoli.astgrep.position.Pos;
import io.github.zheaoli.astgrep.position.Range;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RangeTest {

    private Node root;

    @BeforeAll
    public void initTestInstance() throws Exception {
        String sourceCode = "function test() {\n" +
                "  let a = 123\n" +
                "}";
        root = Root.of(sourceCode, "javascript").root();
        assert root != null;
    }

    @Test
    public void testPos() throws Exception {
        String sourceCode = "function test() {\n" +
                "  let a = 123\n" +
                "}";
        Node root = Root.of(sourceCode, "javascript").root();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        Core core = new Core();
        core.setRule(rule);
        Node node1 = root.find(core);
        assert node1 != null;
        rule.setPattern("let $A = 123");
        Node node2 = root.find(core);
        assert node2 != null;
        Range range1 = node1.range();
        Range range2 = node2.range();
        Pos pos1= range1.getStart();
        Pos pos2 = range2.getStart();
        assert pos1.getLine() == 1;
        assert pos1.getColumn() == 2;
        assert pos1.getIndex() == 20;
        assert pos1.equals(pos2);
    }
    @Test
    public void testRange() throws Exception {
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        Core core = new Core();
        core.setRule(rule);
        Node node1 = root.find(core);
        assert node1 != null;
        rule.setPattern("let $A = 123");
        Node node2 = root.find(core);
        assert node2 != null;
        Range range1 = node1.range();
        Range range2 = node2.range();
        assert range1.equals(range2);
        assert range1.getStart().getLine()==1;
        assert range1.getEnd().getLine()==1;
        assert range1.getStart().getColumn()==2;
        assert range1.getEnd().getColumn()==13;
        assert range1.getStart().getIndex()==20;
        assert range1.getEnd().getIndex()==31;
    }
}
