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
import org.jeecg.modules.system.entity.SysOrgan;
import org.jeecg.modules.system.mapper.SysOrganMapper;
import org.jeecg.modules.system.model.OrganIdModel;
import org.jeecg.modules.system.model.SysOrganTreeModel;
import org.jeecg.modules.system.service.ISysOrganService;
import org.jeecg.modules.system.util.FindsOrgansChildrenUtil;
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
public class SysOrganServiceImpl extends ServiceImpl<SysOrganMapper, SysOrgan> implements ISysOrganService {

	/**
	 * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
	 */
	@Cacheable(value = CacheConstant.SYS_DEPARTS_CACHE)
	@Override
	public List<SysOrganTreeModel> queryTreeList() {
		LambdaQueryWrapper<SysOrgan> query = new LambdaQueryWrapper<SysOrgan>();
		query.eq(SysOrgan::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		query.orderByAsc(SysOrgan::getOrgOrder);
		List<SysOrgan> list = this.list(query);
		// 调用wrapTreeDataToTreeList方法生成树状数据
		List<SysOrganTreeModel> listResult = FindsOrgansChildrenUtil.wrapTreeDataToTreeList(list);
		return listResult;
	}

	@Cacheable(value = CacheConstant.SYS_DEPART_IDS_CACHE)
	@Override
	public List<OrganIdModel> queryOrganIdTreeList() {
		LambdaQueryWrapper<SysOrgan> query = new LambdaQueryWrapper<SysOrgan>();
		query.eq(SysOrgan::getDelFlag, CommonConstant.DEL_FLAG_0.toString());
		query.orderByAsc(SysOrgan::getOrgOrder);
		List<SysOrgan> list = this.list(query);
		// 调用wrapTreeDataToTreeList方法生成树状数据
		List<OrganIdModel> listResult = FindsOrgansChildrenUtil.wrapTreeDataToOrganIdTreeList(list);
		return listResult;
	}

	/**
	 * saveOrganData 对应 add 保存用户在页面添加的新的部门对象数据
	 */
	@Override
	@Transactional
	public void saveOrganData(SysOrgan sysOrgan, String username) {
		if (sysOrgan != null && username != null) {
			if (sysOrgan.getParentId() == null) {
				sysOrgan.setParentId("");
			}
			String s = UUID.randomUUID().toString().replace("-", "");
			sysOrgan.setId(s);
			// 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
			// 获取父级ID
			String parentId = sysOrgan.getParentId();
			//update-begin--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
			JSONObject formData = new JSONObject();
			formData.put("parentId",parentId);
			String[] codeArray = (String[]) FillRuleUtil.executeRule("org_num_role",formData);
			//update-end--Author:baihailong  Date:20191209 for：部门编码规则生成器做成公用配置
			sysOrgan.setOrgCode(codeArray[0]);
			String orgType = codeArray[1];
			sysOrgan.setOrgType(String.valueOf(orgType));
			sysOrgan.setCreateTime(new Date());
			sysOrgan.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
			this.save(sysOrgan);
		}

	}
	
	/**
	 * saveOrganData 的调用方法,生成部门编码和部门类型（作废逻辑）
	 * @deprecated
	 * @param parentId
	 * @return
	 */
	private String[] generateOrgCode(String parentId) {	
		//update-begin--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
				LambdaQueryWrapper<SysOrgan> query = new LambdaQueryWrapper<SysOrgan>();
				LambdaQueryWrapper<SysOrgan> query1 = new LambdaQueryWrapper<SysOrgan>();
				String[] strArray = new String[2];
		        // 创建一个List集合,存储查询返回的所有SysOrgan对象
		        List<SysOrgan> organList = new ArrayList<>();
				// 定义新编码字符串
				String newOrgCode = "";
				// 定义旧编码字符串
				String oldOrgCode = "";
				// 定义部门类型
				String orgType = "";
				// 如果是最高级,则查询出同级的org_code, 调用工具类生成编码并返回
				if (StringUtil.isNullOrEmpty(parentId)) {
					// 线判断数据库中的表是否为空,空则直接返回初始编码
					query1.eq(SysOrgan::getParentId, "").or().isNull(SysOrgan::getParentId);
					query1.orderByDesc(SysOrgan::getOrgCode);
					organList = this.list(query1);
					if(organList == null || organList.size() == 0) {
						strArray[0] = YouBianCodeUtil.getNextYouBianCode(null);
						strArray[1] = "1";
						return strArray;
					}else {
					SysOrgan organ = organList.get(0);
					oldOrgCode = organ.getOrgCode();
					orgType = organ.getOrgType();
					newOrgCode = YouBianCodeUtil.getNextYouBianCode(oldOrgCode);
					}
				} else { // 反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
					// 封装查询同级的条件
					query.eq(SysOrgan::getParentId, parentId);
					// 降序排序
					query.orderByDesc(SysOrgan::getOrgCode);
					// 查询出同级部门的集合
					List<SysOrgan> parentList = this.list(query);
					// 查询出父级部门
					SysOrgan organ = this.getById(parentId);
					// 获取父级部门的Code
					String parentCode = organ.getOrgCode();
					// 根据父级部门类型算出当前部门的类型
					orgType = String.valueOf(Integer.valueOf(organ.getOrgType()) + 1);
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
	 * removeOrganDataById 对应 delete方法 根据ID删除相关部门数据
	 * 
	 */
	/*
	 * @Override
	 * 
	 * @Transactional public boolean removeOrganDataById(String id) {
	 * System.out.println("要删除的ID 为=============================>>>>>"+id); boolean
	 * flag = this.removeById(id); return flag; }
	 */

	/**
	 * updateOrganDataById 对应 edit 根据部门主键来更新对应的部门数据
	 */
	@Override
	@Transactional
	public Boolean updateOrganDataById(SysOrgan sysOrgan, String username) {
		if (sysOrgan != null && username != null) {
			sysOrgan.setUpdateTime(new Date());
			sysOrgan.setUpdateBy(username);
			this.updateById(sysOrgan);
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
	public List<SysOrganTreeModel> searhBy(String keyWord) {
		LambdaQueryWrapper<SysOrgan> query = new LambdaQueryWrapper<SysOrgan>();
		query.like(SysOrgan::getOrgName, keyWord);
		//update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]组织机构搜索回显优化--------------------
		SysOrganTreeModel model = new SysOrganTreeModel();
		List<SysOrgan> organList = this.list(query);
		List<SysOrganTreeModel> newList = new ArrayList<>();
		if(organList.size() > 0) {
			for(SysOrgan organ : organList) {
				model = new SysOrganTreeModel(organ);
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
		//FindsOrgansChildrenUtil.clearOrganIdModel();
		boolean ok = this.removeByIds(idList);
		return ok;
	}
	
	/**
	 * delete 方法调用
	 * @param id
	 * @param idList
	 */
	private void checkChildrenExists(String id, List<String> idList) {	
		LambdaQueryWrapper<SysOrgan> query = new LambdaQueryWrapper<SysOrgan>();
		query.eq(SysOrgan::getParentId,id);
		List<SysOrgan> organList = this.list(query);
		if(organList != null && organList.size() > 0) {
			for(SysOrgan organ : organList) {
				idList.add(organ.getId());
				this.checkChildrenExists(organ.getId(), idList);
			}
		}
	}

	@Override
	public List<SysOrgan> queryUserOrgans(String userId) {
		return baseMapper.queryUserOrgans(userId);
	}

	@Override
	public List<SysOrgan> queryOrgansByUsername(String username) {
		return baseMapper.queryOrgansByUsername(username);
	}

}
