package com.spotlight.platform.userprofile.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CommandType {
    REPLACE("replace"),
    INCREMENT("increment"),
    COLLECT("collect");

    private final String type;

    @JsonCreator
    CommandType(final String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
