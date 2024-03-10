package io.github.zheaoli.astgrep.arguments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Relation {
    @JsonSetter(nulls = Nulls.SKIP)
    private Rule rule;

    @JsonProperty("stop_by")
    @JsonSetter(nulls = Nulls.SKIP)
    private String stopBy;
    @JsonSetter(nulls = Nulls.SKIP)
    private String field;
}
