package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.spotlight.platform.helpers.FixtureHelpers;
import com.spotlight.platform.userprofile.api.dto.CommandType;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UserProfileFixtures {
    public static final UserId USER_ID = UserId.valueOf("existing-user-id");
    public static final UserId NON_EXISTING_USER_ID = UserId.valueOf("non-existing-user-id");
    public static final UserId INVALID_USER_ID = UserId.valueOf("invalid-user-id-%");

    public static final Instant LAST_UPDATE_TIMESTAMP = Instant.parse("2021-06-01T09:16:36.123Z");

    public static final UserProfile USER_PROFILE = new UserProfile(USER_ID, LAST_UPDATE_TIMESTAMP,
            Map.of(UserProfilePropertyName.valueOf("property1"), UserProfilePropertyValue.valueOf("property1Value")));

    public static final String SERIALIZED_USER_PROFILE = FixtureHelpers.fixture("/fixtures/model/profile/userProfile.json");

    public static final UserProfileDTO USER_PROFILE_DTO_REPLACE = new UserProfileDTO(USER_ID, CommandType.REPLACE,
            Map.of(UserProfilePropertyName.valueOf("property1dto"), UserProfilePropertyValue.valueOf("property1dtoValue")));

    public static final UserProfileDTO USER_PROFILE_DTO_INCREMENT = new UserProfileDTO(USER_ID, CommandType.INCREMENT,
            Map.of(UserProfilePropertyName.valueOf("property1dto"), UserProfilePropertyValue.valueOf(1)));

    public static final UserProfileDTO USER_PROFILE_DTO_COLLECT = new UserProfileDTO(USER_ID, CommandType.COLLECT,
            Map.of(UserProfilePropertyName.valueOf("property1dto"), UserProfilePropertyValue.valueOf(Arrays.asList(1, 2))));
}
