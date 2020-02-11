package org.jeecg.modules.system.rule;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.netty.util.internal.StringUtil;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author scott
 * @Date 2019/12/9 11:33
 * @Description: 行政机构编码生成规则：行政机构与组织机构区别，行政机构编码多一个D前缀
 */
public class DepartCodeRule implements IFillRuleHandler {
    //前缀，行政机构比组织机构编码多一个前缀
    private String preStr="D";

    @Override
    public Object execute(JSONObject params, JSONObject formData) {
        ISysDepartService sysDepartService = (ISysDepartService) SpringContextUtils.getBean("sysDepartServiceImpl");

        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        LambdaQueryWrapper<SysDepart> query1 = new LambdaQueryWrapper<SysDepart>();
        // 创建一个List集合,存储查询返回的所有SysDepart对象
        List<SysDepart> departList = new ArrayList<>();
        String[] strArray = new String[2];
        //定义部门类型
        String departType = "";
        // 定义新编码字符串
        String newDepartCode = "";
        // 定义旧编码字符串
        String oldDepartCode = "";

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

        //如果是最高级,则查询出同级的depart_code, 调用工具类生成编码并返回
        if (StringUtil.isNullOrEmpty(parentId)) {
            // 线判断数据库中的表是否为空,空则直接返回初始编码
            query1.eq(SysDepart::getParentId, "").or().isNull(SysDepart::getParentId);
            query1.orderByDesc(SysDepart::getDepartCode);
            departList = sysDepartService.list(query1);
            if (departList == null || departList.size() == 0) {
                //depart的编码以
                strArray[0] = preStr+YouBianCodeUtil.getNextYouBianCode(null);
                strArray[1] = "1";
                return strArray;
            } else {
                SysDepart depart = departList.get(0);
                oldDepartCode = depart.getDepartCode();
                departType = depart.getDepartType();
                newDepartCode = YouBianCodeUtil.getNextYouBianCode(subPreDepartCode(oldDepartCode));
            }
        } else {//反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
            // 封装查询同级的条件
            query.eq(SysDepart::getParentId, parentId);
            // 降序排序
            query.orderByDesc(SysDepart::getDepartCode);
            // 查询出同级部门的集合
            List<SysDepart> parentList = sysDepartService.list(query);
            // 查询出父级部门
            SysDepart depart = sysDepartService.getById(parentId);
            // 获取父级部门的Code
            String parentCode = depart.getDepartCode();
            // 根据父级部门类型算出当前部门的类型
            departType = String.valueOf(Integer.valueOf(depart.getDepartType()) + 1);
            // 处理同级部门为null的情况
            if (parentList == null || parentList.size() == 0) {
                // 直接生成当前的部门编码并返回
                newDepartCode = YouBianCodeUtil.getSubYouBianCode(subPreDepartCode(parentCode), null);
            } else { //处理有同级部门的情况
                // 获取同级部门的编码,利用工具类
                String subCode = parentList.get(0).getDepartCode();
                // 返回生成的当前部门编码
                newDepartCode = YouBianCodeUtil.getSubYouBianCode(subPreDepartCode(parentCode), subPreDepartCode(subCode));
            }
        }
        // 返回最终封装了部门编码和部门类型的数组
        strArray[0] = preStr + newDepartCode;
        strArray[1] = departType;
        return strArray;
    }

    /**
     * 去掉机构编号的前缀，去掉前缀后，行政机构将于组织机构编号一直
     * @param departCode
     * @return
     */
    private String subPreDepartCode(String departCode){
        //卸载前缀
        if(departCode!=null&&!departCode.equals("")&&departCode.startsWith(preStr)){
            departCode=departCode.substring(1,departCode.length());
        }
        return departCode;
    }
}
