package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.spotlight.platform.userprofile.api.core.exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;

public class UserProfilePropertyValue {

    private final Object value;

    @JsonCreator
    private UserProfilePropertyValue(Object value) {
        this.value = value;
    }

    public static UserProfilePropertyValue valueOf(Object value) {
        return new UserProfilePropertyValue(value);
    }

    @JsonValue
    protected Object getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        return value.equals(((UserProfilePropertyValue) obj).getValue());
    }

    @Override
    public String toString() {
        return "UserProfilePropertyValue{" +
                "value=" + value +
                '}';
    }

    public UserProfilePropertyValue increment(UserProfilePropertyValue value) {
        try {
            return valueOf(((Integer) this.value) + ((Integer) value.getValue())); // for simplicity, we just cast to integer here
        } catch (ClassCastException e) {
            throw new BadRequestException("Incompatible types for increment: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public UserProfilePropertyValue collect(UserProfilePropertyValue value) {
        try {
            List<Object> newList = new ArrayList<Object>((List)this.value);
            newList.addAll((List) value.getValue());
            return valueOf(newList);
        } catch (ClassCastException e) {
            throw new BadRequestException("Incompatible types for collect: " + e.getMessage());
        }
    }
}

