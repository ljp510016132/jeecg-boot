package org.jeecg.common.system.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 在线用户信息
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LoginUser {
	/**
	 * 用户类型：0超级管理员、1租户超级管理员、2、租户管理员、3普通用户
	 */
	private Integer type;

	/**
	 * 平台code
	 */
	private String platformCode;

	/**
	 * 登录人id
	 */
	private String id;

	/**
	 * 登录人账号
	 */
	private String username;

	/**
	 * 登录人名字
	 */
	private String realname;

	/**
	 * 登录人密码
	 */
	private String password;

     /**
      * 当前登录组织机构code
      */
    private String orgCode;

	/**
	 * 当前登录行政机构code
	 */
	private String departCode;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 生日
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	/**
	 * 性别（1：男 2：女）
	 */
	private Integer sex;

	/**
	 * 电子邮件
	 */
	private String email;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 状态(1：正常 2：冻结 ）
	 */
	private Integer status;
	
	private String delFlag;
	/**
     * 同步工作流引擎1同步0不同步
     */
    private String activitiSync;

	/**
	 * 创建时间
	 */
	private Date createTime;

}
