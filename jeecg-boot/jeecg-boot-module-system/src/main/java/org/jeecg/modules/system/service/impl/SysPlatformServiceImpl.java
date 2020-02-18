package org.jeecg.modules.system.service.impl;

import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.system.entity.SysPlatform;
import org.jeecg.modules.system.mapper.SysPlatformMapper;
import org.jeecg.modules.system.service.ISysPlatformService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 平台表
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Service
public class SysPlatformServiceImpl extends ServiceImpl<SysPlatformMapper, SysPlatform> implements ISysPlatformService {

    @Override
    public List<SysPlatform> queryUserPlatforms(String userId,String username) {
        //如果用户是超级管理员则拥有所有平台
        if(username.equals(CommonConstant.SUPER_ADMIN_NAME)){
            return baseMapper.queryUserPlatforms(userId,null);
        }else{
            return baseMapper.queryUserPlatforms(userId,username);
        }
    }
}
