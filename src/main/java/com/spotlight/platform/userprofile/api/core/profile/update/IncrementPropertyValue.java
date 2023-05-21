package com.spotlight.platform.userprofile.api.core.profile.update;

import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import org.apache.commons.lang3.NotImplementedException;

public class IncrementPropertyValue implements Updateable {
    @Override
    public void updateOrCreate(UserProfileDTO userProfileDTO) {
        throw new NotImplementedException("This is not yet implemented");
    }
}
