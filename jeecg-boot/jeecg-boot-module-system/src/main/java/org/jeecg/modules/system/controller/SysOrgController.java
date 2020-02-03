package org.jeecg.modules.system.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysOrg;
import org.jeecg.modules.system.model.OrgIdModel;
import org.jeecg.modules.system.model.SysOrgTreeModel;
import org.jeecg.modules.system.service.ISysOrgService;
import org.jeecg.modules.system.util.FindsOrgsChildrenUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 部门表 前端控制器
 * <p>
 * 
 * @Author: Steve @Since： 2019-01-22
 */
@RestController
@RequestMapping("/sys/sysOrg")
@Slf4j
public class SysOrgController {

	@Autowired
	private ISysOrgService sysOrgService;

	/**
	 * 查询数据 查出所有部门,并以树结构数据格式响应给前端
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
	public Result<List<SysOrgTreeModel>> queryTreeList() {
		Result<List<SysOrgTreeModel>> result = new Result<>();
		try {
			// 从内存中读取
//			List<SysOrgTreeModel> list =FindsOrgsChildrenUtil.getSysOrgTreeList();
//			if (CollectionUtils.isEmpty(list)) {
//				list = sysOrgService.queryTreeList();
//			}
			List<SysOrgTreeModel> list = sysOrgService.queryTreeList();
			result.setResult(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}

	/**
	 * 添加新数据 添加用户新建的部门对象数据,并保存到数据库
	 * 
	 * @param sysOrg
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
	public Result<SysOrg> add(@RequestBody SysOrg sysOrg, HttpServletRequest request) {
		Result<SysOrg> result = new Result<SysOrg>();
		String username = JwtUtil.getUserNameByToken(request);
		try {
			sysOrg.setCreateBy(username);
			sysOrgService.saveOrgData(sysOrg, username);
			//清除部门树内存
			// FindsOrgsChildrenUtil.clearSysOrgTreeList();
			// FindsOrgsChildrenUtil.clearOrgIdModel();
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 编辑数据 编辑部门的部分数据,并保存到数据库
	 * 
	 * @param sysOrg
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
	public Result<SysOrg> edit(@RequestBody SysOrg sysOrg, HttpServletRequest request) {
		String username = JwtUtil.getUserNameByToken(request);
		sysOrg.setUpdateBy(username);
		Result<SysOrg> result = new Result<SysOrg>();
		SysOrg sysOrgEntity = sysOrgService.getById(sysOrg.getId());
		if (sysOrgEntity == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = sysOrgService.updateOrgDataById(sysOrg, username);
			// TODO 返回false说明什么？
			if (ok) {
				//清除部门树内存
				//FindsOrgsChildrenUtil.clearSysOrgTreeList();
				//FindsOrgsChildrenUtil.clearOrgIdModel();
				result.success("修改成功!");
			}
		}
		return result;
	}
	
	 /**
     *   通过id删除
    * @param id
    * @return
    */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
   public Result<SysOrg> delete(@RequestParam(name="id",required=true) String id) {

       Result<SysOrg> result = new Result<SysOrg>();
       SysOrg sysOrg = sysOrgService.getById(id);
       if(sysOrg==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = sysOrgService.delete(id);
           if(ok) {
	            //清除部门树内存
	   		   //FindsOrgsChildrenUtil.clearSysOrgTreeList();
	   		   // FindsOrgsChildrenUtil.clearOrgIdModel();
               result.success("删除成功!");
           }
       }
       return result;
   }


