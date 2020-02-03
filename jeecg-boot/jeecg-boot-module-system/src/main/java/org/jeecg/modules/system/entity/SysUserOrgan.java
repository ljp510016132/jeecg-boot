package org.jeecg.modules.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
@Data
@TableName("sys_user_organ")
public class SysUserOrgan implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**主键id*/
    @TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**用户id*/
	private String userId;
	/**部门id*/
	private String orgId;
	public SysUserOrgan(String id, String userId, String orgId) {
		super();
		this.id = id;
		this.userId = userId;
		this.orgId = orgId;
	}

	public SysUserOrgan(String id, String organId) {
		this.userId = id;
		this.orgId = organId;
	}
}
