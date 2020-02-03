package org.jeecg.modules.system.service;


import java.util.List;

import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserOrgan;
import org.jeecg.modules.system.model.OrganIdModel;


import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * SysUserDpeart用户组织机构service
 * </p>
 * @Author ZhiLin
 *
 */
public interface ISysUserOrganService extends IService<SysUserOrgan> {
	

	/**
	 * 根据指定用户id查询部门信息
	 * @param userId
	 * @return
	 */
	List<OrganIdModel> queryOrganIdsOfUser(String userId);
	

	/**
	 * 根据部门id查询用户信息
	 * @param orgId
	 * @return
	 */
	List<SysUser> queryUserByOrgId(String orgId);
}
