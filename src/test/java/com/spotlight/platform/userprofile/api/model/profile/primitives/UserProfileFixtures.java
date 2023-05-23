package com.spotlight.platform.userprofile.api.model.profile.primitives;

import com.spotlight.platform.helpers.FixtureHelpers;
import com.spotlight.platform.userprofile.api.dto.CommandType;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

import java.time.Instant;
import java.util.Arrays;
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
            Map.of(UserProfilePropertyName.valueOf("property1Dto"), UserProfilePropertyValue.valueOf("property1DtoValue")));

    public static final UserProfileDTO USER_PROFILE_DTO_REPLACE_UPDATED = new UserProfileDTO(USER_ID, CommandType.REPLACE,
            Map.of(UserProfilePropertyName.valueOf("property1Dto"), UserProfilePropertyValue.valueOf("property1DtoValueUpdated")));

    public static final String SERIALIZED_USER_PROFILE_DTO_REPLACE = FixtureHelpers.fixture("/fixtures/dto/userProfileDTOReplace.json");

    public static final UserProfileDTO USER_PROFILE_DTO_INCREMENT = new UserProfileDTO(USER_ID, CommandType.INCREMENT,
            Map.of(UserProfilePropertyName.valueOf("property1Dto"), UserProfilePropertyValue.valueOf(1)));

    public static final UserProfileDTO USER_PROFILE_DTO_INCREMENT_INCORRECT = new UserProfileDTO(USER_ID, CommandType.INCREMENT,
            Map.of(UserProfilePropertyName.valueOf("property1Dto"), UserProfilePropertyValue.valueOf("1")));

    public static final UserProfileDTO USER_PROFILE_DTO_INCREMENT_UPDATED = new UserProfileDTO(USER_ID, CommandType.INCREMENT,
            Map.of(UserProfilePropertyName.valueOf("property1Dto"), UserProfilePropertyValue.valueOf(2)));

    public static final String SERIALIZED_USER_PROFILE_DTO_INCREMENT = FixtureHelpers.fixture("/fixtures/dto/userProfileDTOIncrement.json");

    public static final UserProfileDTO USER_PROFILE_DTO_COLLECT = new UserProfileDTO(USER_ID, CommandType.COLLECT,
            Map.of(UserProfilePropertyName.valueOf("property1Dto"), UserProfilePropertyValue.valueOf(Arrays.asList(1, 2))));

    public static final UserProfileDTO USER_PROFILE_DTO_COLLECT_UPDATED = new UserProfileDTO(USER_ID, CommandType.COLLECT,
            Map.of(UserProfilePropertyName.valueOf("property1Dto"), UserProfilePropertyValue.valueOf(Arrays.asList(1, 2, 1, 2))));

    public static final String SERIALIZED_USER_PROFILE_DTO_COLLECT = FixtureHelpers.fixture("/fixtures/dto/userProfileDTOCollect.json");
}
