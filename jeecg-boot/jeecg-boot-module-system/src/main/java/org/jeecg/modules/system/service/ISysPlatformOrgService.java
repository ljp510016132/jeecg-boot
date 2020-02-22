package org.jeecg.modules.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysPlatformOrg;
import org.jeecg.modules.system.model.OrgIdModel;

import java.util.List;

/**
 * <p>
 * SysUserDpeart用户组织机构service
 * </p>
 * @Author ZhiLin
 *
 */
public interface ISysPlatformOrgService extends IService<SysPlatformOrg> {
	

	/**
	 * 根据平台Id查询拥有的机构树
	 * @param platformId
	 * @return
	 */
	List<OrgIdModel> queryOrgIdsOfPlatform(String platformId);

}
