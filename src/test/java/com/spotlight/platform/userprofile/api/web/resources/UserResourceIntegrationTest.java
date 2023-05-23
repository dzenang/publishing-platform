package com.spotlight.platform.userprofile.api.web.resources;

import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDaoInMemory;
import com.spotlight.platform.userprofile.api.dto.UserProfileDTO;
import com.spotlight.platform.userprofile.api.model.profile.UserProfile;
import com.spotlight.platform.userprofile.api.model.profile.primitives.UserProfileFixtures;
import com.spotlight.platform.userprofile.api.web.UserProfileApiApplication;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.vyarus.dropwizard.guice.test.ClientSupport;
import ru.vyarus.dropwizard.guice.test.jupiter.ext.TestDropwizardAppExtension;

import javax.ws.rs.client.Entity;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
class UserResourceIntegrationTest {
    @RegisterExtension
    static TestDropwizardAppExtension APP = TestDropwizardAppExtension.forApp(UserProfileApiApplication.class)
            .randomPorts()
            .create();

    @Nested
    @DisplayName("getUserProfile")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GetUserProfile {
        private static final String USER_ID_PATH_PARAM = "userId";
        private static final String URL = "/users/{%s}/profile".formatted(USER_ID_PATH_PARAM);

        @BeforeAll
        public void setUp(UserProfileDao userProfileDao) {
            ((UserProfileDaoInMemory) userProfileDao).clear();
        }

        @Test
        void existingUser_correctObjectIsReturned(ClientSupport client, UserProfileDao userProfileDao) {
            userProfileDao.put(UserProfileFixtures.USER_PROFILE);

            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request().get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThatJson(response.readEntity(UserProfile.class)).isEqualTo(UserProfileFixtures.SERIALIZED_USER_PROFILE);
        }

        @Test
        void nonExistingUser_returns404(ClientSupport client) {
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.NON_EXISTING_USER_ID).request().get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND_404);
        }

        @Test
        void validationFailed_returns400(ClientSupport client) {
            var response = client.targetRest()
                    .path(URL)
                    .resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.INVALID_USER_ID)
                    .request()
                    .get();

            assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST_400);
        }

//        @Test
//        void unhandledExceptionOccured_returns500(ClientSupport client, UserProfileService userProfileService) {
//            when(userProfileService.get(any(UserId.class))).thenThrow(new RuntimeException("Some unhandled exception"));
//
//            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID).request().get();
//
//            assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR_500);
//        }
    }

    @Nested
    @DisplayName("updateUserProfileWithReplace")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateUserProfileWithReplace {
        private static final String USER_ID_PATH_PARAM = "userId";
        private static final String URL = "/users/{%s}/profile".formatted(USER_ID_PATH_PARAM);

        @BeforeAll
        public void setUp(UserProfileDao userProfileDao) {
            ((UserProfileDaoInMemory) userProfileDao).clear();
        }

        @Test
        @Order(1)
        void updateWithReplace_correctObjectIsCreatedAndReturned(ClientSupport client, UserProfileDao userProfileDao) {
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isNotPresent();
            UserProfileDTO userProfileDTO = UserProfileFixtures.USER_PROFILE_DTO_REPLACE;
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID)
                    .request().put(Entity.json(userProfileDTO));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isPresent();
            assertThatJson(response.readEntity(UserProfile.class)).isEqualTo(userProfileDao.get(UserProfileFixtures.USER_ID));
        }

        @Test
        @Order(2)
        void updateWithReplace_existingObjectIsUpdatedAndReturned(ClientSupport client, UserProfileDao userProfileDao) {
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isPresent();
            UserProfileDTO userProfileDTO = UserProfileFixtures.USER_PROFILE_DTO_REPLACE_UPDATED;
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID)
                    .request().put(Entity.json(userProfileDTO));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isPresent();
            assertThatJson(response.readEntity(UserProfile.class).userProfileProperties())
                    .isEqualTo(userProfileDTO.properties());
        }
    }

    @Nested
    @DisplayName("updateUserProfileWithIncrement")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateUserProfileWithIncrement {
        private static final String USER_ID_PATH_PARAM = "userId";
        private static final String URL = "/users/{%s}/profile".formatted(USER_ID_PATH_PARAM);

        @BeforeAll
        public void setUp(UserProfileDao userProfileDao) {
            ((UserProfileDaoInMemory) userProfileDao).clear();
        }

        @Test
        @Order(1)
        void updateWithIncrement_correctObjectIsCreatedAndReturned(ClientSupport client, UserProfileDao userProfileDao) {
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isNotPresent();
            UserProfileDTO userProfileDTO = UserProfileFixtures.USER_PROFILE_DTO_INCREMENT;
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID)
                    .request().put(Entity.json(userProfileDTO));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isPresent();
            assertThatJson(response.readEntity(UserProfile.class)).isEqualTo(userProfileDao.get(UserProfileFixtures.USER_ID));
        }

        @Test
        @Order(2)
        void updateWithIncrement_existingObjectIsUpdatedAndReturned(ClientSupport client, UserProfileDao userProfileDao) {
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isPresent();
            UserProfileDTO userProfileDTO = UserProfileFixtures.USER_PROFILE_DTO_INCREMENT;
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID)
                    .request().put(Entity.json(userProfileDTO));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isPresent();
            assertThatJson(response.readEntity(UserProfile.class).userProfileProperties())
                    .isEqualTo(UserProfileFixtures.USER_PROFILE_DTO_INCREMENT_UPDATED.properties());
        }
    }

    @Nested
    @DisplayName("updateUserProfileWithCollect")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UpdateUserProfileWithCollect {
        private static final String USER_ID_PATH_PARAM = "userId";
        private static final String URL = "/users/{%s}/profile".formatted(USER_ID_PATH_PARAM);

        @BeforeAll
        public void setUp(UserProfileDao userProfileDao) {
            ((UserProfileDaoInMemory) userProfileDao).clear();
        }

        @Test
        @Order(1)
        void updateWithCollect_correctObjectIsCreatedAndReturned(ClientSupport client, UserProfileDao userProfileDao) {
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isNotPresent();
            UserProfileDTO userProfileDTO = UserProfileFixtures.USER_PROFILE_DTO_COLLECT;
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID)
                    .request().put(Entity.json(userProfileDTO));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isPresent();
            assertThatJson(response.readEntity(UserProfile.class)).isEqualTo(userProfileDao.get(UserProfileFixtures.USER_ID));
        }

        @Test
        @Order(2)
        void updateWithCollect_existingObjectIsUpdatedAndReturned(ClientSupport client, UserProfileDao userProfileDao) {
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isPresent();
            UserProfileDTO userProfileDTO = UserProfileFixtures.USER_PROFILE_DTO_COLLECT;
            var response = client.targetRest().path(URL).resolveTemplate(USER_ID_PATH_PARAM, UserProfileFixtures.USER_ID)
                    .request().put(Entity.json(userProfileDTO));

            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK_200);
            assertThat(userProfileDao.get(UserProfileFixtures.USER_ID)).isPresent();
            assertThatJson(response.readEntity(UserProfile.class).userProfileProperties())
                    .isEqualTo(UserProfileFixtures.USER_PROFILE_DTO_COLLECT_UPDATED.properties());
        }
    }

    //todo add tests for /batch-update
}