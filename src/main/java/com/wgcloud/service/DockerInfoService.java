/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DockerInfo;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.mapper.DockerInfoMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.redis.RedisDataUtil;
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
public class DockerInfoService {
    private static final Logger logger = LoggerFactory.getLogger(DockerInfoService.class);
    @Autowired
    private DockerInfoMapper dockerInfoMapper;
    @Autowired
    private SystemInfoService systemInfoService;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private CommonConfig commonConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<DockerInfo> list = this.dockerInfoMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(DockerInfo dockerInfo, HttpServletRequest request) throws Exception {
        dockerInfo.setId(UUIDUtil.getUUID());
        Date nowDate = new Date();
        dockerInfo.setCreateTime(nowDate);
        dockerInfo.setDockerSize("0");
        dockerInfo.setMemPer(0.0);
        dockerInfo.setGatherDockerNames("\u83b7\u53d6\u4e2d");
        if (!StringUtils.isEmpty((CharSequence)dockerInfo.getDockerId())) {
            dockerInfo.setDockerId(dockerInfo.getDockerId().trim());
        }
        this.dockerInfoMapper.save(dockerInfo);
        this.addExtDataForm(dockerInfo, request, nowDate);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.dockerInfoMapper.updateActive(params);
    }

    private void addExtDataForm(DockerInfo dockerInfo, HttpServletRequest request, Date nowDate) throws Exception {
        String dataFromIndex = request.getParameter("dataFromIndex");
        int rowsLen = 0;
        if (!StringUtils.isEmpty((CharSequence)dataFromIndex)) {
            for (int i = 0; i <= Integer.valueOf(dataFromIndex); ++i) {
                String dockerId = request.getParameter("dockerId_" + i);
                String dockerName = request.getParameter("dockerName_" + i);
                if (StringUtils.isEmpty((CharSequence)dockerId) || StringUtils.isEmpty((CharSequence)dockerName)) continue;
                DockerInfo dockerInfoExt = new DockerInfo();
                BeanUtil.copyProperties((Object)dockerInfo, (Object)dockerInfoExt, (boolean)true);
                dockerInfoExt.setId(UUIDUtil.getUUID());
                dockerInfoExt.setCreateTime(nowDate);
                dockerInfoExt.setDockerId(dockerId);
                dockerInfoExt.setDockerName(dockerName);
                this.dockerInfoMapper.save(dockerInfoExt);
                this.saveLog(request, "\u6dfb\u52a0", dockerInfoExt);
                ++rowsLen;
            }
        }
    }

    public int deleteByHostName(Map<String, Object> map) throws Exception {
        return this.dockerInfoMapper.deleteByHostName(map);
    }

    @Transactional
    public void saveRecord(List<DockerInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (DockerInfo as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.dockerInfoMapper.insertList(recordList);
    }

    public void downByHostName(List<String> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.dockerInfoMapper.downByHostName(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.dockerInfoMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.dockerInfoMapper.deleteById(id);
    }

    @Transactional
    public void updateRecord(List<DockerInfo> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        this.dockerInfoMapper.updateList(recordList);
    }

    public void updateById(DockerInfo dockerInfo) throws Exception {
        this.dockerInfoMapper.updateById(dockerInfo);
    }

    public DockerInfo selectById(String id) throws Exception {
        return this.dockerInfoMapper.selectById(id);
    }

    public List<DockerInfo> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.dockerInfoMapper.selectAllByParams(params);
    }

    public void saveGroupId(String ids, String[] groupIdsArr, HttpServletRequest request) throws Exception {
        List<HostGroup> hostGroupList = new ArrayList();
        if (null != groupIdsArr && groupIdsArr.length > 0) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("groupIds", groupIdsArr);
            hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        }
        if (!StringUtils.isEmpty((CharSequence)ids)) {
            DockerInfo ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6eDOCKER\u6807\u7b7e\uff1a" + ho.getHostname(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public List<HostGroup> setGroupInList(List<DockerInfo> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (DockerInfo dockerInfo : recordList) {
            if (StringUtils.isEmpty((CharSequence)dockerInfo.getGroupId())) continue;
            String groupNames = "";
            for (String groupId : dockerInfo.getGroupId().split(",")) {
                if (!hostGroupMap.containsKey(groupId)) continue;
                groupNames = groupNames + (String)hostGroupMap.get(groupId) + ",";
            }
            if (groupNames.endsWith(",")) {
                groupNames = groupNames.substring(0, groupNames.length() - 1);
            }
            dockerInfo.setGroupId(groupNames);
        }
        return hostGroupList;
    }

