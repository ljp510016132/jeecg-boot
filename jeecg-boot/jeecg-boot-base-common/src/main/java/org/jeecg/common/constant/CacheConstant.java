package org.jeecg.common.constant;

/**
 * @author: huangxutao
 * @date: 2019-06-14
 * @description: 缓存常量
 */
public interface CacheConstant {

	/**
	 * 字典信息缓存
	 */
    public static final String SYS_DICT_CACHE = "sys:cache:dict";
	/**
	 * 表字典信息缓存
	 */
    public static final String SYS_DICT_TABLE_CACHE = "sys:cache:dictTable";

	/**
	 * 数据权限配置缓存
	 */
    public static final String SYS_DATA_PERMISSIONS_CACHE = "sys:cache:permission:datarules";

	/**
	 * 缓存用户信息
	 */
	public static final String SYS_USERS_CACHE = "sys:cache:user";

	/**
	 * 全部组织机构信息缓存
	 */
	public static final String SYS_ORGS_CACHE = "sys:cache:org:alldata";

	/**
	 * 全部行政机构信息缓存
	 */
	public static final String SYS_DEPARTS_CACHE = "sys:cache:depart:alldata";

	/**
	 * 全部组织机构ids缓存
	 */
	public static final String SYS_ORG_IDS_CACHE = "sys:cache:org:allids";

	/**
	 * 全部行政机构ids缓存
	 */
	public static final String SYS_DEPART_IDS_CACHE = "sys:cache:depart:allids";

	/**
	 * 测试缓存key
	 */
	public static final String TEST_DEMO_CACHE = "test:demo";

}
