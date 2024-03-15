package io.github.zheaoli.astgrep.position;

import lombok.Data;

@Data
public class Pos {
    private long line;
    private long column;
    private long index;

    public static Pos of(long line, long column, long index) {
        Pos pos = new Pos();
        pos.line = line;
        pos.column = column;
        pos.index = index;
        return pos;
    }
}
