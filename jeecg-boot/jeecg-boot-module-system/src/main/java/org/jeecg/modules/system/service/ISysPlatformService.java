package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysPlatform;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 平台表
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
public interface ISysPlatformService extends IService<SysPlatform> {

    /**
     * 查询SysPlatform集合
     * @param userId
     * @return
     */
    List<SysPlatform> queryUserPlatforms(String userId,Integer userType);

    /**
     * 更新平台组织机构
     * @param platformId
     * @param orgs
     */
    void editPlatformOrgs(String platformId, String orgs);
}
