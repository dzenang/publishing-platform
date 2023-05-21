package com.spotlight.platform.userprofile.api.core.profile.update;


import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

import javax.inject.Inject;
import java.time.Instant;

public abstract class AbstractUserProfileHandler implements UserProfileHandler{

    protected final UserProfileDao userProfileDao;

    @Inject
    protected AbstractUserProfileHandler(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    protected Runnable createUserProfile(UserProfileDTO userProfileDTO) {
        return () -> userProfileDao.put(new UserProfile(userProfileDTO.userId(), Instant.now(), userProfileDTO.properties()));
    }
}
