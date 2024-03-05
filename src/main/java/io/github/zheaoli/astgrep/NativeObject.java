package io.github.zheaoli.astgrep;

public abstract class NativeObject implements AutoCloseable {
    protected final long nativeHandle;

    protected NativeObject(long nativeHandle) {
        this.nativeHandle = nativeHandle;
    }

    @Override
    public void close() {
        disposeInternal(nativeHandle);
    }

    protected abstract void disposeInternal(long handle);
}
