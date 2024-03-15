package io.github.zheaoli.astgrep.position;

import io.github.zheaoli.astgrep.NativeObject;
import lombok.Data;

@Data
public class Range {
    private Pos start;
    private Pos end;

    public static Range of(Pos start, Pos end) {
        Range range = new Range();
        range.start = start;
        range.end = end;
        return range;
    }
}
