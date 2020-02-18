package org.jeecg.modules.system.util;

import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysOrg;
import org.jeecg.modules.system.model.OrgIdModel;
import org.jeecg.modules.system.model.SysOrgTreeModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <P>
 * 对应部门的表,处理并查找树级数据
 * <P>
 * 
 * @Author: Steve
 * @Date: 2019-01-22
 */
public class FindsOrgsChildrenUtil {

	//部门树信息-树结构
	//private static List<SysOrgTreeModel> sysOrgTreeList = new ArrayList<SysOrgTreeModel>();
	
	//部门树id-树结构
    //private static List<OrgIdModel> idList = new ArrayList<>();


    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysOrg类型的list集合转换成SysOrgTreeModel类型的集合
     */
    public static List<SysOrgTreeModel> wrapTreeDataToTreeList(List<SysOrg> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
    	List<OrgIdModel> idList = new ArrayList<OrgIdModel>();
        List<SysOrgTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysOrg org = recordList.get(i);
            records.add(new SysOrgTreeModel(org));
        }
        List<SysOrgTreeModel> tree = findChildren(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysOrg类型的list集合转换成SysOrgTreeModel类型的集合
     */
    public static List<SysOrgTreeModel> wrapTreeDataToTreeList2(List<SysOrg> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<OrgIdModel> idList = new ArrayList<OrgIdModel>();
        List<SysOrgTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysOrg org = recordList.get(i);
            records.add(new SysOrgTreeModel(org));
        }
        List<SysOrgTreeModel> tree = findChildren2(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * 获取 OrgIdModel
     * @param recordList
     * @return
     */
    public static List<OrgIdModel> wrapTreeDataToOrgIdTreeList(List<SysOrg> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<OrgIdModel> idList = new ArrayList<OrgIdModel>();
        List<SysOrgTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysOrg org = recordList.get(i);
            records.add(new SysOrgTreeModel(org));
        }
        findChildren(records, idList);
        return idList;
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     */
    private static List<SysOrgTreeModel> findChildren(List<SysOrgTreeModel> recordList,
                                                         List<OrgIdModel> orgIdList) {

        List<SysOrgTreeModel> treeList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysOrgTreeModel branch = recordList.get(i);
            if (oConvertUtils.isEmpty(branch.getParentId())) {
                treeList.add(branch);
                OrgIdModel orgIdModel = new OrgIdModel().convert(branch);
                orgIdList.add(orgIdModel);
            }
        }
        getGrandChildren(treeList,recordList,orgIdList);
        
        //idList = orgIdList;
        return treeList;
    }


    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     * 顶级节点不一定是根节点情况
     */
    private static List<SysOrgTreeModel> findChildren2(List<SysOrgTreeModel> recordList,
                                                      List<OrgIdModel> orgIdList) {
        List<SysOrgTreeModel> treeList = new ArrayList<>();

        HashMap<String,SysOrgTreeModel> cacheOrgMap=new HashMap<>(recordList.size());
        for (SysOrgTreeModel org:recordList) {
            cacheOrgMap.put(org.getId(),org);
        }
        for (SysOrgTreeModel org:recordList) {
            if(cacheOrgMap.get(org.getParentId())==null){
                SysOrgTreeModel branch = org;
                treeList.add(branch);
                OrgIdModel orgIdModel = new OrgIdModel().convert(branch);
                orgIdList.add(orgIdModel);
            }
        }

        getGrandChildren(treeList,recordList,orgIdList);

        //idList = orgIdList;
        return treeList;
    }

    /**
     * queryTreeList的子方法====3====
     *该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合
     */
    private static void getGrandChildren(List<SysOrgTreeModel> treeList,List<SysOrgTreeModel> recordList,List<OrgIdModel> idList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysOrgTreeModel model = treeList.get(i);
            OrgIdModel idModel = idList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                SysOrgTreeModel m = recordList.get(i1);
                if (m.getParentId()!=null && m.getParentId().equals(model.getId())) {
                    model.getChildren().add(m);
                    OrgIdModel dim = new OrgIdModel().convert(m);
                    idModel.getChildren().add(dim);
                }
            }
            getGrandChildren(treeList.get(i).getChildren(), recordList, idList.get(i).getChildren());
        }

    }
    

    /**
     * queryTreeList的子方法 ====4====
     * 该方法是将子节点为空的List集合设置为Null值
     */
    private static void setEmptyChildrenAsNull(List<SysOrgTreeModel> treeList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysOrgTreeModel model = treeList.get(i);
            if (model.getChildren().size() == 0) {
                model.setChildren(null);
                model.setIsLeaf(true);
            }else{
                setEmptyChildrenAsNull(model.getChildren());
                model.setIsLeaf(false);
            }
        }
        // sysOrgTreeList = treeList;
    }
}
