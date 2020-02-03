package org.jeecg.modules.system.model;

import org.jeecg.modules.system.entity.SysOrg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 * 
 * @Author Steve
 * @Since 2019-01-22 
 */
public class SysOrgTreeModel implements Serializable{
	
    private static final long serialVersionUID = 1L;
    
    /** 对应SysOrg中的id字段,前端数据树中的key*/
    private String key;

    /** 对应SysOrg中的id字段,前端数据树中的value*/
    private String value;

    /** 对应org_name字段,前端数据树中的title*/
    private String title;


    private boolean isLeaf;
    // 以下所有字段均与SysOrg相同
    
    private String id;

    private String parentId;

    private String orgName;

    private String orgNameEn;

    private String orgNameAbbr;

    private Integer orgOrder;

    private Object description;
    
    private String orgCategory;

    private String orgType;

    private String orgCode;

    private String mobile;

    private String fax;

    private String address;

    private String memo;

    private String status;

    private String delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private List<SysOrgTreeModel> children = new ArrayList<>();


    /**
     * 将SysOrg对象转换成SysOrgTreeModel对象
     * @param sysOrg
     */
	public SysOrgTreeModel(SysOrg sysOrg) {
		this.key = sysOrg.getId();
        this.value = sysOrg.getId();
        this.title = sysOrg.getOrgName();
        this.id = sysOrg.getId();
        this.parentId = sysOrg.getParentId();
        this.orgName = sysOrg.getOrgName();
        this.orgNameEn = sysOrg.getOrgNameEn();
        this.orgNameAbbr = sysOrg.getOrgNameAbbr();
        this.orgOrder = sysOrg.getOrgOrder();
        this.description = sysOrg.getDescription();
        this.orgCategory = sysOrg.getOrgCategory();
        this.orgType = sysOrg.getOrgType();
        this.orgCode = sysOrg.getOrgCode();
        this.mobile = sysOrg.getMobile();
        this.fax = sysOrg.getFax();
        this.address = sysOrg.getAddress();
        this.memo = sysOrg.getMemo();
        this.status = sysOrg.getStatus();
        this.delFlag = sysOrg.getDelFlag();
        this.createBy = sysOrg.getCreateBy();
        this.createTime = sysOrg.getCreateTime();
        this.updateBy = sysOrg.getUpdateBy();
        this.updateTime = sysOrg.getUpdateTime();
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isleaf) {
         this.isLeaf = isleaf;
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


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SysOrgTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<SysOrgTreeModel> children) {
        if (children==null){
            this.isLeaf=true;
        }
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    
    public String getOrgCategory() {
		return orgCategory;
	}

	public void setOrgCategory(String orgCategory) {
		this.orgCategory = orgCategory;
	}

	public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOrgNameEn() {
        return orgNameEn;
    }

    public void setOrgNameEn(String orgNameEn) {
        this.orgNameEn = orgNameEn;
    }

    public String getOrgNameAbbr() {
        return orgNameAbbr;
    }

    public void setOrgNameAbbr(String orgNameAbbr) {
        this.orgNameAbbr = orgNameAbbr;
    }

    public Integer getOrgOrder() {
        return orgOrder;
    }

    public void setOrgOrder(Integer orgOrder) {
        this.orgOrder = orgOrder;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public SysOrgTreeModel() { }

    /**
     * 重写equals方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        SysOrgTreeModel model = (SysOrgTreeModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(parentId, model.parentId) &&
                Objects.equals(orgName, model.orgName) &&
                Objects.equals(orgNameEn, model.orgNameEn) &&
                Objects.equals(orgNameAbbr, model.orgNameAbbr) &&
                Objects.equals(orgOrder, model.orgOrder) &&
                Objects.equals(description, model.description) &&
                Objects.equals(orgCategory, model.orgCategory) &&
                Objects.equals(orgType, model.orgType) &&
                Objects.equals(orgCode, model.orgCode) &&
                Objects.equals(mobile, model.mobile) &&
                Objects.equals(fax, model.fax) &&
                Objects.equals(address, model.address) &&
                Objects.equals(memo, model.memo) &&
                Objects.equals(status, model.status) &&
                Objects.equals(delFlag, model.delFlag) &&
                Objects.equals(createBy, model.createBy) &&
                Objects.equals(createTime, model.createTime) &&
                Objects.equals(updateBy, model.updateBy) &&
                Objects.equals(updateTime, model.updateTime) &&
                Objects.equals(children, model.children);
    }
    
    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(id, parentId, orgName, orgNameEn, orgNameAbbr,
        		orgOrder, description, orgCategory, orgType, orgCode, mobile, fax, address,
        		memo, status, delFlag, createBy, createTime, updateBy, updateTime, 
        		children);
    }

}
