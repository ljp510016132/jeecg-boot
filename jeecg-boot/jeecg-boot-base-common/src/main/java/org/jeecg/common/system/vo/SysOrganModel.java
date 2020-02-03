package org.jeecg.common.system.vo;

/**
 * lvdandan 部门机构model
 */
public class SysOrganModel {
    /**ID*/
    private String id;
    /**父机构ID*/
    private String parentId;
    /**机构/部门名称*/
    private String organName;
    /**英文名*/
    private String organNameEn;
    /**缩写*/
    private String organNameAbbr;
    /**排序*/
    private Integer organOrder;
    /**描述*/
    private Object description;
    /**机构类别 1组织机构，2岗位*/
    private String orgCategory;
    /**机构类型*/
    private String orgType;
    /**机构编码*/
    private String orgCode;
    /**手机号*/
    private String mobile;
    /**传真*/
    private String fax;
    /**地址*/
    private String address;
    /**备注*/
    private String memo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrgName() {
        return organName;
    }

    public void setOrgName(String organName) {
        this.organName = organName;
    }

    public String getOrgNameEn() {
        return organNameEn;
    }

    public void setOrgNameEn(String organNameEn) {
        this.organNameEn = organNameEn;
    }

    public String getOrgNameAbbr() {
        return organNameAbbr;
    }

    public void setOrgNameAbbr(String organNameAbbr) {
        this.organNameAbbr = organNameAbbr;
    }

    public Integer getOrgOrder() {
        return organOrder;
    }

    public void setOrgOrder(Integer organOrder) {
        this.organOrder = organOrder;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
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
}
