package org.jeecg.modules.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.jeecg.modules.system.entity.SysOrgan;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserOrgan;
import org.jeecg.modules.system.mapper.SysUserOrganMapper;
import org.jeecg.modules.system.model.OrganIdModel;
import org.jeecg.modules.system.service.ISysOrganService;
import org.jeecg.modules.system.service.ISysUserOrganService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <P>
 * 用户部门表实现类
 * <p/>
 * @Author ZhiLin
 *@since 2019-02-22
 */
@Service
public class SysUserOrganServiceImpl extends ServiceImpl<SysUserOrganMapper, SysUserOrgan> implements ISysUserOrganService {
	@Autowired
	private ISysOrganService sysOrganService;
	@Autowired
	private ISysUserService sysUserService;
	

	/**
	 * 根据用户id查询部门信息
	 */
	@Override
	public List<OrganIdModel> queryOrganIdsOfUser(String userId) {
		LambdaQueryWrapper<SysUserOrgan> queryUDep = new LambdaQueryWrapper<SysUserOrgan>();
		LambdaQueryWrapper<SysOrgan> queryDep = new LambdaQueryWrapper<SysOrgan>();
		try {
			queryUDep.eq(SysUserOrgan::getUserId, userId);
			List<String> orgIdList = new ArrayList<>();
			List<OrganIdModel> orgIdModelList = new ArrayList<>();
			List<SysUserOrgan> userDepList = this.list(queryUDep);
			if(userDepList != null && userDepList.size() > 0) {
			for(SysUserOrgan userOrgan : userDepList) {
					orgIdList.add(userOrgan.getOrgId());
				}
			queryDep.in(SysOrgan::getId, orgIdList);
			List<SysOrgan> depList = sysOrganService.list(queryDep);
			if(depList != null || depList.size() > 0) {
				for(SysOrgan organ : depList) {
					orgIdModelList.add(new OrganIdModel().convertByUserOrgan(organ));
				}
			}
			return orgIdModelList;
			}
		}catch(Exception e) {
			e.fillInStackTrace();
		}
		return null;
		
		
	}


	/**
	 * 根据部门id查询用户信息
	 */
	@Override
	public List<SysUser> queryUserByOrgId(String orgId) {
		LambdaQueryWrapper<SysUserOrgan> queryUDep = new LambdaQueryWrapper<SysUserOrgan>();
		queryUDep.eq(SysUserOrgan::getOrgId, orgId);
		List<String> userIdList = new ArrayList<>();
		List<SysUserOrgan> uDepList = this.list(queryUDep);
		if(uDepList != null && uDepList.size() > 0) {
			for(SysUserOrgan uDep : uDepList) {
				userIdList.add(uDep.getUserId());
			}
			List<SysUser> userList = (List<SysUser>) sysUserService.listByIds(userIdList);
			//update-begin-author:taoyan date:201905047 for:接口调用查询返回结果不能返回密码相关信息
			for (SysUser sysUser : userList) {
				sysUser.setSalt("");
				sysUser.setPassword("");
			}
			//update-end-author:taoyan date:201905047 for:接口调用查询返回结果不能返回密码相关信息
			return userList;
		}
		return new ArrayList<SysUser>();
	}
	
}
