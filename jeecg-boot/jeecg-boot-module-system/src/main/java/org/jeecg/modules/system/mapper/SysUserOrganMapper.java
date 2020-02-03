package org.jeecg.modules.system.mapper;

import java.util.List;

import org.jeecg.modules.system.entity.SysUserOrgan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.lettuce.core.dynamic.annotation.Param;

public interface SysUserOrganMapper extends BaseMapper<SysUserOrgan>{
	
	List<SysUserOrgan> getUserOrganByUid(@Param("userId") String userId);
}
