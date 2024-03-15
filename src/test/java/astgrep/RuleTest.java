package astgrep;

import io.github.zheaoli.astgrep.Node;
import io.github.zheaoli.astgrep.Root;
import io.github.zheaoli.astgrep.arguments.Core;
import io.github.zheaoli.astgrep.arguments.Relation;
import io.github.zheaoli.astgrep.arguments.Rule;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class RuleTest {
    @Test
    public void testSimple() throws Exception {
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
    }

    @Test
    public void testNotRule() throws Exception {
        String sourceCode = "function test() {\n" +
                "  let a = 123\n" +
                "}";
        Node root = Root.of(sourceCode, "javascript").root();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        Rule notRule = new Rule();
        notRule.setPattern("let a = 123");
        rule.setNot(notRule);
        Core core = new Core();
        core.setRule(rule);
        Node node1 = root.find(core);
        assert node1 == null;
        notRule.setPattern("let b = 123");
        Node node2 = root.find(core);
        assert node2 != null;
    }

    @Test
    public void testRelationRule() throws Exception {
        String sourceCode = "function test() {\n" +
                "  let a = 123\n" +
                "}";
        Node root = Root.of(sourceCode, "javascript").root();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        Rule relationRule = new Rule();
        relationRule.setPattern("let a = 123\n");
        Relation relation = new Relation();
        relation.setKind("function_declaration");
        relation.setStopBy("end");
        rule.setInside(relation);
        Core core = new Core();
        core.setRule(rule);
        Node node1 = root.find(core);
        assert node1 != null;
    }

    @Test
    public void testComplexConfig() throws Exception {
        String sourceCode = "function test() {\n" +
                "  let a = 123\n" +
                "}";
        Node root = Root.of(sourceCode, "javascript").root();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        rule.setRegex("123");
        Rule notRule = new Rule();
        notRule.setRegex("456");
        rule.setNot(notRule);
        Rule constraintRule = new Rule();
        constraintRule.setPattern("a");
        HashMap<String, Rule> constraint = new HashMap<String, Rule>() {
            {
                put("A", constraintRule);
            }
        };
        Core core = getComplexConfig(rule, constraint);
        Node node1 = root.find(core);
        assert node1 != null;
        assert node1.getTransformed("C").equals("2");
    }

    @Test
    public void testComplexConfigNotFound() throws Exception {
        String sourceCode = "function test() {\n" +
                "  let a = 123\n" +
                "}";
        Node root = Root.of(sourceCode, "javascript").root();
        Rule rule = new Rule();
        rule.setPattern("let $A = $B");
        rule.setRegex("123");
        Rule notRule = new Rule();
        notRule.setRegex("456");
        rule.setNot(notRule);

        HashMap<String, Rule> constraint = new HashMap<String, Rule>() {
            {
                Rule constraintRule = new Rule();
                constraintRule.setPattern("a");
                put("A", constraintRule);
                Rule constraintRule2 = new Rule();
                constraintRule2.setRegex("222");
                put("B",constraintRule2);
            }
        };
        Core core = getComplexConfig(rule, constraint);
        Node node1 = root.find(core);
        assert node1 == null;
    }

    private static Core getComplexConfig(Rule rule, HashMap<String, Rule> constraint) {
        HashMap<String, HashMap<String,Object>> transform = new HashMap<String, HashMap<String,Object>>() {
            {
                put("C", new HashMap<String,Object>() {
                    {
                        HashMap<String,Object> temp= new HashMap<String,Object>() {
                            {
                                put("source", "$B");
                                put("startChar", 1);
                                put("endChar", -1);
                            }
                        };
                        put("substring", temp);
                    }
                });
            }
        };
        Core core = new Core();
        core.setRule(rule);
        core.setConstraints(constraint);
        core.setTransform(transform);
        return core;
    }
}
