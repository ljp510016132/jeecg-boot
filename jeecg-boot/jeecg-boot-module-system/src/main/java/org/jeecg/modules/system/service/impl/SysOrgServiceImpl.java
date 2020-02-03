package org.jeecg.modules.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.FillRuleUtil;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.modules.system.entity.SysOrg;
import org.jeecg.modules.system.mapper.SysOrgMapper;
import org.jeecg.modules.system.model.OrgIdModel;
import org.jeecg.modules.system.model.SysOrgTreeModel;
import org.jeecg.modules.system.service.ISysOrgService;
import org.jeecg.modules.system.util.FindsOrgsChildrenUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.netty.util.internal.StringUtil;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 * 
 * @Author Steve
 * @Since 2019-01-22
 */
@Service
public class SysOrgServiceImpl extends ServiceImpl<SysOrgMapper, SysOrg> implements ISysOrgService {

	/**
	 * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
	 */
	@Cacheable(value = CacheConstant.SYS_DEPARTS_CACHE)
	@Override
	public List<SysOrgTreeModel> queryTreeList() {
		LambdaQueryWrapper<SysOrg> query = new LambdaQueryWrapper<SysOrg>();
		query.eq(SysOrg::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		query.orderByAsc(SysOrg::getOrgOrder);
		List<SysOrg> list = this.list(query);
		// 调用wrapTreeDataToTreeList方法生成树状数据
		List<SysOrgTreeModel> listResult = FindsOrgsChildrenUtil.wrapTreeDataToTreeList(list);
		return listResult;
	}

	@Cacheable(value = CacheConstant.SYS_DEPART_IDS_CACHE)
	@Override
	public List<OrgIdModel> queryOrgIdTreeList() {
		LambdaQueryWrapper<SysOrg> query = new LambdaQueryWrapper<SysOrg>();
		query.eq(SysOrg::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		query.orderByAsc(SysOrg::getOrgOrder);
		List<SysOrg> list = this.list(query);
		// 调用wrapTreeDataToTreeList方法生成树状数据
		List<OrgIdModel> listResult = FindsOrgsChildrenUtil.wrapTreeDataToOrgIdTreeList(list);
		return listResult;
	}

	/**
	 * saveOrgData 对应 add 保存用户在页面添加的新的部门对象数据
	 */
	@Override
	@Transactional
	public void saveOrgData(SysOrg sysOrg, String username) {
		if (sysOrg != null && username != null) {
			if (sysOrg.getParentId() == null) {
				sysOrg.setParentId("");
			}
			String s = UUID.randomUUID().toString().replace("-", "");
			sysOrg.setId(s);
			// 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
			// 获取父级ID
			String parentId = sysOrg.getParentId();
			//update-begin--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
			JSONObject formData = new JSONObject();
			formData.put("parentId",parentId);
			String[] codeArray = (String[]) FillRuleUtil.executeRule("org_num_role",formData);
			//update-end--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
			sysOrg.setOrgCode(codeArray[0]);
			String orgType = codeArray[1];
			sysOrg.setOrgType(String.valueOf(orgType));
			sysOrg.setCreateTime(new Date());
			sysOrg.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
			this.save(sysOrg);
		}

	}
	
	/**
	 * saveOrgData 的调用方法,生成部门编码和部门类型（作废逻辑）
	 * @deprecated
	 * @param parentId
	 * @return
	 */
	private String[] generateOrgCode(String parentId) {	
		//update-begin--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
				LambdaQueryWrapper<SysOrg> query = new LambdaQueryWrapper<SysOrg>();
				LambdaQueryWrapper<SysOrg> query1 = new LambdaQueryWrapper<SysOrg>();
				String[] strArray = new String[2];
		        // 创建一个List集合,存储查询返回的所有SysOrg对象
		        List<SysOrg> orgList = new ArrayList<>();
				// 定义新编码字符串
				String newOrgCode = "";
				// 定义旧编码字符串
				String oldOrgCode = "";
				// 定义部门类型
				String orgType = "";
				// 如果是最高级,则查询出同级的org_code, 调用工具类生成编码并返回
				if (StringUtil.isNullOrEmpty(parentId)) {
					// 线判断数据库中的表是否为空,空则直接返回初始编码
					query1.eq(SysOrg::getParentId, "").or().isNull(SysOrg::getParentId);
					query1.orderByDesc(SysOrg::getOrgCode);
					orgList = this.list(query1);
					if(orgList == null || orgList.size() == 0) {
						strArray[0] = YouBianCodeUtil.getNextYouBianCode(null);
						strArray[1] = "1";
						return strArray;
					}else {
					SysOrg org = orgList.get(0);
					oldOrgCode = org.getOrgCode();
					orgType = org.getOrgType();
					newOrgCode = YouBianCodeUtil.getNextYouBianCode(oldOrgCode);
					}
				} else { // 反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
					// 封装查询同级的条件
					query.eq(SysOrg::getParentId, parentId);
					// 降序排序
					query.orderByDesc(SysOrg::getOrgCode);
					// 查询出同级部门的集合
					List<SysOrg> parentList = this.list(query);
					// 查询出父级部门
					SysOrg org = this.getById(parentId);
					// 获取父级部门的Code
					String parentCode = org.getOrgCode();
					// 根据父级部门类型算出当前部门的类型
					orgType = String.valueOf(Integer.valueOf(org.getOrgType()) + 1);
					// 处理同级部门为null的情况
					if (parentList == null || parentList.size() == 0) {
						// 直接生成当前的部门编码并返回
						newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, null);
					} else { //处理有同级部门的情况
						// 获取同级部门的编码,利用工具类
						String subCode = parentList.get(0).getOrgCode();
						// 返回生成的当前部门编码
						newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, subCode);
					}
				}
				// 返回最终封装了部门编码和部门类型的数组
				strArray[0] = newOrgCode;
				strArray[1] = orgType;
				return strArray;
		//update-end--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
	} 

	
	/**
	 * removeOrgDataById 对应 delete方法 根据ID删除相关部门数据
	 * 
	 */
	/*
	 * @Override
	 * 
	 * @Transactional public boolean removeOrgDataById(String id) {
	 * System.out.println("要删除的ID 为=============================>>>>>"+id); boolean
	 * flag = this.removeById(id); return flag; }
	 */

