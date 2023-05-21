package com.spotlight.platform.userprofile.api.core.profile.update;

import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;

public interface UserProfileHandler {
    void process(UserProfileDTO userProfileDTO);
}
