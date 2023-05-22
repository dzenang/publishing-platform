package com.spotlight.platform.userprofile.api.core.profile.update;

import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserId;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReplaceHandlerTest {

    private final UserProfileDao userProfileDaoMock = mock(UserProfileDao.class);
    private final ReplaceHandler replaceHandler = new ReplaceHandler(userProfileDaoMock);
    private final ArgumentCaptor<UserProfile> userProfileArgumentCaptor = ArgumentCaptor.forClass(UserProfile.class);

    @Test
    void processWhenUserProfileNotExist_newUserProfileCreated() {
        when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.empty());

        UserProfileDTO userProfileDTO = UserProfileFixtures.USER_PROFILE_DTO_REPLACE;
        replaceHandler.process(userProfileDTO);

        verify(userProfileDaoMock).put(userProfileArgumentCaptor.capture());
        UserProfile userProfileCaptorValue = userProfileArgumentCaptor.getValue();
        assertThat(userProfileCaptorValue.userProfileProperties()).isEqualTo(userProfileDTO.properties());
    }

    @Test
    void processWhenUserProfileExists_notCreatingNewUserProfile() {
        when(userProfileDaoMock.get(any(UserId.class))).thenReturn(Optional.of(UserProfileFixtures.USER_PROFILE));

        UserProfileDTO userProfileDTO = UserProfileFixtures.USER_PROFILE_DTO_REPLACE;
        replaceHandler.process(userProfileDTO);

        verify(userProfileDaoMock).put(userProfileArgumentCaptor.capture());
        UserProfile userProfileCaptorValue = userProfileArgumentCaptor.getValue();
        assertThat(userProfileCaptorValue.userProfileProperties()).isNotEqualTo(userProfileDTO.properties());
    }
}