package org.jeecg.modules.system.util;

import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysOrgan;
import org.jeecg.modules.system.model.OrganIdModel;
import org.jeecg.modules.system.model.SysOrganTreeModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <P>
 * 对应部门的表,处理并查找树级数据
 * <P>
 * 
 * @Author: Steve
 * @Date: 2019-01-22
 */
public class FindsOrgansChildrenUtil {

	//部门树信息-树结构
	//private static List<SysOrganTreeModel> sysOrganTreeList = new ArrayList<SysOrganTreeModel>();
	
	//部门树id-树结构
    //private static List<OrganIdModel> idList = new ArrayList<>();


    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysOrgan类型的list集合转换成SysOrganTreeModel类型的集合
     */
    public static List<SysOrganTreeModel> wrapTreeDataToTreeList(List<SysOrgan> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
    	List<OrganIdModel> idList = new ArrayList<OrganIdModel>();
        List<SysOrganTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysOrgan organ = recordList.get(i);
            records.add(new SysOrganTreeModel(organ));
        }
        List<SysOrganTreeModel> tree = findChildren(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * 获取 OrganIdModel
     * @param recordList
     * @return
     */
    public static List<OrganIdModel> wrapTreeDataToOrganIdTreeList(List<SysOrgan> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<OrganIdModel> idList = new ArrayList<OrganIdModel>();
        List<SysOrganTreeModel> records = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysOrgan organ = recordList.get(i);
            records.add(new SysOrganTreeModel(organ));
        }
        findChildren(records, idList);
        return idList;
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     */
    private static List<SysOrganTreeModel> findChildren(List<SysOrganTreeModel> recordList,
                                                         List<OrganIdModel> organIdList) {

        List<SysOrganTreeModel> treeList = new ArrayList<>();
        for (int i = 0; i < recordList.size(); i++) {
            SysOrganTreeModel branch = recordList.get(i);
            if (oConvertUtils.isEmpty(branch.getParentId())) {
                treeList.add(branch);
                OrganIdModel organIdModel = new OrganIdModel().convert(branch);
                organIdList.add(organIdModel);
            }
        }
        getGrandChildren(treeList,recordList,organIdList);
        
        //idList = organIdList;
        return treeList;
    }

    /**
     * queryTreeList的子方法====3====
     *该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合
     */
    private static void getGrandChildren(List<SysOrganTreeModel> treeList,List<SysOrganTreeModel> recordList,List<OrganIdModel> idList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysOrganTreeModel model = treeList.get(i);
            OrganIdModel idModel = idList.get(i);
            for (int i1 = 0; i1 < recordList.size(); i1++) {
                SysOrganTreeModel m = recordList.get(i1);
                if (m.getParentId()!=null && m.getParentId().equals(model.getId())) {
                    model.getChildren().add(m);
                    OrganIdModel dim = new OrganIdModel().convert(m);
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
    private static void setEmptyChildrenAsNull(List<SysOrganTreeModel> treeList) {

        for (int i = 0; i < treeList.size(); i++) {
            SysOrganTreeModel model = treeList.get(i);
            if (model.getChildren().size() == 0) {
                model.setChildren(null);
                model.setIsLeaf(true);
            }else{
                setEmptyChildrenAsNull(model.getChildren());
                model.setIsLeaf(false);
            }
        }
        // sysOrganTreeList = treeList;
    }
}
