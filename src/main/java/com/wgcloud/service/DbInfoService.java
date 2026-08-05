/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.DbInfoMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class DbInfoService {
    private static final Logger logger = LoggerFactory.getLogger(DbInfoService.class);
    @Autowired
    private DbInfoMapper dbInfoMapper;
    @Autowired
    private LogInfoService logInfoService;
    @Resource
    private HostGroupService hostGroupService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<DbInfo> list = this.dbInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(DbInfo DbInfo2) throws Exception {
        DbInfo2.setId(UUIDUtil.getUUID());
        DbInfo2.setCreateTime(new Date());
        DbInfo2.setDbState("1");
        this.dbInfoMapper.save(DbInfo2);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.dbInfoMapper.countByParams(params);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.dbInfoMapper.updateActive(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.dbInfoMapper.deleteById(id);
    }

    public int updateById(DbInfo DbInfo2) throws Exception {
        return this.dbInfoMapper.updateById(DbInfo2);
    }

    public DbInfo selectById(String id) throws Exception {
        return this.dbInfoMapper.selectById(id);
    }

    public List<DbInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.dbInfoMapper.selectAllByParams(params);
    }

    public void saveLog(HttpServletRequest request, String action, DbInfo dbInfo) {
        if (null == dbInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u6570\u636e\u5e93\u76d1\u6d4b\u4fe1\u606f\uff1a" + dbInfo.getAliasName(), "\u6570\u636e\u5e93\u7c7b\u578b\uff1a" + dbInfo.getDbType(), "2");
    }

    public void addServerBackMark(List<DbInfo> list) throws Exception {
        for (DbInfo dbInfo : list) {
            if (ServerBackupUtil.isExistDbInfoId(dbInfo.getId())) {
                dbInfo.setServerBackupMark("1");
                continue;
            }
            dbInfo.setServerBackupMark("2");
        }
    }

    public void dbAddLogo(List<DbInfo> list) {
        try {
            for (DbInfo dbInfo : list) {
                if (!StringUtils.isEmpty((CharSequence)dbInfo.getDbType())) {
                    if ("mysql".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/mysql.png");
                        continue;
                    }
                    if ("mariadb".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/mariadb.png");
                        continue;
                    }
                    if ("postgresql".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/postgresql.png");
                        continue;
                    }
                    if ("sqlserver".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/sqlserverDb.png");
                        continue;
                    }
                    if ("db2".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/db2.png");
                        continue;
                    }
                    if ("redis".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/redisDb.png");
                        continue;
                    }
                    if ("mongodb".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/mongoDb.png");
                        continue;
                    }
                    if ("sqlite".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/sqlite.png");
                        continue;
                    }
                    if ("other".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/default_db.png");
                        continue;
                    }
                    if ("clickhouse".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/clickhouse.png");
                        continue;
                    }
                    if ("oracle".equals(dbInfo.getDbType())) {
                        dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/oracle.png");
                        continue;
                    }
                    dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/default_db.png");
                    continue;
                }
                dbInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/mysql.png");
            }
        }
        catch (Exception e) {
            logger.error("\u8bbe\u7f6e\u6570\u636e\u5e93logo\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u8bbe\u7f6e\u6570\u636e\u5e93logo\u9519\u8bef", e.toString(), "2");
        }
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.dbInfoMapper.updateToTargetAccount(params);
    }

    public List<HostGroup> setGroupInList(List<DbInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (DbInfo appInfo : recordList) {
            if (StringUtils.isEmpty((CharSequence)appInfo.getGroupId())) continue;
            String groupNames = "";
            for (String groupId : appInfo.getGroupId().split(",")) {
                if (!hostGroupMap.containsKey(groupId)) continue;
                groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
            if (groupNames.endsWith(",")) {
                groupNames = groupNames.substring(0, groupNames.length() - 1);
            }
            appInfo.setGroupId(groupNames);
        }
        return hostGroupList;
    }

    public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
        List<HostGroup> hostGroupList = new ArrayList();
        if (null != groupIdsArr && groupIdsArr.length > 0) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupIds", groupIdsArr);
            hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        }
        if (!StringUtils.isEmpty((CharSequence)ids)) {
            DbInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6e\u6570\u636e\u5e93\u6807\u7b7e\uff1a" + ho.getAliasName(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }
}

