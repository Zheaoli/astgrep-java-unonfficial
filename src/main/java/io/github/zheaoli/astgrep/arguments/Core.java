package io.github.zheaoli.astgrep.arguments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Core {
    private Rule rule;

    private Map<String, Rule> constraints;

    private Map<String,Rule> utils;

    private Map<String, Map<String, Object>> transform;

    private Object fix;
}
