package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.model.SysUserSysOrgModel;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @Author: scott
 * @Date: 2018-12-20
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
	
	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private SysPermissionMapper sysPermissionMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	private SysUserOrgMapper sysUserOrgMapper;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private SysOrgMapper sysOrgMapper;

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword) {
        SysUser user = userMapper.getUserByName(username);
        String passwordEncode = PasswordUtil.encrypt(username, oldpassword, user.getSalt());
        if (!user.getPassword().equals(passwordEncode)) {
            return Result.error("旧密码输入错误!");
        }
        if (oConvertUtils.isEmpty(newpassword)) {
            return Result.error("新密码不允许为空!");
        }
        if (!newpassword.equals(confirmpassword)) {
            return Result.error("两次输入密码不一致!");
        }
        String password = PasswordUtil.encrypt(username, newpassword, user.getSalt());
        this.userMapper.update(new SysUser().setPassword(password), new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
        return Result.ok("密码重置成功!");
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public Result<?> changePassword(SysUser sysUser) {
        String salt = oConvertUtils.randomGen(8);
        sysUser.setSalt(salt);
        String password = sysUser.getPassword();
        String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
        sysUser.setPassword(passwordEncode);
        this.userMapper.updateById(sysUser);
        return Result.ok("密码修改成功!");
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteUser(String userId) {
		//1.删除用户
		this.removeById(userId);
		//2.删除用户部门关联关系
		LambdaQueryWrapper<SysUserOrg> query = new LambdaQueryWrapper<SysUserOrg>();
		query.eq(SysUserOrg::getUserId, userId);
		sysUserOrgMapper.delete(query);
		//3.删除用户角色关联关系
		//TODO
		return false;
	}

	@Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteBatchUsers(String userIds) {
		//1.删除用户
		this.removeByIds(Arrays.asList(userIds.split(",")));
		//2.删除用户部门关系
		LambdaQueryWrapper<SysUserOrg> query = new LambdaQueryWrapper<SysUserOrg>();
		for(String id : userIds.split(",")) {
			query.eq(SysUserOrg::getUserId, id);
			this.sysUserOrgMapper.delete(query);
		}
		//3.删除用户角色关系
		//TODO
		return false;
	}

	@Override
	public SysUser getUserByName(String username) {
		return userMapper.getUserByName(username);
	}
	
	
	@Override
	@Transactional
	public void addUserWithRole(SysUser user, String roles) {
		this.save(user);
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
	}

	@Override
	@CacheEvict(value= {CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	@Transactional
	public void editUserWithRole(SysUser user, String roles) {
		this.updateById(user);
		//先删后加
		sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
	}


	@Override
	public List<String> getRole(String username) {
		return sysUserRoleMapper.getRoleByUserName(username);
	}
	
	/**
	 * 通过用户名获取用户角色集合
	 * @param username 用户名
     * @return 角色集合
	 */
	@Override
	public Set<String> getUserRolesSet(String username) {
		// 查询用户拥有的角色集合
		List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
		log.info("-------通过数据库读取用户拥有的角色Rules------username： " + username + ",Roles size: " + (roles == null ? 0 : roles.size()));
		return new HashSet<>(roles);
	}

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	@Override
	public Set<String> getUserPermissionsSet(String username) {
		Set<String> permissionSet = new HashSet<>();
		List<SysPermission> permissionList = sysPermissionMapper.queryByUser(username);
		for (SysPermission po : permissionList) {
//			// TODO URL规则有问题？
//			if (oConvertUtils.isNotEmpty(po.getUrl())) {
//				permissionSet.add(po.getUrl());
//			}
			if (oConvertUtils.isNotEmpty(po.getPerms())) {
				permissionSet.add(po.getPerms());
			}
		}
		log.info("-------通过数据库读取用户拥有的权限Perms------username： "+ username+",Perms size: "+ (permissionSet==null?0:permissionSet.size()) );
		return permissionSet;
	}

	@Override
	public SysUserCacheInfo getCacheUser(String username) {
		SysUserCacheInfo info = new SysUserCacheInfo();
		info.setOneOrg(true);
//		SysUser user = userMapper.getUserByName(username);
//		info.setSysUserCode(user.getUsername());
//		info.setSysUserName(user.getRealname());
		

		LoginUser user = sysBaseAPI.getUserByName(username);
		if(user!=null) {
			info.setSysUserCode(user.getUsername());
			info.setSysUserName(user.getRealname());
			info.setSysOrgCode(user.getOrgCode());
		}
		
		//多部门支持in查询
		List<SysOrg> list = sysOrgMapper.queryUserOrgs(user.getId());
		List<String> sysMultiOrgCode = new ArrayList<String>();
		if(list==null || list.size()==0) {
			//当前用户无部门
			//sysMultiOrgCode.add("0");
		}else if(list.size()==1) {
			sysMultiOrgCode.add(list.get(0).getOrgCode());
		}else {
			info.setOneOrg(false);
			for (SysOrg dpt : list) {
				sysMultiOrgCode.add(dpt.getOrgCode());
			}
		}
		info.setSysMultiOrgCode(sysMultiOrgCode);
		
		return info;
	}

	// 根据部门Id查询
	@Override
	public IPage<SysUser> getUserByOrgId(Page<SysUser> page, String orgId,String username) {
		return userMapper.getUserByOrgId(page, orgId,username);
	}

	@Override
	public IPage<SysUser> getUserByOrgIdAndQueryWrapper(Page<SysUser> page, String orgId, QueryWrapper<SysUser> queryWrapper) {
		LambdaQueryWrapper<SysUser> lambdaQueryWrapper = queryWrapper.lambda();

		lambdaQueryWrapper.eq(SysUser::getDelFlag, "0");
        lambdaQueryWrapper.inSql(SysUser::getId, "SELECT user_id FROM sys_user_org WHERE org_id = '" + orgId + "'");

        return userMapper.selectPage(page, lambdaQueryWrapper);
	}

	@Override
	public IPage<SysUserSysOrgModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page) {
		List<SysUserSysOrgModel> list = baseMapper.getUserByOrgCode(page, orgCode, userParams);
		Integer total = baseMapper.getUserByOrgCodeTotal(orgCode, userParams);

		IPage<SysUserSysOrgModel> result = new Page<>(page.getCurrent(), page.getSize(), total);
		result.setRecords(list);

		return result;
	}

	// 根据角色Id查询
	@Override
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username) {
		return userMapper.getUserByRoleId(page,roleId,username);
	}


	@Override
	@CacheEvict(value= {CacheConstant.SYS_USERS_CACHE}, key="#username")
	public void updateUserOrg(String username,String orgCode) {
		baseMapper.updateUserOrg(username, orgCode);
	}


	@Override
	public SysUser getUserByPhone(String phone) {
		return userMapper.getUserByPhone(phone);
	}


	@Override
	public SysUser getUserByEmail(String email) {
		return userMapper.getUserByEmail(email);
	}

	@Override
	@Transactional
	public void addUserWithOrg(SysUser user, String selectedParts) {
//		this.save(user);  //保存角色的时候已经添加过一次了
		if(oConvertUtils.isNotEmpty(selectedParts)) {
			String[] arr = selectedParts.split(",");
			for (String deaprtId : arr) {
				SysUserOrg userDeaprt = new SysUserOrg(user.getId(), deaprtId);
				sysUserOrgMapper.insert(userDeaprt);
			}
		}
	}


	@Override
	@Transactional
	@CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
	public void editUserWithOrg(SysUser user, String orgs) {
		this.updateById(user);  //更新角色的时候已经更新了一次了，可以再跟新一次
		//先删后加
		sysUserOrgMapper.delete(new QueryWrapper<SysUserOrg>().lambda().eq(SysUserOrg::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(orgs)) {
			String[] arr = orgs.split(",");
			for (String orgId : arr) {
				SysUserOrg userOrg = new SysUserOrg(user.getId(), orgId);
				sysUserOrgMapper.insert(userOrg);
			}
		}
	}


	/**
	   * 校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	@Override
	public Result<?> checkUserIsEffective(SysUser sysUser) {
		Result<?> result = new Result<Object>();
		//情况1：根据用户信息查询，该用户不存在
		if (sysUser == null) {
			result.error500("该用户不存在，请注册");
			sysBaseAPI.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null);
			return result;
		}
		//情况2：根据用户信息查询，该用户已注销
		if (CommonConstant.DEL_FLAG_1.toString().equals(sysUser.getDelFlag())) {
			sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
			result.error500("该用户已注销");
			return result;
		}
		//情况3：根据用户信息查询，该用户已冻结
		if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
			sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
			result.error500("该用户已冻结");
			return result;
		}
		return result;
	}
}
