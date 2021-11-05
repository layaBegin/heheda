package com.tanhua.dubbo.api.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion;
import com.tanhua.domain.db.Settings;
import com.tanhua.dubbo.api.SettingsApi;
import com.tanhua.dubbo.mapper.SettingsMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Service
public class SettingApiImpl implements SettingsApi {

    @Autowired
    private SettingsMapper settingsMapper;

    @Override
    public Settings findSettingsByUserId(Long id) {
        QueryWrapper<Settings> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",id);
        return settingsMapper.selectOne(wrapper);
    }

    @Override
    public Integer saveSettings(Settings setting) {
        return settingsMapper.insert(setting);
    }

    @Override
    public Integer update(Settings setting) {
        return settingsMapper.updateById(setting);
    }


}
