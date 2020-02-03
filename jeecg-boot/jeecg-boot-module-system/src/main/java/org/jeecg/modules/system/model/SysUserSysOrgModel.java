package org.jeecg.modules.system.model;

import lombok.Data;
import org.jeecg.modules.system.entity.SysOrg;
import org.jeecg.modules.system.entity.SysUser;

/**
 * 包含 SysUser 和 SysOrg 的 Model
 *
 * @author sunjianlei
 */
@Data
public class SysUserSysOrgModel {

    private SysUser sysUser;
    private SysOrg sysOrg;

}
