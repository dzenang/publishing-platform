package com.spotlight.platform.userprofile.api.core.profile.update;

import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

public interface Updateable {
    void updateOrCreate(UserProfileDTO userProfileDTO);
}