    public List<JSONObject> getAllDocker(String hostName) {
        ArrayList<JSONObject> dockerInfoAllList = new ArrayList<JSONObject>();
        try {
            String dockerListJson = "";
            if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl())) {
                dockerListJson = RedisDataUtil.getValue(hostName + "_ALL_DOCKER");
            } else {
                Object data = StaticKeys.HOST_IMPORT_DATA.get(hostName + "_ALL_DOCKER");
                if (null != data) {
                    dockerListJson = data.toString();
                }
            }
            if (!StringUtils.isEmpty((CharSequence)dockerListJson)) {
                logger.debug("dockerListJson----------------" + dockerListJson);
                JSONObject dockersJson = JSONUtil.parseObj((String)dockerListJson);
                if (!StringUtils.isEmpty((CharSequence)dockersJson.getStr("gatherDockerNames")) && (dockersJson.getStr("gatherDockerNames").equals("No docker environment running") || dockersJson.getStr("gatherDockerNames").equals("This data has been configured not to be monitored"))) {
                    return dockerInfoAllList;
                }
                ArrayList<String> keys = new ArrayList<String>(dockersJson.keySet());
                for (String gatherContainerId : keys) {
                    dockerInfoAllList.add(dockersJson.getJSONObject(gatherContainerId));
                }
            }
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u5168\u91cfdocker\u4fe1\u606f\u67e5\u770b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u5168\u91cfdocker\u4fe1\u606f\u67e5\u770b\u9519\u8bef", e.toString(), "2");
        }
        return dockerInfoAllList;
    }

    public String getAllDockerTime(String hostName) {
        String dockerListTime = "";
        try {
            if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl())) {
                dockerListTime = RedisDataUtil.getValue(hostName + "_ALL_DOCKER" + "_DATETIME");
            } else {
                Object data = StaticKeys.HOST_IMPORT_DATA.get(hostName + "_ALL_DOCKER" + "_DATETIME");
                if (null != data) {
                    dockerListTime = data.toString();
                }
            }
        }
        catch (Exception e) {
            logger.error("\u4e3b\u673a\u5168\u91cfdocker\u4fe1\u606f\u67e5\u770b\u9519\u8bef", (Throwable)e);
            this.logInfoService.save("\u4e3b\u673a\u5168\u91cfdocker\u4fe1\u606f\u67e5\u770b\u9519\u8bef", e.toString(), "2");
        }
        return dockerListTime;
    }

    public void viewAllDockerHandler(Model model, String id) throws Exception {
        SystemInfo systemInfo = this.systemInfoService.selectById(id);
        model.addAttribute("systemInfo", (Object)systemInfo);
        if (!LicenseUtil.checkEnterpriseVersion()) {
            model.addAttribute("tipInfo", "\u63d0\u793a: \u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u53ef\u4ee5\u67e5\u770b\u5168\u91cfDocker\u5bb9\u5668\u5217\u8868\u54e6");
        }
        List<JSONObject> dockerListAll = this.getAllDocker(systemInfo.getHostname());
        model.addAttribute("dockerListAll", dockerListAll);
        if (null != dockerListAll) {
            model.addAttribute("dockerListAllSize", (Object)(" (" + dockerListAll.size() + ")"));
        }
        String dockerListTime = this.getAllDockerTime(systemInfo.getHostname());
        model.addAttribute("caijiDateTime", (Object)dockerListTime);
    }

    public void saveLog(HttpServletRequest request, String action, DockerInfo dockerInfo) {
        if (null == dockerInfo) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "docker\u5bb9\u5668\u76d1\u6d4b\u4fe1\u606f\uff1a" + dockerInfo.getHostname() + "\uff0c" + dockerInfo.getDockerName(), "docker\u540d\u79f0\uff1a" + dockerInfo.getDockerName() + "\uff0c\u83b7\u53d6docker\u65b9\u6cd5\uff1a" + dockerInfo.getDockerId(), "2");
    }
}

