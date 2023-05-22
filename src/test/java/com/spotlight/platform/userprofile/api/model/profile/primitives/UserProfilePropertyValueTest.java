package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.spotlight.platform.userprofile.api.core.exceptions.BadRequestException;
import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserProfilePropertyValueTest {
    private static final String STRING_VALUE = "someString";
    private static final int INTEGER_VALUE = 5;
    private static final int INTEGER_INCREMENT = 1;
    private static final int INTEGER_DECREMENT = -1;
    private static final int INTEGER_VALUE_RESULT = 6;
    private static final int INTEGER_VALUE_RESULT2 = 4;
    private static final List<String> LIST_VALUE = List.of("one", "two");
    private static final List<Integer> LIST_VALUE2 = List.of(1, 2);
    private static final List<Object> LIST_RESULT = List.of("one", "two", 1, 2);
    private static final List<Object> LIST_RESULT2 = List.of(1, 2, "one", "two");

    @Test
    void equals_ReturnsTrueForEqualValues() {
        assertThat(UserProfilePropertyValue.valueOf(STRING_VALUE)).isEqualTo(UserProfilePropertyValue.valueOf(STRING_VALUE));
        assertThat(UserProfilePropertyValue.valueOf(INTEGER_VALUE)).isEqualTo(UserProfilePropertyValue.valueOf(INTEGER_VALUE));
        assertThat(UserProfilePropertyValue.valueOf(LIST_VALUE)).isEqualTo(UserProfilePropertyValue.valueOf(LIST_VALUE));
    }

    @Test
    void serialization_WorksCorrectly() {
        assertThatJson(UserProfilePropertyValue.valueOf(STRING_VALUE)).isEqualTo("someString");
        assertThatJson(UserProfilePropertyValue.valueOf(INTEGER_VALUE)).isEqualTo("5");
        assertThatJson(UserProfilePropertyValue.valueOf(LIST_VALUE)).isEqualTo("[\"one\",\"two\"]");
    }

    @Test
    void increment_WorksCorrectly() {
        assertThat(UserProfilePropertyValue.valueOf(INTEGER_VALUE).increment(UserProfilePropertyValue.valueOf(INTEGER_INCREMENT)))
                .isEqualTo(UserProfilePropertyValue.valueOf(INTEGER_VALUE_RESULT));
        assertThat(UserProfilePropertyValue.valueOf(INTEGER_VALUE).increment(UserProfilePropertyValue.valueOf(INTEGER_DECREMENT)))
                .isEqualTo(UserProfilePropertyValue.valueOf(INTEGER_VALUE_RESULT2));

        assertThatThrownBy(() -> UserProfilePropertyValue.valueOf(INTEGER_VALUE).increment(UserProfilePropertyValue.valueOf(STRING_VALUE)))
                .isExactlyInstanceOf(BadRequestException.class).hasMessageContaining("Incompatible types for increment: ");
        assertThatThrownBy(() -> UserProfilePropertyValue.valueOf(STRING_VALUE).increment(UserProfilePropertyValue.valueOf(INTEGER_VALUE)))
                .isExactlyInstanceOf(BadRequestException.class).hasMessageContaining("Incompatible types for increment: ");
    }

    @Test
    void collect_WorksCorrectly() {
        assertThat(UserProfilePropertyValue.valueOf(LIST_VALUE).collect(UserProfilePropertyValue.valueOf(LIST_VALUE2)))
                .isEqualTo(UserProfilePropertyValue.valueOf(LIST_RESULT));
        assertThat(UserProfilePropertyValue.valueOf(LIST_VALUE2).collect(UserProfilePropertyValue.valueOf(LIST_VALUE)))
                .isEqualTo(UserProfilePropertyValue.valueOf(LIST_RESULT2));

        assertThatThrownBy(() -> UserProfilePropertyValue.valueOf(INTEGER_VALUE).collect(UserProfilePropertyValue.valueOf(STRING_VALUE)))
                .isExactlyInstanceOf(BadRequestException.class).hasMessageContaining("Incompatible types for collect: ");
        assertThatThrownBy(() -> UserProfilePropertyValue.valueOf(LIST_VALUE).collect(UserProfilePropertyValue.valueOf(INTEGER_VALUE)))
                .isExactlyInstanceOf(BadRequestException.class).hasMessageContaining("Incompatible types for collect: ");
        assertThatThrownBy(() -> UserProfilePropertyValue.valueOf(STRING_VALUE).collect(UserProfilePropertyValue.valueOf(LIST_VALUE)))
                .isExactlyInstanceOf(BadRequestException.class).hasMessageContaining("Incompatible types for collect: ");
    }
}