/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.MessageDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.DESUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.MongoDbUtil;
import com.wgcloud.util.PageUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.jdbc.ConnectionUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.redis.RedisUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/dbInfo"})
public class DbInfoController {
    private static final Logger logger = LoggerFactory.getLogger(DbInfoController.class);
    @Resource
    private DbInfoService dbInfoService;
    @Resource
    private DbTableService dbTableService;
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Resource
    private ConnectionUtil connectionUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private MongoDbUtil mongoDbUtil;
    @Autowired
    private CommonConfig commonConfig;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private MessageErrorUtils messageErrorUtils;

    @ResponseBody
    @RequestMapping(value={"agentList"})
    public String agentList(@RequestBody String paramBean, HttpServletRequest request) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        String checkResult = this.tokenUtils.preOpenDataAPICheck(agentJsonObject);
        if (!StringUtils.isEmpty((CharSequence)checkResult)) {
            return checkResult;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            if (null != agentJsonObject.get("dbInfoNames") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("dbInfoNames").toString())) {
                params.put("dbInfoNames", agentJsonObject.get("dbInfoNames").toString().split(","));
            }
            params.put("active", "1");
            List<DbInfo> dbInfoList = this.dbInfoService.selectAllByParams(params);
            ArrayList<DbInfo> dbInfoListResult = new ArrayList<DbInfo>();
            for (DbInfo dbInfo : dbInfoList) {
                dbInfo.setUserName(DESUtil.encryption(dbInfo.getUserName()));
                dbInfo.setDbUrl(DESUtil.encryption(dbInfo.getDbUrl()));
                dbInfo.setPasswd(DESUtil.encryption(DESUtil.decryptForServerDb(dbInfo.getPasswd())));
                String sqlinkey = FormatUtil.haveSqlDanger(dbInfo.getTestQuerySql(), this.commonConfig.getSqlInKeys());
                if (!StringUtils.isEmpty((CharSequence)sqlinkey)) {
                    logger.error("\u6d4b\u8bd5SQL\u8bed\u53e5\u542b\u6709sql\u654f\u611f\u5b57\u7b26" + sqlinkey);
                    continue;
                }
                dbInfoListResult.add(dbInfo);
            }
            if (null != agentJsonObject.get("SERVER_BACKUP_FLAG") && !StringUtils.isEmpty((CharSequence)agentJsonObject.get("SERVER_BACKUP_FLAG").toString())) {
                ServerBackupUtil.cacheSaveDbInfoId(dbInfoList);
                logger.info("server-backup request DBINFO-------" + IpUtil.getIpAddr(request));
            }
            return ResDataUtils.resetSuccessJson(dbInfoListResult);
        }
        catch (Exception e) {
            logger.error("agent\u83b7\u53d6\u76d1\u63a7\u6570\u636e\u5e93\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("agent\u83b7\u53d6\u76d1\u63a7\u6570\u636e\u5e93\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            return ResDataUtils.resetErrorJson(e.toString());
        }
    }

    @ResponseBody
    @RequestMapping(value={"validate"})
    public String valdateDbInfo(DbInfo dbInfo, Model model, HttpServletRequest request) {
        MessageDto messageDto = new MessageDto();
        try {
            if ("redis".equals(dbInfo.getDbType())) {
                String pong = this.redisUtil.connectRedis(dbInfo);
                if (StringUtils.isEmpty((CharSequence)pong)) {
                    messageDto.setCode("1");
                    messageDto.setMsg("\u8fde\u63a5Redis\u9519\u8bef\uff0c\u8bf7\u68c0\u67e5\u53c2\u6570\u662f\u5426\u6b63\u786e\u3002" + dbInfo.getTestErrorMsg());
                } else {
                    messageDto.setCode("0");
                    messageDto.setMsg("\u8fde\u63a5Redis\u6210\u529f");
                }
            } else if ("mongodb".equals(dbInfo.getDbType())) {
                this.mongoDbUtil.connectMongoDb(dbInfo);
                if ("2".equals(dbInfo.getDbState())) {
                    messageDto.setCode("1");
                    messageDto.setMsg("\u8fde\u63a5MongoDb\u9519\u8bef\uff0c\u8bf7\u68c0\u67e5\u53c2\u6570\u662f\u5426\u6b63\u786e\u3002" + dbInfo.getTestErrorMsg());
                } else {
                    messageDto.setCode("0");
                    messageDto.setMsg("\u8fde\u63a5MongoDb\u6210\u529f");
                }
            } else {
                JdbcTemplate JdbcTemplate2 = this.connectionUtil.getJdbcTemplate(dbInfo);
                if (JdbcTemplate2 == null) {
                    messageDto.setCode("1");
                    messageDto.setMsg("\u8fde\u63a5\u6570\u636e\u5e93\u9519\u8bef\uff0c\u8bf7\u68c0\u67e5\u53c2\u6570\u662f\u5426\u6b63\u786e\u3002" + dbInfo.getTestErrorMsg());
                } else {
                    messageDto.setCode("0");
                    messageDto.setMsg("\u8fde\u63a5\u6570\u636e\u5e93\u6210\u529f");
                }
            }
        }
        catch (Exception e) {
            logger.error("\u6d4b\u8bd5\u6570\u636e\u5e93\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u6d4b\u8bd5\u6570\u636e\u5e93\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
            messageDto.setCode("1");
            messageDto.setMsg("\u6d4b\u8bd5\u6570\u636e\u5e93\u4fe1\u606f\u9519\u8bef\uff0c" + e.toString());
        }
        return JSONUtil.toJsonStr((Object)messageDto);
    }

    @ResponseBody
    @RequestMapping(value={"testHeath"})
    public String testHeath(Model model, HttpServletRequest request) {
        String errorMsg = "\u6570\u636e\u5e93\u76d1\u63a7\u6d4b\u8bd5\u8fde\u63a5\u9519\u8bef\uff1a";
        String resultMsg = "success";
        try {
            if (!StaticKeys.LICENSE_STATE.equals("1")) {
                return "noPro";
            }
            String id = request.getParameter("id");
            if (!StringUtils.isEmpty((CharSequence)id)) {
                DbInfo dbInfo = this.dbInfoService.selectById(id);
                if (!StringUtils.isEmpty((CharSequence)dbInfo.getPasswd())) {
                    dbInfo.setPasswd(DESUtil.decryptForServerDb(dbInfo.getPasswd()));
                }
                if ("redis".equals(dbInfo.getDbType())) {
                    String pong = this.redisUtil.connectRedis(dbInfo);
                    if (StringUtils.isEmpty((CharSequence)pong)) {
                        resultMsg = "\u8fde\u63a5Redis\u9519\u8bef\uff0c\u8bf7\u68c0\u67e5\u53c2\u6570\u662f\u5426\u6b63\u786e\u3002" + dbInfo.getTestErrorMsg();
                    }
                } else if ("mongodb".equals(dbInfo.getDbType())) {
                    this.mongoDbUtil.connectMongoDb(dbInfo);
                    if ("2".equals(dbInfo.getDbState())) {
                        resultMsg = "\u8fde\u63a5MongoDb\u9519\u8bef\uff0c\u8bf7\u68c0\u67e5\u53c2\u6570\u662f\u5426\u6b63\u786e\u3002" + dbInfo.getTestErrorMsg();
                    }
                } else {
                    JdbcTemplate JdbcTemplate2 = this.connectionUtil.getJdbcTemplate(dbInfo);
                    if (JdbcTemplate2 == null) {
                        resultMsg = "\u8fde\u63a5\u6570\u636e\u5e93\u9519\u8bef\uff0c\u8bf7\u68c0\u67e5\u53c2\u6570\u662f\u5426\u6b63\u786e\u3002" + dbInfo.getTestErrorMsg();
                    }
                }
            } else {
                resultMsg = "\u7ed3\u679c\u597d\u50cf\u662f\u7a7a\u7684";
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            resultMsg = "\u6d4b\u8bd5\u9519\u8bef:" + e.toString();
        }
        return resultMsg;
    }

    @RequestMapping(value={"list"})
    public String dbInfoList(DbInfo dbInfo, Model model, HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        try {
            LicenseUtil.maxLicense_10(model, request, dbInfo);
            StringBuffer url = new StringBuffer();
            if (!StringUtils.isEmpty((CharSequence)dbInfo.getAccount())) {
                params.put("account", dbInfo.getAccount());
                url.append("&account=").append(dbInfo.getAccount());
            }
            if (!StringUtils.isEmpty((CharSequence)dbInfo.getAliasName())) {
                params.put("aliasName", dbInfo.getAliasName());
                url.append("&aliasName=").append(dbInfo.getAliasName());
            }
            if (!StringUtils.isEmpty((CharSequence)dbInfo.getGroupId())) {
                params.put("groupId", dbInfo.getGroupId());
                url.append("&groupId=").append(dbInfo.getGroupId());
            }
            if (!StringUtils.isEmpty((CharSequence)dbInfo.getDbState())) {
                params.put("dbState", dbInfo.getDbState());
                url.append("&dbState=").append(dbInfo.getDbState());
            }
            if (!StringUtils.isEmpty((CharSequence)dbInfo.getActive())) {
                params.put("active", dbInfo.getActive());
                url.append("&active=").append(dbInfo.getActive());
            }
            if (!StringUtils.isEmpty((CharSequence)dbInfo.getOrderBy())) {
                params.put("orderBy", dbInfo.getOrderBy());
                params.put("orderType", dbInfo.getOrderType());
                url.append("&orderBy=").append(dbInfo.getOrderBy());
                url.append("&orderType=").append(dbInfo.getOrderType());
            }
            HostUtil.addAccountquery(request, params);
            PageInfo pageInfo = this.dbInfoService.selectByParams(params, dbInfo.getPage(), dbInfo.getPageSize());
            for (DbInfo dbInfoTmp : (java.util.List<DbInfo>) pageInfo.getList()) {
                if ("true".equals(this.commonConfig.getShowWarnCount())) {
                    String warnQueryWd = "\u6570\u636e\u5e93\u8fde\u63a5\u5931\u8d25\u544a\u8b66\uff1a" + dbInfoTmp.getAliasName();
                    this.logInfoService.warnQueryHandle(dbInfoTmp, warnQueryWd);
                }
                dbInfoTmp.setResTimesSecond(FormatUtil.msToSecond(dbInfoTmp.getResTimes()));
            }
            if ("true".equals(this.commonConfig.getHostGroup())) {
                this.dbInfoService.setGroupInList(pageInfo.getList(), model, request);
            }
            this.dbInfoService.dbAddLogo(pageInfo.getList());
            this.dbInfoService.addServerBackMark(pageInfo.getList());
            PageUtil.initPageNumber(pageInfo, model);
            HostUtil.addAccountListModel(model);
            model.addAttribute("pageUrl", (Object)("/dbInfo/list?1=1" + url.toString()));
            model.addAttribute("page", (Object)pageInfo);
            model.addAttribute("dbInfo", (Object)dbInfo);
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u6570\u636e\u5e93\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u67e5\u8be2\u6570\u636e\u5e93\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "mysql/dbList";
    }

    @ResponseBody
    @RequestMapping(value={"saveGroupId"})
    public String saveGroupId(Model model, HttpServletRequest request) {
        try {
            String ids = request.getParameter("ids");
            String[] groupIdsArr = request.getParameterValues("groupId");
            this.dbInfoService.saveGroupId(ids, groupIdsArr, request);
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u6570\u636e\u5e93\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u6570\u636e\u5e93\u6807\u7b7e\u4fe1\u606f\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/dbInfo/list";
    }

    @RequestMapping(value={"edit"})
    public String edit(Model model, HttpServletRequest request) {
        String errorMsg = "\u6dfb\u52a0\u6570\u636e\u5e93\u9519\u8bef";
        String id = request.getParameter("id");
        DbInfo dbInfo = new DbInfo();
        try {
            if (!this.isAddContinue()) {
                return "redirect:/dbInfo/list?liceFlage=1";
            }
            model.addAttribute("sqlInKeys", (Object)this.commonConfig.getSqlInKeys());
            if (StringUtils.isEmpty((CharSequence)id)) {
                model.addAttribute("dbInfo", (Object)dbInfo);
                return "mysql/addDb";
            }
            dbInfo = this.dbInfoService.selectById(id);
            if (StringUtils.isEmpty((CharSequence)dbInfo.getDriverName())) {
                if ("mysql".equals(dbInfo.getDbType())) {
                    dbInfo.setDriverName("com.mysql.jdbc.Driver");
                    dbInfo.setTestQuerySql("select version()");
                } else if ("mariadb".equals(dbInfo.getDbType())) {
                    dbInfo.setDriverName("org.mariadb.jdbc.Driver");
                    dbInfo.setTestQuerySql("select version()");
                } else if ("postgresql".equals(dbInfo.getDbType())) {
                    dbInfo.setDriverName("org.postgresql.Driver");
                    dbInfo.setTestQuerySql("select version()");
                } else if ("sqlserver".equals(dbInfo.getDbType())) {
                    dbInfo.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    dbInfo.setTestQuerySql("SELECT @@VERSION");
                } else if ("db2".equals(dbInfo.getDbType())) {
                    dbInfo.setDriverName("com.ibm.db2.jdbc.app.DB2Driver");
                    dbInfo.setTestQuerySql("SELECT SERVICE_LEVEL FROM SYSIBMADM.ENV_INST_INFO");
                } else if ("sqlite".equals(dbInfo.getDbType())) {
                    dbInfo.setDriverName("org.sqlite.JDBC");
                    dbInfo.setTestQuerySql("select sqlite_version()");
                } else if ("other".equals(dbInfo.getDbType())) {
                    dbInfo.setDriverName("");
                    dbInfo.setTestQuerySql("");
                } else {
                    dbInfo.setDriverName("oracle.jdbc.driver.OracleDriver");
                    dbInfo.setTestQuerySql("select version()");
                }
            }
            dbInfo.setPasswd(DESUtil.decryptForServerDb(dbInfo.getPasswd()));
            model.addAttribute("dbInfo", (Object)dbInfo);
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "mysql/addDb";
    }

    @RequestMapping(value={"updateActive"})
    public String updateActive(Model model, HttpServletRequest request) {
        String errorMsg = "\u6279\u91cf\u5f00\u59cb\u76d1\u63a7\u548c\u505c\u6b62\u76d1\u63a7\u9519\u8bef";
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids = request.getParameter("id").split(",");
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("ids", ids);
                String activeValue = request.getParameter("active");
                params.put("active", activeValue);
                this.dbInfoService.updateActive(params);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dbInfo/list";
    }

    @RequestMapping(value={"save"})
    public String saveDbInfo(DbInfo dbInfo, Model model, HttpServletRequest request) {
        try {
            String sqlinkey = FormatUtil.haveSqlDanger(dbInfo.getTestQuerySql(), this.commonConfig.getSqlInKeys());
            if (!StringUtils.isEmpty((CharSequence)sqlinkey)) {
                model.addAttribute("dbInfo", (Object)dbInfo);
                model.addAttribute("sqlInKeys", (Object)this.commonConfig.getSqlInKeys());
                model.addAttribute("msg", (Object)("\u6d4b\u8bd5SQL\u8bed\u53e5\u542b\u6709sql\u654f\u611f\u5b57\u7b26" + sqlinkey + "\uff0c\u8bf7\u68c0\u67e5"));
                return "mysql/addDb";
            }
            dbInfo.setPasswd(DESUtil.encryptionForServerDb(dbInfo.getPasswd()));
            if (StringUtils.isEmpty((CharSequence)dbInfo.getId())) {
                AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
                if (null != accountInfo && !"admin".equals(accountInfo.getRole())) {
                    dbInfo.setAccount(accountInfo.getAccount());
                }
                this.dbInfoService.save(dbInfo);
                this.dbInfoService.saveLog(request, "\u6dfb\u52a0", dbInfo);
            } else {
                this.dbInfoService.updateById(dbInfo);
                this.dbInfoService.saveLog(request, "\u4fee\u6539", dbInfo);
            }
        }
        catch (Exception e) {
            logger.error("\u4fdd\u5b58\u6570\u636e\u5e93\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4fdd\u5b58\u6570\u636e\u5e93\u9519\u8bef", e.toString(), "2");
        }
        return "redirect:/dbInfo/list?msg=save";
    }

    @RequestMapping(value={"del"})
    public String delete(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String errorMsg = "\u5220\u9664\u6570\u636e\u5e93\u4fe1\u606f\u9519\u8bef";
        DbInfo DbInfo2 = new DbInfo();
        try {
            if (!StringUtils.isEmpty((CharSequence)request.getParameter("id"))) {
                String[] ids;
                for (String id : ids = request.getParameter("id").split(",")) {
                    DbInfo2 = this.dbInfoService.selectById(id);
                    this.dbInfoService.saveLog(request, "\u5220\u9664", DbInfo2);
                    this.dbTableService.deleteByDbInfoId(DbInfo2.getId());
                }
                this.dbInfoService.deleteById(ids);
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "redirect:/dbInfo/list?msg=del";
    }

    @RequestMapping(value={"view"})
    public String view(Model model, HttpServletRequest request) {
        String errorMsg = "\u67e5\u770b\u6570\u636e\u5e93\u76d1\u63a7\u8be6\u60c5";
        String id = request.getParameter("id");
        DbInfo dbInfo = new DbInfo();
        try {
            dbInfo = this.dbInfoService.selectById(id);
            model.addAttribute("dbInfo", (Object)dbInfo);
            if ("true".equals(this.commonConfig.getHostGroup())) {
                dbInfo.setGroupId(this.hostGroupService.returnGroupNames(dbInfo.getGroupId()));
            }
            dbInfo.setResTimesSecond(FormatUtil.msToSecond(dbInfo.getResTimes()));
            if ("2".equals(dbInfo.getDbState())) {
                model.addAttribute("dbInfoErrorMsg", (Object)this.messageErrorUtils.viewErrorMsgHandler(id));
            } else {
                model.addAttribute("dbInfoErrorMsg", "success");
            }
        }
        catch (Exception e) {
            logger.error(errorMsg, (Throwable)e);
            this.logInfoService.save(errorMsg, e.toString(), "2");
        }
        return "mysql/dbView";
    }

    private boolean isAddContinue() {
        try {
            HashMap<String, Object> params;
            int dbSize;
            if (!StaticKeys.LICENSE_STATE.equals("1") && (dbSize = this.dbInfoService.countByParams(params = new HashMap<String, Object>())) >= 10) {
                return false;
            }
        }
        catch (Exception e) {
            logger.error("isAddContinue error", (Throwable)e);
        }
        return true;
    }
}

