package org.jeecg.modules.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.entity.SysOrg;
import org.jeecg.modules.system.entity.SysPlatformOrg;
import org.jeecg.modules.system.mapper.SysPlatformOrgMapper;
import org.jeecg.modules.system.model.OrgIdModel;
import org.jeecg.modules.system.service.ISysOrgService;
import org.jeecg.modules.system.service.ISysPlatformOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 平台部门表实现类
 * <p/>
 *
 * @Author ZhiLin
 * @since 2019-02-22
 */
@Service
public class SysPlatformOrgServiceImpl extends ServiceImpl<SysPlatformOrgMapper, SysPlatformOrg> implements ISysPlatformOrgService {
    @Autowired
    private ISysOrgService sysOrgService;

    /**
     * 根据平台id查询部门信息
     */
    @Override
    public List<OrgIdModel> queryOrgIdsOfPlatform(String platformId) {
        LambdaQueryWrapper<SysPlatformOrg> queryPOrg = new LambdaQueryWrapper<SysPlatformOrg>();
        LambdaQueryWrapper<SysOrg> queryOrg = new LambdaQueryWrapper<SysOrg>();
        try {
            queryPOrg.eq(SysPlatformOrg::getPlatformId, platformId);
            List<String> orgIdList = new ArrayList<>();
            List<OrgIdModel> orgIdModelList = new ArrayList<>();
            List<SysPlatformOrg> userOrgList = this.list(queryPOrg);
            if (userOrgList != null && userOrgList.size() > 0) {
                for (SysPlatformOrg userOrg : userOrgList) {
                    orgIdList.add(userOrg.getOrgId());
                }
                queryOrg.in(SysOrg::getId, orgIdList);
                List<SysOrg> orgList = sysOrgService.list(queryOrg);
                if (orgList != null || orgList.size() > 0) {
                    for (SysOrg org : orgList) {
                        orgIdModelList.add(new OrgIdModel().convertByUserOrg(org));
                    }
                }
                return orgIdModelList;
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return null;


    }

}
