/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.mapper.DbTableMapper;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.MongoDbUtil;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.jdbc.ConnectionUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.redis.RedisUtil;
import com.wgcloud.util.staticvar.BatchData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DbTableService {
    private static final Logger logger = LoggerFactory.getLogger(DbTableService.class);
    @Autowired
    private DbTableMapper dbTableMapper;
    @Autowired
    private DbInfoService dbInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MongoDbUtil mongoDbUtil;
    @Autowired
    private ConnectionUtil connectionUtil;
    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<DbTable> list = this.dbTableMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(DbTable dbTable) throws Exception {
        dbTable.setId(UUIDUtil.getUUID());
        dbTable.setCreateTime(new Date());
        if (!StringUtils.isEmpty((CharSequence)dbTable.getTableName())) {
            dbTable.setTableName(dbTable.getTableName().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)dbTable.getResultExp())) {
            dbTable.setResultExp(dbTable.getResultExp().trim());
        }
        this.dbTableMapper.save(dbTable);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.dbTableMapper.countByParams(params);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.dbTableMapper.updateActive(params);
    }

    public Long sumByParams(Map<String, Object> params) throws Exception {
        return this.dbTableMapper.sumByParams(params);
    }

    public void warnCheckExp(List<DbTable> dbTableList, List<DbInfo> dbInfos) {
        HashMap<String, String> dbInfoMap = new HashMap<String, String>();
        for (DbInfo dbInfo : dbInfos) {
            dbInfoMap.put(dbInfo.getId(), dbInfo.getAccount());
        }
        for (DbTable dbTable : dbTableList) {
            try {
                if ("2".equals(dbTable.getState()) && !StringUtils.isEmpty((CharSequence)dbTable.getTestErrorMsg())) {
                    Runnable runnable = () -> WarnOtherUtil.sendDbTableDown(dbTable, (String)dbInfoMap.get(dbTable.getDbInfoId()), true);
                    ThreadPoolUtil.executor.execute(runnable);
                    continue;
                }
                if (StringUtils.isEmpty((CharSequence)dbTable.getResultExp()) || null == dbTable.getTableCount()) continue;
                Boolean result = FormatUtil.validateExpression(dbTable.getResultExp(), FormatUtil.getExpressionEnv(dbTable.getTableCount()));
                if (result.booleanValue()) {
                    dbTable.setState("2");
                }
                Runnable runnable = () -> WarnOtherUtil.sendDbTableDown(dbTable, (String)dbInfoMap.get(dbTable.getDbInfoId()), result);
                ThreadPoolUtil.executor.execute(runnable);
            }
            catch (Exception e) {
                logger.error("\u6570\u636e\u8868\u76d1\u63a7\u7f16\u8bd1\u544a\u8b66\u8868\u8fbe\u5f0f\u9519\u8bef", (Throwable)e);
            }
        }
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.dbTableMapper.deleteById(id);
    }

    @Transactional
    public int deleteByDbInfoId(String dbInfoId) throws Exception {
        if (StringUtils.isEmpty((CharSequence)dbInfoId)) {
            return 0;
        }
        return this.dbTableMapper.deleteByDbInfoId(dbInfoId);
    }

    public void updateById(DbTable dbTable) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)dbTable.getTableName())) {
            dbTable.setTableName(dbTable.getTableName().trim());
        }
        if (!StringUtils.isEmpty((CharSequence)dbTable.getResultExp())) {
            dbTable.setResultExp(dbTable.getResultExp().trim());
        }
        this.dbTableMapper.updateById(dbTable);
    }

    @Transactional
    public void updateRecord(List<DbTable> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.dbTableMapper.updateList(recordList);
    }

    public DbTable selectById(String id) throws Exception {
        return this.dbTableMapper.selectById(id);
    }

    public List<DbTable> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.dbTableMapper.selectAllByParams(params);
    }

    public void taskThreadHandler() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        ArrayList<DbTable> dbTablesUpdate = new ArrayList<DbTable>();
        Date date = new Date();
        String tableCount = "";
        try {
            params.put("active", "1");
            List<DbInfo> dbInfos = this.dbInfoService.selectAllByParams(params);
            for (DbInfo dbInfo : dbInfos) {
                if (ServerBackupUtil.isExistDbInfoId(dbInfo.getId())) {
                    logger.info("\u6b64\u6570\u636e\u5e93\u7531wgcloud-server-backup\u76d1\u6d4b:" + dbInfo.getAliasName());
                    continue;
                }
                if (!StringUtils.isEmpty((CharSequence)dbInfo.getPasswd())) {
                    dbInfo.setPasswd(DESUtil.decryptForServerDb(dbInfo.getPasswd()));
                }
                if ("redis".equals(dbInfo.getDbType())) {
                    this.redisUtil.connectRedis(dbInfo);
                    this.messageErrorUtils.setErrorMsgHandler(dbInfo.getId(), dbInfo.getTestErrorMsg());
                    continue;
                }
                if ("mongodb".equals(dbInfo.getDbType())) {
                    this.mongoDbUtil.connectMongoDb(dbInfo);
                    this.messageErrorUtils.setErrorMsgHandler(dbInfo.getId(), dbInfo.getTestErrorMsg());
                    continue;
                }
                JdbcTemplate jdbcTemplate = this.connectionUtil.getJdbcTemplate(dbInfo);
                params.put("dbInfoId", dbInfo.getId());
                params.put("active", "1");
                List<DbTable> dbTables = this.selectAllByParams(params);
                for (DbTable dbTable : dbTables) {
                    if (StringUtils.isEmpty((CharSequence)dbTable.getWhereVal())) continue;
                    tableCount = this.connectionUtil.queryTableCount(dbInfo, jdbcTemplate, dbTable);
                    DbTableCount dbTableCount = new DbTableCount();
                    dbTableCount.setCreateTime(date);
                    dbTableCount.setDbTableId(dbTable.getId());
                    dbTableCount.setTableCount(tableCount);
                    BatchData.DBTABLE_COUNT_LIST.add(dbTableCount);
                    dbTable.setCreateTime(date);
                    dbTable.setTableCount(tableCount);
                    dbTablesUpdate.add(dbTable);
                    this.messageErrorUtils.setErrorMsgHandler(dbTable.getId(), dbTable.getTestErrorMsg());
                }
            }
            if (dbTablesUpdate.size() > 0) {
                this.warnCheckExp(dbTablesUpdate, dbInfos);
                ArrayList<DbTable> dbTablesFilterList = new ArrayList<DbTable>();
                for (DbTable dbTable : dbTablesUpdate) {
                    DbTable dbTableForUpdate = new DbTable();
                    dbTableForUpdate.setId(dbTable.getId());
                    dbTableForUpdate.setState(dbTable.getState());
                    dbTableForUpdate.setTableCount(dbTable.getTableCount());
                    dbTableForUpdate.setResTimes(dbTable.getResTimes());
                    dbTableForUpdate.setCreateTime(date);
                    dbTablesFilterList.add(dbTableForUpdate);
                }
                this.updateRecord(dbTablesFilterList);
            }
        }
        catch (Exception e) {
            logger.error("\u6570\u636e\u8868\u76d1\u63a7\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6570\u636e\u8868\u76d1\u63a7\u4efb\u52a1\u9519\u8bef", e.toString(), "2");
        }
    }

    public void saveLog(HttpServletRequest request, String action, DbTable dbTable) {
        if (null == dbTable) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u6570\u636e\u8868\u76d1\u6d4b\u4fe1\u606f\uff1a" + dbTable.getRemark(), "\u6570\u636e\u8868\uff1a" + dbTable.getRemark(), "2");
    }
}

