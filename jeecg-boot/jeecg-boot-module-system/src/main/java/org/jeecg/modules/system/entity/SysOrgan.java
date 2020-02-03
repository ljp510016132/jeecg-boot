package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.system.model.SysOrganTreeModel;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 部门表
 * <p>
 * 
 * @Author Steve
 * @Since  2019-01-22
 */
@Data
@TableName("sys_organ")
public class SysOrgan implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**ID*/
	@TableId(type = IdType.ID_WORKER_STR)
	private String id;
	/**父机构ID*/
	private String parentId;
	/**机构/部门名称*/
	@Excel(name="机构/部门名称",width=15)
	private String orgName;
	/**英文名*/
	@Excel(name="英文名",width=15)
	private String orgNameEn;
	/**缩写*/
	private String orgNameAbbr;
	/**排序*/
	@Excel(name="排序",width=15)
	private Integer orgOrder;
	/**描述*/
	@Excel(name="描述",width=15)
	private Object description;
	/**机构类别 1组织机构，2岗位*/
	@Excel(name="机构类别",width=15)
	private String orgCategory;
	/**机构类型*/
	@Excel(name="机构类型",width=15)
	private String orgType;
	/**机构编码*/
	@Excel(name="机构编码",width=15)
	private String orgCode;
	/**手机号*/
	@Excel(name="手机号",width=15)
	private String mobile;
	/**传真*/
	@Excel(name="传真",width=15)
	private String fax;
	/**地址*/
	@Excel(name="地址",width=15)
	private String address;
	/**备注*/
	@Excel(name="备注",width=15)
	private String memo;
	/**状态（1启用，0不启用）*/
	@Excel(name="状态",width=15)
	@Dict(dicCode = "organ_status")
	private String status;
	/**删除状态（0，正常，1已删除）*/
	@Excel(name="删除状态",width=15)
	@Dict(dicCode = "del_flag")
	private String delFlag;
	/**创建人*/
	private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
	private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
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
        if (!super.equals(o)) {
			return false;
		}
        SysOrgan organ = (SysOrgan) o;
        return Objects.equals(id, organ.id) &&
                Objects.equals(parentId, organ.parentId) &&
                Objects.equals(orgName, organ.orgName) &&
                Objects.equals(orgNameEn, organ.orgNameEn) &&
                Objects.equals(orgNameAbbr, organ.orgNameAbbr) &&
                Objects.equals(orgOrder, organ.orgOrder) &&
                Objects.equals(description, organ.description) &&
                Objects.equals(orgCategory, organ.orgCategory) &&
                Objects.equals(orgType, organ.orgType) &&
                Objects.equals(orgCode, organ.orgCode) &&
                Objects.equals(mobile, organ.mobile) &&
                Objects.equals(fax, organ.fax) &&
                Objects.equals(address, organ.address) &&
                Objects.equals(memo, organ.memo) &&
                Objects.equals(status, organ.status) &&
                Objects.equals(delFlag, organ.delFlag) &&
                Objects.equals(createBy, organ.createBy) &&
                Objects.equals(createTime, organ.createTime) &&
                Objects.equals(updateBy, organ.updateBy) &&
                Objects.equals(updateTime, organ.updateTime);
    }

    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, parentId, orgName,
        		orgNameEn, orgNameAbbr, orgOrder, description,orgCategory,
        		orgType, orgCode, mobile, fax, address, memo, status, 
        		delFlag, createBy, createTime, updateBy, updateTime);
    }
}
