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
    private final Map<CommandType, Updateable> updateableMap;

    @Inject
    public UserProfileService(UserProfileDao userProfileDao, Map<CommandType, Updateable> updateableMap) {
        this.userProfileDao = userProfileDao;
        this.updateableMap = updateableMap;
    }

    public UserProfile get(UserId userId) {
        return userProfileDao.get(userId).orElseThrow(EntityNotFoundException::new);
    }

    public UserProfile update(UserId userId, UserProfileDTO userProfileDTO) {
        // todo handle different userIds in path and body
        setUpdaterInstance(userProfileDTO.type());
        Updateable updater = setUpdaterInstance(userProfileDTO.type());
        updater.updateOrCreate(userProfileDTO);

        return userProfileDao.get(userId).get();
    }

    private Updateable setUpdaterInstance(CommandType commandType) {
        return updateableMap.get(commandType);
    }
}
