package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysOrg;
import org.jeecg.modules.system.model.SysOrgTreeModel;
import org.jeecg.modules.system.model.TreeModel;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 * 
 * @Author: Steve
 * @Since：   2019-01-22
 */
public interface SysOrgMapper extends BaseMapper<SysOrg> {
	
	/**
	 * 根据用户ID查询部门集合
	 */
	public List<SysOrg> queryUserOrgs(@Param("userId") String userId);

	/**
	 * 根据用户名查询部门
	 *
	 * @param username
	 * @return
	 */
	public List<SysOrg> queryOrgsByUsername(@Param("username") String username);

	@Select("select id from sys_org where org_code=#{orgCode}")
	public String queryOrgIdByOrgCode(@Param("orgCode") String orgCode);

	@Select("select id,parent_id from sys_org where id=#{orgId}")
	public SysOrg getParentOrgId(@Param("orgId") String orgId);

}
