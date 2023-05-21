package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.core.profile.update.UserProfileHandler;
import com.spotlight.platform.userprofile.api.dto.CommandType;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;

import javax.inject.Inject;
import java.util.Map;

public class UserProfileService {
    private final UserProfileDao userProfileDao;
    private final Map<CommandType, UserProfileHandler> updateableMap;

    @Inject
    public UserProfileService(UserProfileDao userProfileDao, Map<CommandType, UserProfileHandler> updateableMap) {
        this.userProfileDao = userProfileDao;
        this.updateableMap = updateableMap;
    }

    public UserProfile get(UserId userId) {
        return userProfileDao.get(userId).orElseThrow(EntityNotFoundException::new);
    }

    public UserProfile update(UserId userId, UserProfileDTO userProfileDTO) {
        // todo handle different userIds in path and body
        System.out.println("Got following type: " + userProfileDTO.type());
        setUpdaterInstance(userProfileDTO.type());
        UserProfileHandler updater = setUpdaterInstance(userProfileDTO.type());
        updater.process(userProfileDTO);

        return userProfileDao.get(userId).get();
    }

    private UserProfileHandler setUpdaterInstance(CommandType commandType) {
        return updateableMap.get(commandType);
    }
}
