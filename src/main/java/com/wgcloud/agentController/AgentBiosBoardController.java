/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.agentController;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.entity.HostPciInfo;
import com.wgcloud.util.AgentUtils;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.TokenUtils;
import com.wgcloud.util.staticvar.BatchData;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/agentBiosBoardGo"})
public class AgentBiosBoardController {
    private static final Logger logger = LoggerFactory.getLogger(AgentBiosBoardController.class);
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AgentUtils agentUtils;

    @ResponseBody
    @RequestMapping(value={"/minTask"})
    public JSONObject minTask(@RequestBody String paramBean) {
        JSONObject agentJsonObject = (JSONObject)JSONUtil.parse((Object)paramBean);
        logger.debug("agent\u4e0a\u62a5\u4e3b\u673a\u7684BIOS\u548c\u4e3b\u677f\u6570\u636e-------------" + agentJsonObject.toString());
        JSONObject resultJson = new JSONObject();
        if (!this.tokenUtils.checkAgentToken(agentJsonObject)) {
            logger.error("Token is error");
            resultJson.set("result", "Token is error");
            return resultJson;
        }
        String bindIp = agentJsonObject.getStr("bindIp");
        String hostName = agentJsonObject.getStr("hostName");
        String biosInfo = agentJsonObject.getStr("biosInfo");
        String baseboard = agentJsonObject.getStr("baseboard");
        String pciData = agentJsonObject.getStr("pciData");
        Date nowTime = new Date();
        if (StringUtils.isEmpty((CharSequence)(bindIp = this.agentUtils.checkBindIP(bindIp, hostName)))) {
            resultJson.set("result", "error: bindIp is null");
            return resultJson;
        }
        try {
            if (!StringUtils.isEmpty((CharSequence)biosInfo)) {
                HostUtil.setImportDataHandler(bindIp + "_HOST_BIOS", biosInfo, nowTime);
            }
            if (!StringUtils.isEmpty((CharSequence)baseboard)) {
                HostUtil.setImportDataHandler(bindIp + "_BASE_BOARD", baseboard, nowTime);
            }
            if (!StringUtils.isEmpty((CharSequence)pciData)) {
                this.addHostPci(pciData, nowTime, bindIp);
            }
            resultJson.set("result", "success");
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790bios\u4e0a\u62a5\u6570\u636e\u9519\u8bef", (Throwable)e);
            resultJson.set("result", (Object)("error:" + e.toString()));
        }
        return resultJson;
    }

    private void addHostPci(String hostPciListJsonString, Date nowTime, String bindIp) {
        if (this.isExists(bindIp)) {
            logger.error("agentHostPCIGo multiple times at the same time: " + bindIp);
            return;
        }
        JSONArray hostPciListJson = JSONUtil.parseArray((String)hostPciListJsonString);
        List<HostPciInfo> willSaveList = new ArrayList();
        for (Object hostPciObj : hostPciListJson) {
            try {
                String productName;
                String vendorName;
                HostPciInfo hostPciInfo = new HostPciInfo();
                hostPciInfo.setHostname(bindIp);
                hostPciInfo.setCreateTime(nowTime);
                JSONObject dataJsonObject = (JSONObject)JSONUtil.parse(hostPciObj);
                String address = dataJsonObject.getStr("address");
                if (!StringUtils.isEmpty((CharSequence)address)) {
                    hostPciInfo.setDeviceId(address);
                }
                if (!StringUtils.isEmpty((CharSequence)(vendorName = dataJsonObject.getStr("vendorName")))) {
                    hostPciInfo.setVendorName(vendorName);
                }
                if (!StringUtils.isEmpty((CharSequence)(productName = dataJsonObject.getStr("productName")))) {
                    hostPciInfo.setProductName(productName);
                }
                willSaveList.add(hostPciInfo);
            }
            catch (Exception e) {
                logger.error("\u89e3\u6790\u76d1\u63a7\u4e3b\u673a\u7684PCI\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            }
        }
        if (!CollectionUtil.isEmpty(willSaveList)) {
            HostPciInfo hostPciInfo = new HostPciInfo();
            hostPciInfo.setDeviceId("\u603b\u5171" + willSaveList.size() + "\u6761\u6570\u636e");
            if (!StaticKeys.LICENSE_STATE.equals("1") && willSaveList.size() > 5) {
                willSaveList = willSaveList.subList(0, 5);
                hostPciInfo.setHostname(bindIp);
                hostPciInfo.setCreateTime(nowTime);
                hostPciInfo.setProductName("\u63d0\u793a: \u5347\u7ea7\u5230\u4e13\u4e1a\u7248\u53ef\u4ee5\u67e5\u770b\u6240\u6709\u6570\u636e\u54e6");
                willSaveList.add(hostPciInfo);
            }
            BatchData.HOST_PCI_LIST.addAll(willSaveList);
        }
    }

    private boolean isExists(String bindIp) {
        if (StringUtils.isEmpty((CharSequence)bindIp)) {
            return true;
        }
        ArrayList<HostPciInfo> hostPciList = new ArrayList<HostPciInfo>();
        hostPciList.addAll(BatchData.HOST_PCI_LIST);
        for (HostPciInfo hostPciInfo : hostPciList) {
            if (!hostPciInfo.getHostname().equals(bindIp)) continue;
            return true;
        }
        return false;
    }
}

