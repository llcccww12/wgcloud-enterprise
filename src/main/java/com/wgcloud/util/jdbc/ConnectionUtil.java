/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util.jdbc;

import cn.hutool.core.collection.CollectionUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class ConnectionUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private DbInfoService dbInfoService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    CommonConfig commonConfig;

    public JdbcTemplate getJdbcTemplate(DbInfo dbInfo) throws Exception {
        String sqlinkey = FormatUtil.haveSqlDanger(dbInfo.getTestQuerySql(), this.commonConfig.getSqlInKeys());
        if (!StringUtils.isEmpty((CharSequence)sqlinkey)) {
            dbInfo.setTestErrorMsg("\u6d4b\u8bd5SQL\u8bed\u53e5\u542b\u6709sql\u654f\u611f\u5b57\u7b26" + sqlinkey + "\uff0c\u8bf7\u68c0\u67e5");
            return null;
        }
        JdbcTemplate jdbcTemplate = null;
        String driver = "";
        String url = dbInfo.getDbUrl();
        driver = "mysql".equals(dbInfo.getDbType()) ? "com.mysql.jdbc.Driver" : ("mariadb".equals(dbInfo.getDbType()) ? "org.mariadb.jdbc.Driver" : ("postgresql".equals(dbInfo.getDbType()) ? "org.postgresql.Driver" : ("sqlserver".equals(dbInfo.getDbType()) ? "com.microsoft.sqlserver.jdbc.SQLServerDriver" : ("db2".equals(dbInfo.getDbType()) ? "com.ibm.db2.jdbc.app.DB2Driver" : ("sqlite".equals(dbInfo.getDbType()) ? "org.sqlite.JDBC" : ("clickhouse".equals(dbInfo.getDbType()) ? dbInfo.getDriverName() : ("other".equals(dbInfo.getDbType()) ? dbInfo.getDriverName() : ("oracle".equals(dbInfo.getDbType()) ? "oracle.jdbc.driver.OracleDriver" : dbInfo.getDriverName()))))))));
        DbInfo dbInfoForUpdate = new DbInfo();
        dbInfoForUpdate.setId(dbInfo.getId());
        try {
            long startTimes = System.currentTimeMillis();
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(dbInfo.getUserName());
            dataSource.setPassword(dbInfo.getPasswd());
            jdbcTemplate = new JdbcTemplate((DataSource)dataSource);
            jdbcTemplate.setQueryTimeout(300);
            if ("mysql".equals(dbInfo.getDbType())) {
                jdbcTemplate.queryForRowSet("select version()");
            } else if ("mariadb".equals(dbInfo.getDbType())) {
                jdbcTemplate.queryForRowSet("select version()");
            } else if ("postgresql".equals(dbInfo.getDbType())) {
                jdbcTemplate.queryForRowSet("select version()");
            } else if ("sqlserver".equals(dbInfo.getDbType())) {
                jdbcTemplate.queryForRowSet("SELECT @@VERSION");
            } else if ("db2".equals(dbInfo.getDbType())) {
                jdbcTemplate.queryForRowSet("SELECT SERVICE_LEVEL FROM SYSIBMADM.ENV_INST_INFO");
            } else if ("sqlite".equals(dbInfo.getDbType())) {
                jdbcTemplate.queryForRowSet("select sqlite_version()");
            } else if ("clickhouse".equals(dbInfo.getDbType())) {
                jdbcTemplate.queryForRowSet("select version()");
            } else if ("other".equals(dbInfo.getDbType())) {
                jdbcTemplate.queryForRowSet(dbInfo.getTestQuerySql());
            } else if ("oracle".equals(dbInfo.getDbType())) {
                jdbcTemplate.queryForRowSet("select * from v$version");
            } else {
                jdbcTemplate.queryForRowSet(dbInfo.getTestQuerySql());
            }
            long endTimes = System.currentTimeMillis();
            String resTimes = endTimes - startTimes + "";
            dbInfoForUpdate.setResTimes(Integer.valueOf(resTimes));
            dbInfoForUpdate.setDbState("1");
            if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(dbInfo.getId()))) {
                Runnable runnable = () -> WarnOtherUtil.sendDbDown(dbInfo, false);
                ThreadPoolUtil.executor.execute(runnable);
            }
            dbInfoForUpdate.setCreateTime(new Date());
            if (!StringUtils.isEmpty((CharSequence)dbInfoForUpdate.getId())) {
                this.dbInfoService.updateById(dbInfoForUpdate);
            }
            return jdbcTemplate;
        }
        catch (Exception e) {
            jdbcTemplate = null;
            logger.error("\u76d1\u63a7\u6570\u636e\u5e93\u8fde\u63a5\u9519\u8bef", (Throwable)e);
            dbInfoForUpdate.setDbState("2");
            dbInfo.setTestErrorMsg(e.toString());
            this.messageErrorUtils.setErrorMsgHandler(dbInfo.getId(), dbInfo.getTestErrorMsg());
            if (!StringUtils.isEmpty((CharSequence)dbInfoForUpdate.getId())) {
                this.dbInfoService.updateById(dbInfoForUpdate);
            }
            Runnable runnable = () -> WarnOtherUtil.sendDbDown(dbInfo, true);
            ThreadPoolUtil.executor.execute(runnable);
            this.logInfoService.save("\u76d1\u63a7\u6570\u636e\u5e93\u8fde\u63a5\u9519\u8bef\uff1a" + dbInfo.getAliasName(), "\u6570\u636e\u5e93\u540d\u79f0\uff1a" + dbInfo.getAliasName() + "\uff0c" + e.toString(), "2");
            return null;
        }
    }

    public String queryTableCount(DbInfo dbInfo, JdbcTemplate jdbcTemplate, DbTable dbTable) {
        try {
            dbTable.setState("1");
            dbTable.setTestErrorMsg("");
            String sqlinkey = FormatUtil.haveSqlDanger(dbTable.getWhereVal(), this.commonConfig.getSqlInKeys());
            if (!StringUtils.isEmpty((CharSequence)sqlinkey)) {
                String errinfo = "\u7edf\u8ba1SQL\u8bed\u53e5\u542b\u6709sql\u654f\u611f\u5b57\u7b26" + sqlinkey;
                logger.error("\u7edf\u8ba1\u6570\u636e\u8868\u9519\u8bef\uff1a" + errinfo);
                this.logInfoService.save(errinfo + "\uff1a" + dbInfo.getAliasName(), "\u6570\u636e\u5e93\u540d\u79f0\uff1a" + dbInfo.getAliasName() + "\uff0c\u6570\u636e\u8868\u540d\u79f0\uff1a" + dbTable.getRemark() + "\uff0c\u9519\u8bef\u4fe1\u606f\uff1a" + errinfo, "2");
                return "";
            }
            if (null == jdbcTemplate) {
                dbTable.setState("2");
                return "";
            }
            long startTimes = System.currentTimeMillis();
            List list = jdbcTemplate.queryForList(dbTable.getWhereVal());
            long endTimes = System.currentTimeMillis();
            String resTimes = endTimes - startTimes + "";
            dbTable.setResTimes(Integer.valueOf(resTimes));
            if (CollectionUtil.isEmpty((Collection)list)) {
                return "";
            }
            String resultData = this.getDataFromQueryResult(list, dbTable);
            this.queryResultToTable(list, dbTable);
            return resultData;
        }
        catch (Exception e) {
            dbTable.setState("2");
            logger.error("\u6570\u636e\u8868\u76d1\u63a7\u9519\u8bef", (Throwable)e);
            dbTable.setTestErrorMsg(e.toString());
            this.logInfoService.save("\u6570\u636e\u8868\u76d1\u63a7sql\u6267\u884c\u9519\u8bef\uff1a" + dbInfo.getAliasName(), "\u6570\u636e\u5e93\u540d\u79f0\uff1a" + dbInfo.getAliasName() + "\uff0c\u6570\u636e\u8868\u540d\u79f0\uff1a" + dbTable.getRemark() + "\uff0c\u9519\u8bef\u4fe1\u606f\uff1a" + e.toString(), "2");
            return "";
        }
    }

    public String getDataFromQueryResult(List<Map<String, Object>> list, DbTable dbTable) throws Exception {
        Map<String, Object> mapResult = list.get(0);
        String resultData = "";
        if (StringUtils.isEmpty((CharSequence)dbTable.getTableName())) {
            Iterator<String> iterator = mapResult.keySet().iterator();
            if (iterator.hasNext()) {
                String key = iterator.next();
                resultData = mapResult.get(key).toString();
            }
        } else {
            String[] colNameArr = this.getColNameArray(dbTable.getTableName());
            for (int i = 0; i < colNameArr.length; ++i) {
                resultData = i != colNameArr.length - 1 ? resultData + mapResult.get(colNameArr[i]).toString() + "," : resultData + mapResult.get(colNameArr[i]).toString();
            }
        }
        return resultData;
    }

    public String[] getColNameArray(String dbTableCols) {
        String[] colNameArr = new String[]{};
        if (!StringUtils.isEmpty((CharSequence)dbTableCols)) {
            colNameArr = dbTableCols.split(",");
        }
        return colNameArr;
    }

    public String testQueryTable(DbInfo dbInfo, JdbcTemplate jdbcTemplate, DbTable dbTable) {
        try {
            dbTable.setState("1");
            dbTable.setTestErrorMsg("");
            String sqlinkey = FormatUtil.haveSqlDanger(dbTable.getWhereVal(), this.commonConfig.getSqlInKeys());
            if (!StringUtils.isEmpty((CharSequence)sqlinkey)) {
                String errinfo = "\u7edf\u8ba1SQL\u8bed\u53e5\u542b\u6709sql\u654f\u611f\u5b57\u7b26" + sqlinkey;
                logger.error("\u7edf\u8ba1\u6570\u636e\u8868\u9519\u8bef\uff1a" + errinfo);
                this.logInfoService.save(errinfo + "\uff1a" + dbInfo.getAliasName(), "\u6570\u636e\u5e93\u540d\u79f0\uff1a" + dbInfo.getAliasName() + "\uff0c\u6570\u636e\u8868\u540d\u79f0\uff1a" + dbTable.getRemark() + "\uff0c\u9519\u8bef\u4fe1\u606f\uff1a" + errinfo, "2");
                return errinfo;
            }
            if (null == jdbcTemplate) {
                dbTable.setState("2");
                return "\u6570\u636e\u5e93\u8fde\u63a5\u5931\u8d25";
            }
            long startTimes = System.currentTimeMillis();
            List list = jdbcTemplate.queryForList(dbTable.getWhereVal());
            long endTimes = System.currentTimeMillis();
            String resTimes = endTimes - startTimes + "";
            dbTable.setResTimes(Integer.valueOf(resTimes));
            if (CollectionUtil.isEmpty((Collection)list)) {
                return "\u67e5\u8be2\u7ed3\u679c\u4e3a\u7a7a";
            }
            String resultData = this.getDataFromQueryResult(list, dbTable);
            this.queryResultToTable(list, dbTable);
            String result = "";
            dbTable.setTableCount(resultData);
            result = "success\u3010" + resultData + "\u3011\uff0c\u8017\u65f6" + FormatUtil.msToSecond(dbTable.getResTimes());
            if (!StringUtils.isEmpty((CharSequence)dbTable.getResultExp())) {
                Boolean resultValidate = FormatUtil.validateExpression(dbTable.getResultExp(), FormatUtil.getExpressionEnv(dbTable.getTableCount()));
                if (resultValidate.booleanValue()) {
                    result = result + "\uff0c\u544a\u8b66\u8868\u8fbe\u5f0f\u6210\u7acb";
                    dbTable.setState("2");
                } else {
                    result = result + "\uff0c\u544a\u8b66\u8868\u8fbe\u5f0f\u4e0d\u6210\u7acb";
                }
            }
            return result;
        }
        catch (Exception e) {
            dbTable.setState("2");
            logger.error("\u6570\u636e\u8868\u76d1\u63a7\u9519\u8bef", (Throwable)e);
            dbTable.setTestErrorMsg(e.toString());
            this.logInfoService.save("\u6570\u636e\u8868\u76d1\u63a7sql\u6267\u884c\u9519\u8bef\uff1a" + dbInfo.getAliasName(), "\u6570\u636e\u5e93\u540d\u79f0\uff1a" + dbInfo.getAliasName() + "\uff0c\u6570\u636e\u8868\u540d\u79f0\uff1a" + dbTable.getRemark() + "\uff0c\u9519\u8bef\u4fe1\u606f\uff1a" + e.toString(), "2");
            return "\u6570\u636e\u8868\u76d1\u63a7\u9519\u8bef:" + e.toString();
        }
    }

    public void queryResultToTable(List<Map<String, Object>> list, DbTable dbTable) {
        try {
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            List listResult = new ArrayList<Map<String, Object>>();
            listResult.addAll(list);
            if (listResult.size() > 10) {
                listResult = listResult.subList(0, 10);
            }
            StringBuilder tableHtml = new StringBuilder("<table class=\"table table-bordered table-hover\">");
            ArrayList<String> keyList = new ArrayList<String>();
            tableHtml.append("<tr>");
            for (Object keyObj : ((Map)listResult.get(0)).keySet()) {
                String key = (String) keyObj;
                tableHtml.append("<td>" + key + "</td>");
                keyList.add(key);
            }
            tableHtml.append("</tr>");
            for (Map map : (java.util.List<Map>)listResult) {
                tableHtml.append("<tr>");
                for (String key : keyList) {
                    if (null != map.get(key)) {
                        tableHtml.append("<td>" + map.get(key).toString() + "</td>");
                        continue;
                    }
                    tableHtml.append("<td></td>");
                }
                tableHtml.append("</tr>");
            }
            tableHtml.append("</table>");
            this.messageErrorUtils.setErrorMsgHandler(dbTable.getId() + "_TABLE_HTML", tableHtml.toString());
        }
        catch (Exception e) {
            logger.error("queryResultToTable", (Throwable)e);
            this.messageErrorUtils.setErrorMsgHandler(dbTable.getId() + "_TABLE_HTML", e.toString());
        }
    }
}

