package org.jeecg.modules.system.rule;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.netty.util.internal.StringUtil;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.modules.system.entity.SysOrgan;
import org.jeecg.modules.system.service.ISysOrganService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author scott
 * @Date 2019/12/9 11:33
 * @Description: 机构编码生成规则
 */
public class OrgCodeRule implements IFillRuleHandler {

    @Override
    public Object execute(JSONObject params, JSONObject formData) {
        ISysOrganService sysOrganService = (ISysOrganService) SpringContextUtils.getBean("sysOrganServiceImpl");

        LambdaQueryWrapper<SysOrgan> query = new LambdaQueryWrapper<SysOrgan>();
        LambdaQueryWrapper<SysOrgan> query1 = new LambdaQueryWrapper<SysOrgan>();
        // 创建一个List集合,存储查询返回的所有SysOrgan对象
        List<SysOrgan> organList = new ArrayList<>();
        String[] strArray = new String[2];
        //定义部门类型
        String orgType = "";
        // 定义新编码字符串
        String newOrgCode = "";
        // 定义旧编码字符串
        String oldOrgCode = "";

        String parentId = null;
        if (formData != null && formData.size() > 0) {
            Object obj = formData.get("parentId");
            if (obj != null) parentId = obj.toString();
        } else {
            if (params != null) {
                Object obj = params.get("parentId");
                if (obj != null) parentId = obj.toString();
            }
        }

        //如果是最高级,则查询出同级的org_code, 调用工具类生成编码并返回
        if (StringUtil.isNullOrEmpty(parentId)) {
            // 线判断数据库中的表是否为空,空则直接返回初始编码
            query1.eq(SysOrgan::getParentId, "").or().isNull(SysOrgan::getParentId);
            query1.orderByDesc(SysOrgan::getOrgCode);
            organList = sysOrganService.list(query1);
            if (organList == null || organList.size() == 0) {
                strArray[0] = YouBianCodeUtil.getNextYouBianCode(null);
                strArray[1] = "1";
                return strArray;
            } else {
                SysOrgan organ = organList.get(0);
                oldOrgCode = organ.getOrgCode();
                orgType = organ.getOrgType();
                newOrgCode = YouBianCodeUtil.getNextYouBianCode(oldOrgCode);
            }
        } else {//反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
            // 封装查询同级的条件
            query.eq(SysOrgan::getParentId, parentId);
            // 降序排序
            query.orderByDesc(SysOrgan::getOrgCode);
            // 查询出同级部门的集合
            List<SysOrgan> parentList = sysOrganService.list(query);
            // 查询出父级部门
            SysOrgan organ = sysOrganService.getById(parentId);
            // 获取父级部门的Code
            String parentCode = organ.getOrgCode();
            // 根据父级部门类型算出当前部门的类型
            orgType = String.valueOf(Integer.valueOf(organ.getOrgType()) + 1);
            // 处理同级部门为null的情况
            if (parentList == null || parentList.size() == 0) {
                // 直接生成当前的部门编码并返回
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, null);
            } else { //处理有同级部门的情况
                // 获取同级部门的编码,利用工具类
                String subCode = parentList.get(0).getOrgCode();
                // 返回生成的当前部门编码
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, subCode);
            }
        }
        // 返回最终封装了部门编码和部门类型的数组
        strArray[0] = newOrgCode;
        strArray[1] = orgType;
        return strArray;
    }
}
