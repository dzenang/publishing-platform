package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.core.profile.update.Updateable;
import com.spotlight.platform.userprofile.api.dto.CommandType;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;

import javax.inject.Inject;
import java.util.Map;

public class UserProfileService {
    private final UserProfileDao userProfileDao;
    private Updateable updater;

    @Inject
    Map<CommandType, Updateable> updateableMap;

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
        setUpdaterInstance(userProfileDTO.type());
        updater.updateOrCreate(userProfileDTO);

        return userProfileDao.get(userId).get();
    }

    private void setUpdaterInstance(CommandType commandType) {
        updater = updateableMap.get(commandType);
    }
}