	/**
	 * 批量删除 根据前端请求的多个ID,对数据库执行删除相关部门数据的操作
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
	public Result<SysOrg> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

		Result<SysOrg> result = new Result<SysOrg>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		} else {
			this.sysOrgService.deleteBatchWithChildren(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * 查询数据 添加或编辑页面对该方法发起请求,以树结构形式加载所有部门的名称,方便用户的操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryIdTree", method = RequestMethod.GET)
	public Result<List<OrgIdModel>> queryIdTree() {
//		Result<List<OrgIdModel>> result = new Result<List<OrgIdModel>>();
//		List<OrgIdModel> idList;
//		try {
//			idList = FindsOrgsChildrenUtil.wrapOrgIdModel();
//			if (idList != null && idList.size() > 0) {
//				result.setResult(idList);
//				result.setSuccess(true);
//			} else {
//				sysOrgService.queryTreeList();
//				idList = FindsOrgsChildrenUtil.wrapOrgIdModel();
//				result.setResult(idList);
//				result.setSuccess(true);
//			}
//			return result;
//		} catch (Exception e) {
//			log.error(e.getMessage(),e);
//			result.setSuccess(false);
//			return result;
//		}
		Result<List<OrgIdModel>> result = new Result<>();
		try {
			List<OrgIdModel> list = sysOrgService.queryOrgIdTreeList();
			result.setResult(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}
	 
	/**
	 * <p>
	 * 部门搜索功能方法,根据关键字模糊搜索相关部门
	 * </p>
	 * 
	 * @param keyWord
	 * @return
	 */
	@RequestMapping(value = "/searchBy", method = RequestMethod.GET)
	public Result<List<SysOrgTreeModel>> searchBy(@RequestParam(name = "keyWord", required = true) String keyWord) {
		Result<List<SysOrgTreeModel>> result = new Result<List<SysOrgTreeModel>>();
		try {
			List<SysOrgTreeModel> treeList = this.sysOrgService.searhBy(keyWord);
			if (treeList.size() == 0 || treeList == null) {
				throw new Exception();
			}
			result.setSuccess(true);
			result.setResult(treeList);
			return result;
		} catch (Exception e) {
			e.fillInStackTrace();
			result.setSuccess(false);
			result.setMessage("查询失败或没有您想要的任何数据!");
			return result;
		}
	}


	/**
     * 导出excel
     *
     * @param request
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysOrg sysOrg,HttpServletRequest request) {
        // Step.1 组装查询条件
        QueryWrapper<SysOrg> queryWrapper = QueryGenerator.initQueryWrapper(sysOrg, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysOrg> pageList = sysOrgService.list(queryWrapper);
        //按字典排序
        Collections.sort(pageList, new Comparator<SysOrg>() {
            @Override
			public int compare(SysOrg arg0, SysOrg arg1) {
            	return arg0.getOrgCode().compareTo(arg1.getOrgCode());
            }
        });
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "部门列表");
        mv.addObject(NormalExcelConstants.CLASS, SysOrg.class);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("部门列表数据", "导出人:"+user.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
            	// orgCode编码长度
            	int codeLength = 3;
                List<SysOrg> listSysOrgs = ExcelImportUtil.importExcel(file.getInputStream(), SysOrg.class, params);
                //按长度排序
                Collections.sort(listSysOrgs, new Comparator<SysOrg>() {
                    @Override
					public int compare(SysOrg arg0, SysOrg arg1) {
                    	return arg0.getOrgCode().length() - arg1.getOrgCode().length();
                    }
                });
                for (SysOrg sysOrg : listSysOrgs) {
                	String orgCode = sysOrg.getOrgCode();
                	if(orgCode.length() > codeLength) {
                		String parentCode = orgCode.substring(0, orgCode.length()-codeLength);
                		QueryWrapper<SysOrg> queryWrapper = new QueryWrapper<SysOrg>();
                		queryWrapper.eq("org_code", parentCode);
                		try {
                		SysOrg parentDept = sysOrgService.getOne(queryWrapper);
                		if(!parentDept.equals(null)) {
							sysOrg.setParentId(parentDept.getId());
						} else {
							sysOrg.setParentId("");
						}
                		}catch (Exception e) {
                			//没有查找到parentDept
                		}
                	}else{
                		sysOrg.setParentId("");
					}
                    sysOrgService.save(sysOrg);
                }
                return Result.ok("文件导入成功！数据行数：" + listSysOrgs.size());
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return Result.error("文件导入失败:"+e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }
}
