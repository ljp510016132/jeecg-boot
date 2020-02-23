package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysPlatform;
import org.jeecg.modules.system.entity.SysPlatformOrg;
import org.jeecg.modules.system.mapper.SysPlatformMapper;
import org.jeecg.modules.system.mapper.SysPlatformOrgMapper;
import org.jeecg.modules.system.service.ISysPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 平台表
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Service
public class SysPlatformServiceImpl extends ServiceImpl<SysPlatformMapper, SysPlatform> implements ISysPlatformService {

    @Autowired
    SysPlatformOrgMapper sysPlatformOrgMapper;

    @Override
    public List<SysPlatform> queryUserPlatforms(String userId,Integer userType) {
        //如果用户是超级管理员则拥有所有平台
        if(userType.equals(CommonConstant.SUPER_ADMIN_TYPE)){
            return baseMapper.queryUserPlatforms(userId,true);
        }else{
            return baseMapper.queryUserPlatforms(userId,false);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public void editPlatformOrgs(String platformId, String orgs) {
        //先删后加
        sysPlatformOrgMapper.delete(new QueryWrapper<SysPlatformOrg>().lambda().eq(SysPlatformOrg::getPlatformId, platformId));
        if(oConvertUtils.isNotEmpty(orgs)) {
            String[] arr = orgs.split(",");
            for (String orgId : arr) {
                SysPlatformOrg platformOrg = new SysPlatformOrg(platformId, orgId);
                sysPlatformOrgMapper.insert(platformOrg);
            }
        }
    }

}
