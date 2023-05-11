package com.spotlight.platform.userprofile.api.core.profile.update;

import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Optional;

public class ReplacePropertyValue implements Updateable {

    private final UserProfileDao userProfileDao;

    @Inject
    public ReplacePropertyValue(UserProfileDao userProfileDao) {
        this.userProfileDao = userProfileDao;
    }

    @Override
    public void updateOrCreate(UserProfileDTO userProfileDTO) {
        var sentProperties = userProfileDTO.properties();
        var userProfileOpt = userProfileDao.get(userProfileDTO.userId());
        if (userProfileOpt.isEmpty()) {
            userProfileOpt = Optional.of(new UserProfile(userProfileDTO.userId(), Instant.now(), userProfileDTO.properties()));
        } else {
            var existingProperties = userProfileOpt.get().userProfileProperties();
            existingProperties.putAll(sentProperties);
            userProfileOpt = Optional.of(new UserProfile(userProfileDTO.userId(), Instant.now(), existingProperties));
        }
        userProfileDao.put(userProfileOpt.get());
    }
}
