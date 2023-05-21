package com.spotlight.platform.userprofile.api.core.profile.update;

import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;

public interface Updateable {
    void process(UserProfileDTO userProfileDTO);
}
