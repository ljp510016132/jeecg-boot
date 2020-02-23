package org.jeecg.modules.system.vo;

import lombok.Data;
import org.jeecg.modules.system.entity.SysRole;

import java.io.Serializable;
import java.util.List;

@Data
public class SysRolePage extends SysRole{
	/**
	 * 所属平台名称
	 */
	private String platformName;
}
