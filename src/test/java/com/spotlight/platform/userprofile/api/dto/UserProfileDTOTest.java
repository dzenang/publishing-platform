package com.spotlight.platform.userprofile.api.dto;

import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;
import org.junit.jupiter.api.Test;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

class UserProfileDTOTest {

    @Test
    void serialization_WorksAsExpected() {
        assertThatJson(UserProfileFixtures.USER_PROFILE_DTO_REPLACE).isEqualTo(UserProfileFixtures.SERIALIZED_USER_PROFILE_DTO_REPLACE);
        assertThatJson(UserProfileFixtures.USER_PROFILE_DTO_INCREMENT).isEqualTo(UserProfileFixtures.SERIALIZED_USER_PROFILE_DTO_INCREMENT);
        assertThatJson(UserProfileFixtures.USER_PROFILE_DTO_COLLECT).isEqualTo(UserProfileFixtures.SERIALIZED_USER_PROFILE_DTO_COLLECT);
    }
}