/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.entity.FileWarnInfo;
import com.wgcloud.entity.FileWarnState;
import com.wgcloud.service.AppInfoService;
import com.wgcloud.service.DockerInfoService;
import com.wgcloud.service.FileWarnInfoService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.PortInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.msg.WarnMailUtil;
import com.wgcloud.util.staticvar.BatchData;
import java.util.Date;
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
@RequestMapping(value={"/agentLogGo"})
public class AgentLogGoController {
    private static final Logger logger = LoggerFactory.getLogger(AgentLogGoController.class);
    @Resource
    private LogInfoService logInfoService;
    @Resource
    private SystemInfoService systemInfoService;
    @Autowired
    private DockerInfoService dockerInfoService;
    @Autowired
    private FileWarnInfoService fileWarnInfoService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private PortInfoService portInfoService;
    @Autowired
    private TokenUtils tokenUtils;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public JSONObject minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u65e5\u5fd7\u76d1\u63a7\u6570\u636e-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        Date nowtime = new Date();
        try {
            JSONObject fileWarnInfosJson = agentJsonObject.getJSONObject("fileWarnInfos");
            if (fileWarnInfosJson == null) {
                logger.error("fileWarnList is null");
                resultJson.set("result", "error\uff1afileWarnList is null");
                return resultJson;
            }
            for (String id : fileWarnInfosJson.keySet()) {
                JSONObject jsonObject = fileWarnInfosJson.getJSONObject(id);
                String filePath = jsonObject.getStr("filePath");
                String fileSize = jsonObject.getStr("fileSize");
                String warnRows = jsonObject.getStr("warnRows");
                String warnContent = jsonObject.getStr("warnContent");
                String rowsGatherCount = jsonObject.getStr("rowsGatherCount");
                String rowsCount = jsonObject.getStr("rowsCount");
                FileWarnState state = new FileWarnState();
                state.setCreateTime(nowtime);
                state.setFilePath(filePath);
                state.setFileWarnId(id);
                state.setWarContent(warnContent);
                state.setRowsCount(rowsCount);
                state.setRowsGatherCount(rowsGatherCount);
                if (!StringUtils.isEmpty((CharSequence)warnContent)) {
                    BatchData.FILEWARN_STATE_LIST.add(state);
                }
                FileWarnInfo info = new FileWarnInfo();
                info.setId(id);
                info.setCreateTime(nowtime);
                info.setFileSize(fileSize);
                info.setWarnRows(warnRows);
                BatchData.FILEWARN_INFO_LIST.add(info);
                if (StringUtils.isEmpty((CharSequence)warnContent)) continue;
                Runnable runnable = () -> {
                    try {
                        FileWarnInfo fileWarnInfo = this.fileWarnInfoService.selectById(info.getId());
                        if (fileWarnInfo != null) {
                            WarnMailUtil.sendFileWarnDown(fileWarnInfo, state, filePath, warnContent, true);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                ThreadPoolUtil.executor.execute(runnable);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u65e5\u5fd7\u76d1\u63a7\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error\uff1a" + e.toString()));
        }
        return resultJson;
    }
}

