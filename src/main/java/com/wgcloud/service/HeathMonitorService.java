/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.HeaderDto;
import com.wgcloud.dto.HeathMonitorResDto;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.HeathState;
import com.wgcloud.entity.HostGroup;
import com.wgcloud.mapper.HeathMonitorMapper;
import com.wgcloud.service.HostGroupService;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.RestUtil;
import com.wgcloud.util.ServerBackupUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import com.wgcloud.util.redis.RedisDataUtil;
import com.wgcloud.util.staticvar.BatchData;
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
public class HeathMonitorService {
    private static final Logger logger = LoggerFactory.getLogger(HeathMonitorService.class);
    @Autowired
    private HeathMonitorMapper heathMonitorMapper;
    @Autowired
    private LogInfoService logInfoService;
    @Autowired
    private RestUtil restUtil;
    @Resource
    private HostGroupService hostGroupService;
    @Autowired
    private CommonConfig commonConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<HeathMonitor> list = this.heathMonitorMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo selectByParamsNoContent(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<HeathMonitor> list = this.heathMonitorMapper.selectByParamsNoContent(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(HeathMonitor heathMonitor) throws Exception {
        heathMonitor.setId(UUIDUtil.getUUID());
        heathMonitor.setCreateTime(new Date());
        if (StringUtils.isEmpty((CharSequence)heathMonitor.getResNoKeyword())) {
            heathMonitor.setResNoKeyword("");
        }
        if (StringUtils.isEmpty((CharSequence)heathMonitor.getResKeyword())) {
            heathMonitor.setResKeyword("");
        }
        if (StringUtils.isEmpty((CharSequence)heathMonitor.getHeathUrl())) {
            heathMonitor.setHeathUrl(heathMonitor.getHeathUrl().trim());
        }
        this.setHeaderJson(heathMonitor);
        this.setFormJson(heathMonitor);
        this.heathMonitorMapper.save(heathMonitor);
    }

    @Transactional
    public void saveRecord(List<HeathMonitor> recordList) throws Exception {
        if (recordList.size() < 1) {
            return;
        }
        for (HeathMonitor as : recordList) {
            as.setId(UUIDUtil.getUUID());
        }
        this.heathMonitorMapper.insertList(recordList);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.heathMonitorMapper.countByParams(params);
    }

    public int updateActive(Map<String, Object> params) throws Exception {
        return this.heathMonitorMapper.updateActive(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.heathMonitorMapper.deleteById(id);
    }

    public void updateById(HeathMonitor heathMonitor) throws Exception {
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getHeathUrl())) {
            heathMonitor.setHeathUrl(heathMonitor.getHeathUrl().trim());
        }
        this.setHeaderJson(heathMonitor);
        this.setFormJson(heathMonitor);
        heathMonitor.setErrorMsg("");
        this.heathMonitorMapper.updateById(heathMonitor);
    }

    public void updateForServerBackup(HeathMonitor heathMonitor) throws Exception {
        heathMonitor.setErrorMsg("");
        this.heathMonitorMapper.updateById(heathMonitor);
    }

    public HeathMonitor selectById(String id) throws Exception {
        return this.heathMonitorMapper.selectById(id);
    }

    @Transactional
    public void updateRecord(List<HeathMonitor> recordList) throws Exception {
        this.heathMonitorMapper.updateList(recordList);
    }

    public List<HeathMonitor> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.heathMonitorMapper.selectAllByParams(params);
    }

