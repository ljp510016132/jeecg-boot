package org.jeecg.modules.system.mapper;

import java.util.List;

import org.jeecg.modules.system.entity.SysUserOrg;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.lettuce.core.dynamic.annotation.Param;

public interface SysUserOrgMapper extends BaseMapper<SysUserOrg>{
	
	List<SysUserOrg> getUserOrgByUid(@Param("userId") String userId);
}
