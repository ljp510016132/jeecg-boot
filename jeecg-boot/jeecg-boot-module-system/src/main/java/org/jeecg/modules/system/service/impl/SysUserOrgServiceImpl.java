package org.jeecg.modules.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.jeecg.modules.system.entity.SysOrg;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserOrg;
import org.jeecg.modules.system.mapper.SysUserOrgMapper;
import org.jeecg.modules.system.model.OrgIdModel;
import org.jeecg.modules.system.service.ISysOrgService;
import org.jeecg.modules.system.service.ISysUserOrgService;
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
public class SysUserOrgServiceImpl extends ServiceImpl<SysUserOrgMapper, SysUserOrg> implements ISysUserOrgService {
	@Autowired
	private ISysOrgService sysOrgService;
	@Autowired
	private ISysUserService sysUserService;
	

	/**
	 * 根据用户id查询部门信息
	 */
	@Override
	public List<OrgIdModel> queryOrgIdsOfUser(String userId) {
		LambdaQueryWrapper<SysUserOrg> queryUDep = new LambdaQueryWrapper<SysUserOrg>();
		LambdaQueryWrapper<SysOrg> queryDep = new LambdaQueryWrapper<SysOrg>();
		try {
			queryUDep.eq(SysUserOrg::getUserId, userId);
			List<String> orgIdList = new ArrayList<>();
			List<OrgIdModel> orgIdModelList = new ArrayList<>();
			List<SysUserOrg> userDepList = this.list(queryUDep);
			if(userDepList != null && userDepList.size() > 0) {
			for(SysUserOrg userOrg : userDepList) {
					orgIdList.add(userOrg.getOrgId());
				}
			queryDep.in(SysOrg::getId, orgIdList);
			List<SysOrg> depList = sysOrgService.list(queryDep);
			if(depList != null || depList.size() > 0) {
				for(SysOrg org : depList) {
					orgIdModelList.add(new OrgIdModel().convertByUserOrg(org));
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
		LambdaQueryWrapper<SysUserOrg> queryUDep = new LambdaQueryWrapper<SysUserOrg>();
		queryUDep.eq(SysUserOrg::getOrgId, orgId);
		List<String> userIdList = new ArrayList<>();
		List<SysUserOrg> uDepList = this.list(queryUDep);
		if(uDepList != null && uDepList.size() > 0) {
			for(SysUserOrg uDep : uDepList) {
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
