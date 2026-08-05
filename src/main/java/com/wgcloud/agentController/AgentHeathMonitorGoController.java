/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.HeathMonitorResDto;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.HeathState;
import com.wgcloud.service.HeathMonitorService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.ResDataUtils;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.staticvar.BatchData;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentHeathMonitorGo"})
public class AgentHeathMonitorGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentHeathMonitorGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Autowired
    private HeathMonitorService heathMonitorService;
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
        logger.debug("server-backup\u76d1\u63a7\u670d\u52a1\u63a5\u53e3\u4e0a\u62a5\u6570\u636e-------------" + agentJsonObject.toString());
        Date nowTime = new Date();
        try {
            JSONArray heathMonitorsJsonArr = agentJsonObject.getJSONArray("heathMonitorsUpdate");
            if (heathMonitorsJsonArr == null) {
                logger.error("heathMonitorsUpdate is null");
                return ResDataUtils.resetErrorJson("heathMonitorsUpdate is null");
            }
            List HeathMonitorResDtoList = JSONUtil.toList((JSONArray)heathMonitorsJsonArr, HeathMonitorResDto.class);
            for (HeathMonitorResDto heathMonitorResDto : (java.util.List<HeathMonitorResDto>)HeathMonitorResDtoList) {
                Runnable runnable;
                HeathMonitor heathMonitorToUpdate = new HeathMonitor();
                HeathMonitor heathMonitorSaved = this.heathMonitorService.selectById(heathMonitorResDto.getId());
                heathMonitorToUpdate.setId(heathMonitorResDto.getId());
                heathMonitorToUpdate.setHeathStatus(heathMonitorResDto.getHeathStatus() + "");
                heathMonitorToUpdate.setResponseBodySize(heathMonitorResDto.getResponseBodySize());
                heathMonitorToUpdate.setCreateTime(nowTime);
                heathMonitorToUpdate.setResTimes(heathMonitorResDto.getResTimes());
                if (!"200".equals(heathMonitorResDto.getHeathStatus() + "")) {
                    heathMonitorToUpdate.setCreateTime(null);
                }
                try {
                    this.heathMonitorService.updateForServerBackup(heathMonitorToUpdate);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                HostUtil.setImportDataHandler(heathMonitorResDto.getId() + "_HEATH_MONITOR", JSONUtil.parseObj((Object)heathMonitorResDto).toString(), nowTime);
                HeathState heathState = new HeathState();
                heathState.setResTimes(heathMonitorToUpdate.getResTimes());
                heathState.setHeathId(heathMonitorToUpdate.getId());
                heathState.setCreateTime(nowTime);
                BatchData.HEATH_STATE_LIST.add(heathState);
                heathMonitorSaved.setHeathStatus(heathMonitorToUpdate.getHeathStatus());
                if (!"200".equals(heathMonitorToUpdate.getHeathStatus() + "")) {
                    runnable = () -> WarnOtherUtil.sendHeathInfo(heathMonitorSaved, true);
                    ThreadPoolUtil.executor.execute(runnable);
                    continue;
                }
                if (StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(heathMonitorToUpdate.getId()))) continue;
                runnable = () -> WarnOtherUtil.sendHeathInfo(heathMonitorSaved, false);
                ThreadPoolUtil.executor.execute(runnable);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790server-backup\u76d1\u63a7\u670d\u52a1\u63a5\u53e3\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            return ResDataUtils.resetErrorJson(e.toString());
        }
        return ResDataUtils.resetSuccessJson(null);
    }
}

