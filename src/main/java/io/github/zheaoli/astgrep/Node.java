package io.github.zheaoli.astgrep;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.zheaoli.astgrep.arguments.Core;
import io.github.zheaoli.astgrep.arguments.Rule;
import io.questdb.jar.jni.JarJniLoader;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Node extends NativeObject {
    static {
        JarJniLoader.loadLib(Node.class, "rust/libs", "ast_grep_java_unofficial");
    }

    public Node(long nativeHandle) {
        super(nativeHandle);
    }

    public boolean isLeaf() {
        return isLeaf(nativeHandle);
    }

    public boolean isNamed() {
        return isNamed(nativeHandle);
    }

    public boolean isNamedLeaf() {
        return isNamedLeaf(nativeHandle);
    }

    public String kind() {
        return kind(nativeHandle);
    }

    public String text() {
        return text(nativeHandle);
    }

    public boolean matches(Rule rule) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ruleJson = objectMapper.writeValueAsString(rule);
        return matches(nativeHandle, ruleJson);
    }

    public boolean inside(Rule rule) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ruleJson = objectMapper.writeValueAsString(rule);
        return inside(nativeHandle, ruleJson);
    }

    public boolean has(Rule rule) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ruleJson = objectMapper.writeValueAsString(rule);
        return has(nativeHandle, ruleJson);
    }

    public boolean precedes(Rule rule) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ruleJson = objectMapper.writeValueAsString(rule);
        return precedes(nativeHandle, ruleJson);
    }

    public boolean follows(Rule rule) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ruleJson = objectMapper.writeValueAsString(rule);
        return follows(nativeHandle, ruleJson);
    }

    public Node find(Core core) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String coreJson = objectMapper.writeValueAsString(core);
        return find(nativeHandle, coreJson);
    }

    public Node getMatch(String meta) {
        return getMatch(nativeHandle, meta);
    }

    public List<Node> getMultipleMatches(String meta){
        return Arrays.asList(getMultipleMatches(nativeHandle, meta));
    }

    public String getTransformed(String meta)  {
        return getTransformed(nativeHandle, meta);
    }


    @Override
    protected native void disposeInternal(long handle);

    protected static native boolean isLeaf(long handle);

    protected static native boolean isNamed(long handle);

    protected static native boolean isNamedLeaf(long handle);


    protected static native String kind(long handle);

    protected static native String text(long handle);

    protected static native boolean matches(long handle, String rule);

    protected static native boolean inside(long handle, String rule);

    protected static native boolean has(long handle, String rule);

    protected static native boolean precedes(long handle, String rule);

    protected static native boolean follows(long handle, String rule);

    protected static native Node find(long handle, String core);

    protected static native Node getMatch(long handle, String meta);

    protected static native Node[] getMultipleMatches(long handle, String meta);

    protected static native String getTransformed(long handle, String meta);
}
