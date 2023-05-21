package com.spotlight.platform.userprofile.api.core.profile.update;

import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;

public class ReplacePropertyValue implements Updateable {

    private final UserProfileDao userProfileDao;

    @Inject
    public ReplacePropertyValue(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    @Override
    public void process(UserProfileDTO userProfileDTO) {
        userProfileDao.get(userProfileDTO.userId()).ifPresentOrElse(
                updateUserProfile(userProfileDTO),
                createUserProfile(userProfileDTO)
        );
    }

    private Runnable createUserProfile(UserProfileDTO userProfileDTO) {
        return () -> userProfileDao.put(new UserProfile(userProfileDTO.userId(), Instant.now(), userProfileDTO.properties()));
    }

    private Consumer<UserProfile> updateUserProfile(UserProfileDTO userProfileDTO) {
        return userProfile -> {
            var existingProperties = userProfile.userProfileProperties();
            existingProperties.putAll(userProfileDTO.properties());
            userProfileDao.put(new UserProfile(userProfileDTO.userId(), Instant.now(), existingProperties));
        };
    }
}
