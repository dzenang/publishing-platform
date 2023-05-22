package com.spotlight.platform.userprofile.api.core.profile.update;

import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

import javax.inject.Inject;
import java.time.Instant;
import java.util.HashMap;
import java.util.function.Consumer;

public class CollectHandler extends AbstractUserProfileHandler {

    @Inject
    protected CollectHandler(UserProfileDao userProfileDao) {
        super(userProfileDao);
    }

    @Override
    public void process(UserProfileDTO userProfileDTO) {
        userProfileDao.get(userProfileDTO.userId()).ifPresentOrElse(
                updateUserProfile(userProfileDTO),
                createUserProfile(userProfileDTO)
        );
    }

    private Consumer<UserProfile> updateUserProfile(UserProfileDTO userProfileDTO) {
        return userProfile -> {
            var existingProperties = new HashMap<>(userProfile.userProfileProperties());
            userProfileDTO.properties().forEach(
                    (k, v) -> existingProperties.compute(k, (k1, v1) -> v1 != null ? v1.collect(v) : v)
            );

            userProfileDao.put(new UserProfile(userProfile.userId(), Instant.now(), existingProperties));
            // would be good if we would create UserProfile instance inside UserProfileDao put method, so we take care of that in one place
        };
    }
}
