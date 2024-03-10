package io.github.zheaoli.astgrep.arguments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonSetter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rule {
    @JsonSetter(nulls = Nulls.SKIP)

    private String pattern;

    @JsonSetter(nulls = Nulls.SKIP)
    private String kind;

    @JsonSetter(nulls = Nulls.SKIP)
    private String regex;

    @JsonSetter(nulls = Nulls.SKIP)
    private Relation inside;
    @JsonSetter(nulls = Nulls.SKIP)
    private Relation has;
    @JsonSetter(nulls = Nulls.SKIP)
    private Relation precedes;
    @JsonSetter(nulls = Nulls.SKIP)
    private Relation follows;
    @JsonSetter(nulls = Nulls.SKIP)
    private ArrayList<Rule> all;
    @JsonSetter(nulls = Nulls.SKIP)
    private Rule not;
    @JsonSetter(nulls = Nulls.SKIP)
    private String matches;

}