    private void setHeaderJson(HeathMonitor heathMonitor) {
        ArrayList<HeaderDto> dtoList = new ArrayList<HeaderDto>();
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getHeaderKey())) {
            HeaderDto dto1 = new HeaderDto();
            dto1.setSort("1");
            dto1.setKey(heathMonitor.getHeaderKey());
            dto1.setValue(heathMonitor.getHeaderValue());
            dtoList.add(dto1);
        }
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getHeaderKey2())) {
            HeaderDto dto2 = new HeaderDto();
            dto2.setSort("2");
            dto2.setKey(heathMonitor.getHeaderKey2());
            dto2.setValue(heathMonitor.getHeaderValue2());
            dtoList.add(dto2);
        }
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getHeaderKey3())) {
            HeaderDto dto3 = new HeaderDto();
            dto3.setSort("3");
            dto3.setKey(heathMonitor.getHeaderKey3());
            dto3.setValue(heathMonitor.getHeaderValue3());
            dtoList.add(dto3);
        }
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getHeaderKey4())) {
            HeaderDto dto4 = new HeaderDto();
            dto4.setSort("4");
            dto4.setKey(heathMonitor.getHeaderKey4());
            dto4.setValue(heathMonitor.getHeaderValue4());
            dtoList.add(dto4);
        }
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getHeaderKey5())) {
            HeaderDto dto5 = new HeaderDto();
            dto5.setSort("5");
            dto5.setKey(heathMonitor.getHeaderKey5());
            dto5.setValue(heathMonitor.getHeaderValue5());
            dtoList.add(dto5);
        }
        heathMonitor.setHeaderJson(JSONUtil.toJsonStr(dtoList));
    }

    private void setFormJson(HeathMonitor heathMonitor) {
        ArrayList<HeaderDto> dtoList = new ArrayList<HeaderDto>();
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getFormKey())) {
            HeaderDto dto1 = new HeaderDto();
            dto1.setSort("1");
            dto1.setKey(heathMonitor.getFormKey());
            dto1.setValue(heathMonitor.getFormValue());
            dtoList.add(dto1);
        }
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getFormKey2())) {
            HeaderDto dto2 = new HeaderDto();
            dto2.setSort("2");
            dto2.setKey(heathMonitor.getFormKey2());
            dto2.setValue(heathMonitor.getFormValue2());
            dtoList.add(dto2);
        }
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getFormKey3())) {
            HeaderDto dto3 = new HeaderDto();
            dto3.setSort("3");
            dto3.setKey(heathMonitor.getFormKey3());
            dto3.setValue(heathMonitor.getFormValue3());
            dtoList.add(dto3);
        }
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getFormKey4())) {
            HeaderDto dto4 = new HeaderDto();
            dto4.setSort("4");
            dto4.setKey(heathMonitor.getFormKey4());
            dto4.setValue(heathMonitor.getFormValue4());
            dtoList.add(dto4);
        }
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getFormKey5())) {
            HeaderDto dto5 = new HeaderDto();
            dto5.setSort("5");
            dto5.setKey(heathMonitor.getFormKey5());
            dto5.setValue(heathMonitor.getFormValue5());
            dtoList.add(dto5);
        }
        heathMonitor.setFormJson(JSONUtil.toJsonStr(dtoList));
    }

    public HashMap<String, String> displayHeaderJson(HeathMonitor heathMonitor) {
        HashMap<String, String> headerHashMap = new HashMap<String, String>();
        try {
            if (!StringUtils.isEmpty((CharSequence)heathMonitor.getHeaderJson())) {
                if (JSONUtil.isJsonObj((String)heathMonitor.getHeaderJson())) {
                    HeaderDto dto = (HeaderDto)JSONUtil.toBean((String)heathMonitor.getHeaderJson(), HeaderDto.class);
                    heathMonitor.setHeaderKey(dto.getKey());
                    heathMonitor.setHeaderValue(dto.getValue());
                    headerHashMap.put(dto.getKey(), dto.getValue());
                } else {
                    JSONArray jsonArray = JSONUtil.parseArray((String)heathMonitor.getHeaderJson());
                    if (jsonArray.size() > 0) {
                        heathMonitor.setHeaderSize(" (" + jsonArray.size() + ")");
                    }
                    for (Object jsonObject : jsonArray) {
                        HeaderDto dto = (HeaderDto)JSONUtil.toBean((JSONObject)((JSONObject)jsonObject), HeaderDto.class);
                        headerHashMap.put(dto.getKey(), dto.getValue());
                        if ("1".equals(dto.getSort())) {
                            heathMonitor.setHeaderKey(dto.getKey());
                            heathMonitor.setHeaderValue(dto.getValue());
                        }
                        if ("2".equals(dto.getSort())) {
                            heathMonitor.setHeaderKey2(dto.getKey());
                            heathMonitor.setHeaderValue2(dto.getValue());
                        }
                        if ("3".equals(dto.getSort())) {
                            heathMonitor.setHeaderKey3(dto.getKey());
                            heathMonitor.setHeaderValue3(dto.getValue());
                        }
                        if ("4".equals(dto.getSort())) {
                            heathMonitor.setHeaderKey4(dto.getKey());
                            heathMonitor.setHeaderValue4(dto.getValue());
                        }
                        if (!"5".equals(dto.getSort())) continue;
                        heathMonitor.setHeaderKey5(dto.getKey());
                        heathMonitor.setHeaderValue5(dto.getValue());
                    }
                }
            }
        }
        catch (Exception e) {
            logger.error("displayHeaderJson\u9519\u8bef", (Throwable)e);
        }
        return headerHashMap;
    }

    public HashMap<String, String> displayFormJson(HeathMonitor heathMonitor) {
        HashMap<String, String> headerHashMap = new HashMap<String, String>();
        try {
            if (!StringUtils.isEmpty((CharSequence)heathMonitor.getFormJson())) {
                JSONArray jsonArray = JSONUtil.parseArray((String)heathMonitor.getFormJson());
                if (jsonArray.size() > 0) {
                    heathMonitor.setFormDataSize(" (" + jsonArray.size() + ")");
                }
                for (Object jsonObject : jsonArray) {
                    HeaderDto dto = (HeaderDto)JSONUtil.toBean((JSONObject)((JSONObject)jsonObject), HeaderDto.class);
                    headerHashMap.put(dto.getKey(), dto.getValue());
                    if ("1".equals(dto.getSort())) {
                        heathMonitor.setFormKey(dto.getKey());
                        heathMonitor.setFormValue(dto.getValue());
                    }
                    if ("2".equals(dto.getSort())) {
                        heathMonitor.setFormKey2(dto.getKey());
                        heathMonitor.setFormValue2(dto.getValue());
                    }
                    if ("3".equals(dto.getSort())) {
                        heathMonitor.setFormKey3(dto.getKey());
                        heathMonitor.setFormValue3(dto.getValue());
                    }
                    if ("4".equals(dto.getSort())) {
                        heathMonitor.setFormKey4(dto.getKey());
                        heathMonitor.setFormValue4(dto.getValue());
                    }
                    if (!"5".equals(dto.getSort())) continue;
                    heathMonitor.setFormKey5(dto.getKey());
                    heathMonitor.setFormValue5(dto.getValue());
                }
            }
        }
        catch (Exception e) {
            logger.error("displayFormJson\u9519\u8bef", (Throwable)e);
        }
        return headerHashMap;
    }

    public void saveLog(HttpServletRequest request, String action, HeathMonitor heathMonitor) {
        if (null == heathMonitor) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u670d\u52a1\u63a5\u53e3\u76d1\u6d4b\u4fe1\u606f\uff1a" + heathMonitor.getAppName(), "\u670d\u52a1\u63a5\u53e3URL\uff1a" + heathMonitor.getHeathUrl(), "2");
    }

    public void addServerBackMark(List<HeathMonitor> list) throws Exception {
        for (HeathMonitor heathMonitor : list) {
            if (ServerBackupUtil.isExistHeathMonitorId(heathMonitor.getId())) {
                heathMonitor.setServerBackupMark("1");
                continue;
            }
            heathMonitor.setServerBackupMark("2");
        }
    }

    public HeathMonitor testHeathMonitor(HeathMonitor heathMonitor, Date date) {
        HeathMonitor heathMonitorForUpdate = new HeathMonitor();
        heathMonitorForUpdate.setId(heathMonitor.getId());
        int status = 500;
        HeathMonitorResDto heathMonitorResDto = new HeathMonitorResDto();
        HashMap<String, String> headerMap = this.displayHeaderJson(heathMonitor);
        HashMap<String, String> formMap = this.displayFormJson(heathMonitor);
        heathMonitorResDto = "POST".equals(heathMonitor.getMethod()) ? (formMap.isEmpty() ? this.restUtil.post(heathMonitor, headerMap) : this.restUtil.postFormData(heathMonitor, headerMap, formMap)) : this.restUtil.get(heathMonitor, headerMap);
        heathMonitorForUpdate.setResponseBodySize(heathMonitor.getResponseBodySize());
        status = heathMonitorResDto.getHeathStatus();
        heathMonitorForUpdate.setCreateTime(date);
        if (status != 200) {
            heathMonitorForUpdate.setCreateTime(null);
        }
        heathMonitorForUpdate.setHeathStatus(status + "");
        heathMonitor.setHeathStatus(heathMonitorForUpdate.getHeathStatus());
        heathMonitorForUpdate.setResTimes(heathMonitorResDto.getResTimes());
        HostUtil.setImportDataHandler(heathMonitor.getId() + "_HEATH_MONITOR", JSONUtil.parseObj((Object)heathMonitorResDto).toString(), date);
        try {
            this.updateForServerBackup(heathMonitorForUpdate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty((CharSequence)heathMonitorResDto.getErrorMsg())) {
            heathMonitorForUpdate.setErrorMsg(heathMonitorResDto.getErrorMsg());
        }
        return heathMonitorForUpdate;
    }

    public void taskThreadHandler(HeathMonitor heathMonitor, Date date) {
        HeathMonitor heathMonitorForUpdate = this.testHeathMonitor(heathMonitor, date);
        HeathState heathState = new HeathState();
        heathState.setResTimes(heathMonitorForUpdate.getResTimes());
        heathState.setHeathId(heathMonitorForUpdate.getId());
        heathState.setCreateTime(date);
        BatchData.HEATH_STATE_LIST.add(heathState);
        if (!"200".equals(heathMonitorForUpdate.getHeathStatus())) {
            WarnOtherUtil.sendHeathInfo(heathMonitor, true);
        } else if (!StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark(heathMonitor.getId()))) {
            WarnOtherUtil.sendHeathInfo(heathMonitor, false);
        }
    }

    public void updateToTargetAccount(Map<String, Object> params) throws Exception {
        this.heathMonitorMapper.updateToTargetAccount(params);
    }

    public List<HostGroup> setGroupInList(List<HeathMonitor> recordList, Model model, HttpServletRequest request) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<HostGroup> hostGroupList = this.hostGroupService.selectAllByParams(params, request);
        model.addAttribute("hostGroupList", hostGroupList);
        HashMap<String, String> hostGroupMap = new HashMap<String, String>();
        for (HostGroup hostGroup : hostGroupList) {
            hostGroupMap.put(hostGroup.getId(), hostGroup.getGroupName());
        }
        for (HeathMonitor appInfo : recordList) {
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
            HeathMonitor ho = null;
            for (String id : ids.split(",")) {
                ho = this.selectById(id);
                if (hostGroupList.size() > 0) {
                    ho.setGroupId(StringUtils.join((Object[])groupIdsArr, (String)","));
                } else {
                    ho.setGroupId("");
                }
                this.updateById(ho);
                this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + "\u8bbe\u7f6e\u670d\u52a1\u63a5\u53e3\u6807\u7b7e\uff1a" + ho.getAppName(), "\u6807\u7b7e\uff1a" + this.hostGroupService.returnGroupNames(hostGroupList), "2");
            }
        }
    }

    public HeathMonitorResDto getHeathMonitorResFromMem(String heathId) {
        HeathMonitorResDto heathMonitorResDto = new HeathMonitorResDto();
        if (!StringUtils.isEmpty((CharSequence)this.commonConfig.getRedisUrl())) {
            String jsonString = RedisDataUtil.getValue(heathId + "_HEATH_MONITOR");
            if (!StringUtils.isEmpty((CharSequence)jsonString)) {
                JSONObject jsonObject = JSONUtil.parseObj((String)jsonString);
                heathMonitorResDto = (HeathMonitorResDto)JSONUtil.toBean((JSONObject)jsonObject, HeathMonitorResDto.class);
            }
        } else {
            String jsonString = "";
            Object data = StaticKeys.HOST_IMPORT_DATA.get(heathId + "_HEATH_MONITOR");
            if (null != data) {
                jsonString = data.toString();
            }
            if (!StringUtils.isEmpty((CharSequence)jsonString)) {
                JSONObject jsonObject = JSONUtil.parseObj((String)jsonString);
                heathMonitorResDto = (HeathMonitorResDto)JSONUtil.toBean((JSONObject)jsonObject, HeathMonitorResDto.class);
            }
        }
        return heathMonitorResDto;
    }
}

