package org.jeecg.modules.system.model;

import lombok.Data;
import org.jeecg.modules.system.entity.SysOrgan;
import org.jeecg.modules.system.entity.SysUser;

/**
 * 包含 SysUser 和 SysOrgan 的 Model
 *
 * @author sunjianlei
 */
@Data
public class SysUserSysOrganModel {

    private SysUser sysUser;
    private SysOrgan sysOrgan;

}
