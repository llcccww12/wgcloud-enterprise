/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.BaseEntity;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.service.DbInfoService;
import com.wgcloud.service.DbTableService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.MessageErrorUtils;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.staticvar.BatchData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentDbTableGo"})
public class AgentDbTableGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentDbTableGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private DbInfoService dbInfoService;
    @Autowired
    private DbTableService dbTableService;
    @Autowired
    private MessageErrorUtils messageErrorUtils;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private CommonConfig commonConfig;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public String minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        boolean checkResult = this.tokenUtils.checkAgentToken(agentJsonObject);
        if (!checkResult) {
            logger.error("Token is error");
            return ResDataUtils.resetErrorJson("Token is error");
        }
        logger.debug("server-backup\u76d1\u63a7\u6570\u636e\u5e93\u3001\u6570\u636e\u8868\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        Date nowtime = new Date();
        try {
            JSONArray dbInfosJsonArr = agentJsonObject.getJSONArray("dbInfosUpdate");
            JSONArray dbTablesJsonArr = agentJsonObject.getJSONArray("dbTablesUpdate");
            if (dbInfosJsonArr == null) {
                logger.error("dbInfosUpdate is null");
                return ResDataUtils.resetErrorJson("dbInfosUpdate is null");
            }
            List dbInfoList = JSONUtil.toList((JSONArray)dbInfosJsonArr, DbInfo.class);
            for (Object dbInfo : dbInfoList) {
                Runnable runnable;
                DbInfo dbInfoSaved = this.dbInfoService.selectById(((BaseEntity)dbInfo).getId());
                if ("1".equals(((DbInfo)dbInfo).getDbState())) {
                    ((DbInfo)dbInfo).setCreateTime(nowtime);
                    dbInfoSaved.setResTimes(((DbInfo)dbInfo).getResTimes());
                    runnable = () -> WarnOtherUtil.sendDbDown(dbInfoSaved, false);
                    ThreadPoolUtil.executor.execute(runnable);
                } else {
                    runnable = () -> WarnOtherUtil.sendDbDown(dbInfoSaved, true);
                    ThreadPoolUtil.executor.execute(runnable);
                }
                this.dbInfoService.updateById((DbInfo)dbInfo);
                this.messageErrorUtils.setErrorMsgHandler(((BaseEntity)dbInfo).getId(), ((DbInfo)dbInfo).getTestErrorMsg());
            }
            if (null != dbTablesJsonArr) {
                List dbTableList = JSONUtil.toList((JSONArray)dbTablesJsonArr, DbTable.class);
                for (DbTable dbTable : (java.util.List<DbTable>)dbTableList) {
                    dbTable.setCreateTime(nowtime);
                    DbTableCount dbTableCount = new DbTableCount();
                    dbTableCount.setCreateTime(nowtime);
                    dbTableCount.setDbTableId(dbTable.getId());
                    dbTableCount.setTableCount(dbTable.getTableCount());
                    BatchData.DBTABLE_COUNT_LIST.add(dbTableCount);
                }
                HashMap<String, Object> params = new HashMap<String, Object>();
                List<DbInfo> dbInfos = new ArrayList<DbInfo>();
                if ("true".equals(this.commonConfig.getUserInfoManage())) {
                    dbInfos = this.dbInfoService.selectAllByParams(params);
                }
                this.dbTableService.warnCheckExp(dbTableList, dbInfos);
                ArrayList<DbTable> dbTablesFilterList = new ArrayList<DbTable>();
                for (DbTable dbTable : (java.util.List<DbTable>)dbTableList) {
                    DbTable dbTableForUpdate = new DbTable();
                    dbTableForUpdate.setId(dbTable.getId());
                    dbTableForUpdate.setState(dbTable.getState());
                    dbTableForUpdate.setTableCount(dbTable.getTableCount());
                    dbTableForUpdate.setResTimes(dbTable.getResTimes());
                    dbTableForUpdate.setCreateTime(nowtime);
                    dbTablesFilterList.add(dbTableForUpdate);
                    this.messageErrorUtils.setErrorMsgHandler(dbTable.getId(), dbTable.getTestErrorMsg());
                    this.messageErrorUtils.setErrorMsgHandler(dbTable.getId() + "_TABLE_HTML", dbTable.getSqlTableHtml());
                }
                this.dbTableService.updateRecord(dbTablesFilterList);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7\u6570\u636e\u8868\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }
}

