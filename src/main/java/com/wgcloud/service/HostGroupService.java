/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.HostGroupMapper;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HostGroupService {
    private static final Logger logger = LoggerFactory.getLogger(HostGroupService.class);
    @Autowired
    private HostGroupMapper hostGroupMapper;
    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private CommonConfig commonConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<HostGroup> list = this.hostGroupMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(HostGroup HostGroup2) throws Exception {
        HostGroup2.setId(UUIDUtil.getUUID());
        HostGroup2.setCreateTime(new Date());
        this.hostGroupMapper.save(HostGroup2);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.hostGroupMapper.deleteById(id);
    }

    public void updateById(HostGroup HostGroup2) throws Exception {
        this.hostGroupMapper.updateById(HostGroup2);
    }

    public HostGroup selectById(String id) throws Exception {
        return this.hostGroupMapper.selectById(id);
    }

    public List<HostGroup> selectAllByParams(Map<String, Object> params, HttpServletRequest request) throws Exception {
        AccountInfo accountInfo;
        if (null != request && "user".equals((accountInfo = HostUtil.getAccountByRequest(request)).getRole())) {
            params.put("account", accountInfo.getAccount());
        }
        return this.hostGroupMapper.selectAllByParams(params);
    }

    public String returnGroupNames(String hostGroupIds) {
        String names = "";
        if (StringUtils.isEmpty((CharSequence)hostGroupIds) || !"true".equals(this.commonConfig.getHostGroup())) {
            return "";
        }
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupIds", hostGroupIds.split(","));
            List<HostGroup> hostGroupList = this.selectAllByParams(params, null);
            for (int i = 0; i < hostGroupList.size(); ++i) {
                names = names + hostGroupList.get(i).getGroupName();
                if (i == hostGroupList.size() - 1) continue;
                names = names + ",";
            }
        }
        catch (Exception e) {
            logger.error("\u6807\u7b7eID\u8f6c\u6362\u9519\u8bef", (Throwable)e);
        }
        return names;
    }

    public String returnGroupNames(List<HostGroup> hostGroupList) {
        if (CollectionUtil.isEmpty(hostGroupList)) {
            return "";
        }
        String names = "";
        try {
            for (int i = 0; i < hostGroupList.size(); ++i) {
                names = names + hostGroupList.get(i).getGroupName();
                if (i == hostGroupList.size() - 1) continue;
                names = names + ",";
            }
        }
        catch (Exception e) {
            logger.error("\u6807\u7b7eID\u8f6c\u6362\u9519\u8bef", (Throwable)e);
        }
        return names;
    }

    public void invokeGroupIdToNames(List list) {
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<HostGroup> hostGroupList = this.selectAllByParams(params, null);
            HashMap<String, String> hostGroupMap = new HashMap<String, String>();
            for (HostGroup hostGroup : hostGroupList) {
                hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
            }
            for (HostGroup obj : (java.util.List<HostGroup>)list) {
                Class<?> superClazz = obj.getClass().getSuperclass();
                Method getGroupIdMethod = superClazz.getDeclaredMethod("getGroupId", new Class[0]);
                String groupIdValue = (String)getGroupIdMethod.invoke(obj, new Object[0]);
                if (StringUtils.isEmpty((CharSequence)groupIdValue)) continue;
                String groupNames = "";
                for (String groupId : groupIdValue.split(",")) {
                    if (!hostGroupMap.containsKey(groupId)) continue;
                    groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
                }
                if (groupNames.endsWith(",")) {
                    groupNames = groupNames.substring(0, groupNames.length() - 1);
                }
                Method setGroupIdMethod = superClazz.getDeclaredMethod("setGroupId", String.class);
                setGroupIdMethod.invoke(obj, groupNames);
            }
        }
        catch (Exception e) {
            logger.error("\u901a\u7528\u6807\u7b7e\u540d\u79f0\u8f6c\u6362\u9519\u8bef", (Throwable)e);
        }
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.hostGroupMapper.countByParams(params);
    }

    public void saveLog(HttpServletRequest request, String action, HostGroup hostGroup) {
        if (null == hostGroup) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u6807\u7b7e\u4fe1\u606f\uff1a" + hostGroup.getGroupName(), "\u8d44\u6e90\u6807\u7b7e\uff1a" + hostGroup.getRemark(), "2");
    }
}

