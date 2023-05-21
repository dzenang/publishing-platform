package com.spotlight.platform.userprofile.api.web.modules;

import com.google.inject.AbstractModule;

import com.google.inject.multibindings.MapBinder;
import com.spotlight.platform.userprofile.api.core.profile.UserProfileService;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDao;
import com.spotlight.platform.userprofile.api.core.profile.persistence.UserProfileDaoInMemory;
import com.spotlight.platform.userprofile.api.core.profile.update.IncrementPropertyValue;
import com.spotlight.platform.userprofile.api.core.profile.update.ReplacePropertyValue;
import com.spotlight.platform.userprofile.api.core.profile.update.Updateable;
import com.spotlight.platform.userprofile.api.dto.CommandType;

import javax.inject.Singleton;

public class ProfileModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserProfileDao.class).to(UserProfileDaoInMemory.class).in(Singleton.class);
        bind(UserProfileService.class).in(Singleton.class);

        MapBinder<CommandType, Updateable> mapBinder = MapBinder.newMapBinder(binder(), CommandType.class, Updateable.class);
        mapBinder.addBinding(CommandType.REPLACE).to(ReplacePropertyValue.class);
        mapBinder.addBinding(CommandType.INCREMENT).to(IncrementPropertyValue.class);
        // add other command's bindings
    }
}
