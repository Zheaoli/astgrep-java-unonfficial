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


  private Root(long nativeHandle) {
    super(nativeHandle);
  }

  public String print() {
    return print(nativeHandle);
  }

  @Override
  protected native void disposeInternal(long handle);

  protected static native long constructor(String sourceCode, String language);

  protected static native String print(long handle);


}
