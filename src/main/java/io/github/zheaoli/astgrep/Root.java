package io.github.zheaoli.astgrep;

import io.questdb.jar.jni.JarJniLoader;

public class Root extends NativeObject {
    static {
        JarJniLoader.loadLib(Root.class,
                "rust/libs",
                "ast_grep_java_unofficial");
    }

    public static Root of(String sourceCode, String language) {
        long nativeHandle = constructor(sourceCode, language);
        return new Root(nativeHandle);
    }

    public static Root of(long nativeHandle) {
        return new Root(nativeHandle);
    }


    private Root(long nativeHandle) {
        super(nativeHandle);
    }

    public String filename() {
        return filename(nativeHandle);
    }

    public Node root() {
        return root(nativeHandle);
    }

    @Override
    protected native void disposeInternal(long handle);

    protected static native long constructor(String sourceCode, String language);

    protected static native String filename(long handle);

    protected static native Node root(long handle);

}
