package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.core.profile.update.Updateable;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;

import javax.inject.Inject;
import java.time.Instant;

public class UserProfileService {
    private final UserProfileDao userProfileDao;
    private final Updateable updater;

    @Inject
    public UserProfileService(UserProfileDao userProfileDao, Updateable updater) {
        this.userProfileDao = userProfileDao;
        this.updater = updater;
    }

    public UserProfile get(UserId userId) {
        return userProfileDao.get(userId).orElseThrow(EntityNotFoundException::new);
    }

    public UserProfile update(UserId userId, UserProfileDTO userProfileDTO) {
        // todo handle different userIds in path and body
        updater.updateOrCreate(userProfileDTO);

        return userProfileDao.get(userId).get();
    }
}
