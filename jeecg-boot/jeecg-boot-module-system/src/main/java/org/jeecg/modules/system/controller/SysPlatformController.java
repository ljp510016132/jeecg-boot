package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysOrg;
import org.jeecg.modules.system.entity.SysPlatform;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.OrgIdModel;
import org.jeecg.modules.system.service.ISysPlatformOrgService;
import org.jeecg.modules.system.service.ISysPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 平台表
 * @Author: jeecg-boot
 * @Date: 2020-02-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "平台表")
@RestController
@RequestMapping("/sys/platform")
public class SysPlatformController extends JeecgController<SysPlatform, ISysPlatformService> {
    @Autowired
    private ISysPlatformService sysPlatformService;

    @Autowired
    private ISysPlatformOrgService sysPlatformOrgService;
    /**
     * 分页列表查询
     *
     * @param sysPlatform
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "平台表-分页列表查询")
    @ApiOperation(value = "平台表-分页列表查询", notes = "平台表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysPlatform sysPlatform,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SysPlatform> queryWrapper = QueryGenerator.initQueryWrapper(sysPlatform, req.getParameterMap());
        Page<SysPlatform> page = new Page<SysPlatform>(pageNo, pageSize);
        IPage<SysPlatform> pageList = sysPlatformService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param sysPlatform
     * @return
     */
    @AutoLog(value = "平台表-添加")
    @ApiOperation(value = "平台表-添加", notes = "平台表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysPlatform sysPlatform) {
        sysPlatformService.save(sysPlatform);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysPlatform
     * @return
     */
    @AutoLog(value = "平台表-编辑")
    @ApiOperation(value = "平台表-编辑", notes = "平台表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysPlatform sysPlatform) {
        sysPlatformService.updateById(sysPlatform);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "平台表-通过id删除")
    @ApiOperation(value = "平台表-通过id删除", notes = "平台表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysPlatformService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "平台表-批量删除")
    @ApiOperation(value = "平台表-批量删除", notes = "平台表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysPlatformService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "平台表-通过id查询")
    @ApiOperation(value = "平台表-通过id查询", notes = "平台表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysPlatform sysPlatform = sysPlatformService.getById(id);
        return Result.ok(sysPlatform);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysPlatform
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysPlatform sysPlatform) {
        return super.exportXls(request, sysPlatform, SysPlatform.class, "平台表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysPlatform.class);
    }

    @AutoLog(value = "平台机构编辑")
    @ApiOperation(value = "平台机构编辑", notes = "平台机构编辑")
    @PostMapping(value = "/editPlatformOrgs")
    public Result<?> editPlatformOrgs(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
        String platformId = jsonObject.getString("platformId");
        String selectedOrgs = jsonObject.getString("orgs");
        try {
            sysPlatformService.editPlatformOrgs(platformId, selectedOrgs);
            result.success("编辑成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }
    /**
     * 查询平台关联的机构数据
     *
     * @param platformId
     * @return
     */
    @RequestMapping(value = "/platformOrgList", method = RequestMethod.GET)
    public Result<List<OrgIdModel>> getPlatformOrgList(@RequestParam(name = "platformId", required = true) String platformId) {
        Result<List<OrgIdModel>> result = new Result<>();
        try {
            List<OrgIdModel> orgIdModelList = this.sysPlatformOrgService.queryOrgIdsOfPlatform(platformId);
            if (orgIdModelList != null && orgIdModelList.size() > 0) {
                result.setSuccess(true);
                result.setMessage("查找成功");
                result.setResult(orgIdModelList);
            } else {
                result.setSuccess(false);
                result.setMessage("查找失败");
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage("查找过程中出现了异常: " + e.getMessage());
            return result;
        }

    }
}
