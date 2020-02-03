package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysOrgan;
import org.jeecg.modules.system.model.OrganIdModel;
import org.jeecg.modules.system.model.SysOrganTreeModel;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 * 
 * @Author:Steve
 * @Since：   2019-01-22
 */
public interface ISysOrganService extends IService<SysOrgan>{


    /**
     * 查询所有部门信息,并分节点进行显示
     * @return
     */
    List<SysOrganTreeModel> queryTreeList();

    /**
     * 查询所有部门OrganId信息,并分节点进行显示
     * @return
     */
    public List<OrganIdModel> queryOrganIdTreeList();

    /**
     * 保存部门数据
     * @param sysOrgan
     */
    void saveOrganData(SysOrgan sysOrgan,String username);

    /**
     * 更新organ数据
     * @param sysOrgan
     * @return
     */
    Boolean updateOrganDataById(SysOrgan sysOrgan,String username);
    
    /**
     * 删除organ数据
     * @param id
     * @return
     */
	/* boolean removeOrganDataById(String id); */
    
    /**
     * 根据关键字搜索相关的部门数据
     * @param keyWord
     * @return
     */
    List<SysOrganTreeModel> searhBy(String keyWord);
    
    /**
     * 根据部门id删除并删除其可能存在的子级部门
     * @param id
     * @return
     */
    boolean delete(String id);
    
    /**
     * 查询SysOrgan集合
     * @param userId
     * @return
     */
	public List<SysOrgan> queryUserOrgans(String userId);

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    List<SysOrgan> queryOrgansByUsername(String username);

	 /**
     * 根据部门id批量删除并删除其可能存在的子级部门
     * @param id
     * @return
     */
	void deleteBatchWithChildren(List<String> ids);
    
}
