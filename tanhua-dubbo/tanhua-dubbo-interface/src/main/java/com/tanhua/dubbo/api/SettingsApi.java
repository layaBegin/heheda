package com.tanhua.dubbo.api;

import com.tanhua.domain.db.Settings;

public interface SettingsApi {

    //查找用户setting资料
    Settings findSettingsByUserId(Long id);

    Integer saveSettings(Settings setting);

    Integer update(Settings setting);
}
