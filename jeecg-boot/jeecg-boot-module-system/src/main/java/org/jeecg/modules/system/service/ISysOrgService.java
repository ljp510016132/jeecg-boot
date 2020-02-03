package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysOrg;
import org.jeecg.modules.system.model.OrgIdModel;
import org.jeecg.modules.system.model.SysOrgTreeModel;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 * 
 * @Author:Steve
 * @Since：   2019-01-22
 */
public interface ISysOrgService extends IService<SysOrg>{


    /**
     * 查询所有部门信息,并分节点进行显示
     * @return
     */
    List<SysOrgTreeModel> queryTreeList();

    /**
     * 查询所有部门OrgId信息,并分节点进行显示
     * @return
     */
    public List<OrgIdModel> queryOrgIdTreeList();

    /**
     * 保存部门数据
     * @param sysOrg
     */
    void saveOrgData(SysOrg sysOrg,String username);

    /**
     * 更新org数据
     * @param sysOrg
     * @return
     */
    Boolean updateOrgDataById(SysOrg sysOrg,String username);
    
    /**
     * 删除org数据
     * @param id
     * @return
     */
	/* boolean removeOrgDataById(String id); */
    
    /**
     * 根据关键字搜索相关的部门数据
     * @param keyWord
     * @return
     */
    List<SysOrgTreeModel> searhBy(String keyWord);
    
    /**
     * 根据部门id删除并删除其可能存在的子级部门
     * @param id
     * @return
     */
    boolean delete(String id);
    
    /**
     * 查询SysOrg集合
     * @param userId
     * @return
     */
	public List<SysOrg> queryUserOrgs(String userId);

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    List<SysOrg> queryOrgsByUsername(String username);

	 /**
     * 根据部门id批量删除并删除其可能存在的子级部门
     * @param id
     * @return
     */
	void deleteBatchWithChildren(List<String> ids);
    
}
