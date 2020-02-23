package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysPlatform;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 平台表
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
public interface SysPlatformMapper extends BaseMapper<SysPlatform> {
    /**
     * 根据用户ID查询部门集合
     * @param superAdmin 是否为超级管理员
     */
    public List<SysPlatform> queryUserPlatforms(@Param("userId") String userId,@Param("superAdmin") boolean superAdmin);

}
