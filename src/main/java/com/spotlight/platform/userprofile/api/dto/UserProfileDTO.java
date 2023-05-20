package com.spotlight.platform.userprofile.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyName;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfilePropertyValue;

import java.time.Instant;
import java.util.Map;

public record UserProfileDTO(@JsonProperty UserId userId, @JsonProperty CommandType type,
                             @JsonProperty Map<UserProfilePropertyName, UserProfilePropertyValue> properties) {
}
