package io.github.zheaoli.astgrep.arguments;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Core {
    private Rule rule;

    private HashMap<String, Rule> constraints;

    private HashMap<String,Rule> utils;

    private HashMap<String, HashMap<String, Object>> transform;

    private Object fix;
}
