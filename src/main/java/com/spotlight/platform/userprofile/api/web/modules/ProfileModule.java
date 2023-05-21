package com.spotlight.platform.userprofile.api.web.modules;

import com.google.inject.AbstractModule;

import com.google.inject.multibindings.MapBinder;
import com.spotlight.platform.userprofile.api.core.profile.UserProfileService;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDaoInMemory;
import com.spotlight.platform.userprofile.api.core.profile.update.CollectHandler;
import com.spotlight.platform.userprofile.api.core.profile.update.IncrementHandler;
import com.spotlight.platform.userprofile.api.core.profile.update.ReplaceHandler;
import com.spotlight.platform.userprofile.api.core.profile.update.UserProfileHandler;
import com.spotlight.platform.userprofile.api.dto.CommandType;

import javax.inject.Singleton;

public class ProfileModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserProfileDao.class).to(UserProfileDaoInMemory.class).in(Singleton.class);
        bind(UserProfileService.class).in(Singleton.class);

        MapBinder<CommandType, UserProfileHandler> mapBinder = MapBinder.newMapBinder(binder(), CommandType.class, UserProfileHandler.class);
        mapBinder.addBinding(CommandType.REPLACE).to(ReplaceHandler.class);
        mapBinder.addBinding(CommandType.INCREMENT).to(IncrementHandler.class);
        mapBinder.addBinding(CommandType.COLLECT).to(CollectHandler.class);
        // add other command's bindings here
    }
}
