package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_platform_org")
public class SysPlatformOrg implements Serializable {
	private static final long serialVersionUID = 1L;

	/**主键id*/
    @TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**平台id*/
	private String platformId;
	/**部门id*/
	private String orgId;
	public SysPlatformOrg(String id, String platformId, String orgId) {
		super();
		this.id = id;
		this.platformId = platformId;
		this.orgId = orgId;
	}

	public SysPlatformOrg(String id, String orgId) {
		this.platformId = id;
		this.orgId = orgId;
	}
}
