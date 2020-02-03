package org.jeecg.modules.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeecg.modules.system.entity.SysOrgan;

/**
 * <p>
 * 部门表 封装树结构的部门的名称的实体类
 * <p>
 * 
 * @Author Steve
 * @Since 2019-01-22 
 *
 */
public class OrganIdModel implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键ID
    private String key;

    // 主键ID
    private String value;

    // 部门名称
    private String title;
    
    List<OrganIdModel> children = new ArrayList<>();
    
    /**
     * 将SysOrganTreeModel的部分数据放在该对象当中
     * @param treeModel
     * @return
     */
    public OrganIdModel convert(SysOrganTreeModel treeModel) {
        this.key = treeModel.getId();
        this.value = treeModel.getId();
        this.title = treeModel.getOrgName();
        return this;
    }
    
    /**
     * 该方法为用户部门的实现类所使用
     * @param sysOrgan
     * @return
     */
    public OrganIdModel convertByUserOrgan(SysOrgan sysOrgan) {
        this.key = sysOrgan.getId();
        this.value = sysOrgan.getId();
        this.title = sysOrgan.getOrgName();
        return this;
    } 

    public List<OrganIdModel> getChildren() {
        return children;
    }

    public void setChildren(List<OrganIdModel> children) {
        this.children = children;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
