package io.github.zheaoli.astgrep;

import io.questdb.jar.jni.JarJniLoader;

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

    @Override
    protected native void disposeInternal(long handle);

    protected static native boolean isLeaf(long handle);

    protected static native boolean isNamed(long handle);

    protected static native boolean isNamedLeaf(long handle);


    protected static native String kind(long handle);

    protected static native String text(long handle);


}
