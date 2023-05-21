package com.spotlight.platform.userprofile.api.core.profile.update;

import com.spotlight.platform.userprofile.api.core.exceptions.BadRequestException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

import javax.inject.Inject;
import java.time.Instant;
import java.util.function.Consumer;

public class IncrementPropertyValue implements Updateable {

    private final UserProfileDao userProfileDao;

    @Inject
    public IncrementPropertyValue(UserProfileDao userProfileDao) {
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
        // would be good if we would create UserProfile instance inside UserProfileDao put method, so we take care of that in one place
    }

    private Consumer<UserProfile> updateUserProfile(UserProfileDTO userProfileDTO) {
        return userProfile -> {
            var existingProperties = userProfile.userProfileProperties();
            userProfileDTO.properties().forEach(
                    (k, v) -> existingProperties.compute(k, (k1, v1) -> v1 != null ? v1.increment(v) : v)
            );

            userProfileDao.put(new UserProfile(userProfile.userId(), Instant.now(), existingProperties));
        };
    }
}