	/**
	 * updateOrgDataById 对应 edit 根据部门主键来更新对应的部门数据
	 */
	@Override
	@Transactional
	public Boolean updateOrgDataById(SysOrg sysOrg, String username) {
		if (sysOrg != null && username != null) {
			sysOrg.setUpdateTime(new Date());
			sysOrg.setUpdateBy(username);
			this.updateById(sysOrg);
			return true;
		} else {
			return false;
		}

	}
	
	@Override
	@Transactional
	public void deleteBatchWithChildren(List<String> ids) {
		List<String> idList = new ArrayList<String>();
		for(String id: ids) {
			idList.add(id);
			this.checkChildrenExists(id, idList);
		}
		this.removeByIds(idList);

	}
	/**
	 * <p>
	 * 根据关键字搜索相关的部门数据
	 * </p>
	 */
	@Override
	public List<SysOrgTreeModel> searhBy(String keyWord) {
		LambdaQueryWrapper<SysOrg> query = new LambdaQueryWrapper<SysOrg>();
		query.like(SysOrg::getOrgName, keyWord);
		//update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索回显优化--------------------
		SysOrgTreeModel model = new SysOrgTreeModel();
		List<SysOrg> orgList = this.list(query);
		List<SysOrgTreeModel> newList = new ArrayList<>();
		if(orgList.size() > 0) {
			for(SysOrg org : orgList) {
				model = new SysOrgTreeModel(org);
				model.setChildren(null);
	    //update-end--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索功回显优化----------------------
				newList.add(model);
			}
			return newList;
		}
		return null;
	}

	/**
	 * 根据部门id删除并且删除其可能存在的子级任何部门
	 */
	@Override
	public boolean delete(String id) {
		List<String> idList = new ArrayList<>();
		idList.add(id);
		this.checkChildrenExists(id, idList);
		//清空部门树内存
		//FindsOrgsChildrenUtil.clearOrgIdModel();
		boolean ok = this.removeByIds(idList);
		return ok;
	}
	
	/**
	 * delete 方法调用
	 * @param id
	 * @param idList
	 */
	private void checkChildrenExists(String id, List<String> idList) {	
		LambdaQueryWrapper<SysOrg> query = new LambdaQueryWrapper<SysOrg>();
		query.eq(SysOrg::getParentId,id);
		List<SysOrg> orgList = this.list(query);
		if(orgList != null && orgList.size() > 0) {
			for(SysOrg org : orgList) {
				idList.add(org.getId());
				this.checkChildrenExists(org.getId(), idList);
			}
		}
	}

	@Override
	public List<SysOrg> queryUserOrgs(String userId) {
		return baseMapper.queryUserOrgs(userId);
	}

	@Override
	public List<SysOrg> queryOrgsByUsername(String username) {
		return baseMapper.queryOrgsByUsername(username);
	}

}
