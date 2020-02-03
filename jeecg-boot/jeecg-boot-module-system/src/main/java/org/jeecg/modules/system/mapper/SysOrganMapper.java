package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysOrgan;
import org.jeecg.modules.system.model.SysOrganTreeModel;
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
public interface SysOrganMapper extends BaseMapper<SysOrgan> {
	
	/**
	 * 根据用户ID查询部门集合
	 */
	public List<SysOrgan> queryUserOrgans(@Param("userId") String userId);

	/**
	 * 根据用户名查询部门
	 *
	 * @param username
	 * @return
	 */
	public List<SysOrgan> queryOrgansByUsername(@Param("username") String username);

	@Select("select id from sys_organ where org_code=#{orgCode}")
	public String queryOrganIdByOrgCode(@Param("orgCode") String orgCode);

	@Select("select id,parent_id from sys_organ where id=#{organId}")
	public SysOrgan getParentOrganId(@Param("organId") String organId);

}
