package com.spotlight.platform.userprofile.api.core.profile;

import com.spotlight.platform.userprofile.api.core.exceptions.EntityNotFoundException;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.core.profile.update.UserProfileHandler;
import com.spotlight.platform.userprofile.api.dto.CommandType;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserProfileService {
    private final UserProfileDao userProfileDao;
    private final Map<CommandType, UserProfileHandler> updateableMap;
    private static final Logger logger = Logger.getLogger(UserProfileService.class.getName());

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
        UserProfileHandler updater = getUpdaterInstance(userProfileDTO.type());
        updater.process(userProfileDTO);

        return userProfileDao.get(userId).get();
    }

    public UserProfile batchUpdate(UserId userId, List<UserProfileDTO> userProfileDTOList) {
        logger.log(Level.INFO, "Starting processing {0} commands!", userProfileDTOList.size());
        AtomicInteger processingCounter = new AtomicInteger();
        userProfileDTOList.forEach(
                processSingleCommand(userId, processingCounter)
        );
        return userProfileDao.get(userId).get();
    }

    private Consumer<UserProfileDTO> processSingleCommand(UserId userId, AtomicInteger processingCounter) {
        return userProfileDTO -> {
            try {
                update(userId, userProfileDTO);
                processingCounter.incrementAndGet();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Processing failed for command {0}. Command payload: {1}",
                        new Object[] {processingCounter.incrementAndGet(), userProfileDTO});
                throw e;
            }
        };
    }

    private UserProfileHandler getUpdaterInstance(CommandType commandType) {
        return updateableMap.get(commandType);
    }
}
