/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.config.MailConfig;
import com.wgcloud.dto.MessageDto;
import com.wgcloud.dto.SubtitleDto;
import com.wgcloud.entity.DiskIoState;
import com.wgcloud.entity.HostWarnDiy;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.mapper.DiskIoStateMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.staticvar.StaticKeys;
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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class DiskIoStateService {
    private static final Logger logger = LoggerFactory.getLogger(DiskIoStateService.class);
    @Autowired
    private DiskIoStateMapper diskIoStateMapper;
    @Autowired
    private MailConfig mailConfig;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<DiskIoState> list = this.diskIoStateMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(DiskIoState diskIoState) throws Exception {
        diskIoState.setId(UUIDUtil.getUUID());
        diskIoState.setCreateTime(new Date());
        this.diskIoStateMapper.save(diskIoState);
    }

    public void saveRecord(List<DiskIoState> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (DiskIoState as : recordList) {
                as.setId(UUIDUtil.getUUID());
                as.setCreateTime(new Date());
            }
            this.diskIoStateMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("DiskIoState save error", (Throwable)e);
        }
    }

    public int deleteById(String[] id) throws Exception {
        return this.diskIoStateMapper.deleteById(id);
    }

    public DiskIoState selectById(String id) throws Exception {
        return this.diskIoStateMapper.selectById(id);
    }

    public List<DiskIoState> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.diskIoStateMapper.selectAllByParams(params);
    }

    public List<DiskIoState> selectAllByParamsAndDay(String hostname, Model model, HttpServletRequest request) {
        List<DiskIoState> diskIoStateList = new ArrayList<DiskIoState>();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("hostname", hostname);
            String am = request.getParameter("am");
            this.setDateParamForDisk(am, params, model);
            model.addAttribute("amList", this.getDayListParamForDisk());
            diskIoStateList = this.selectAllByParams(params);
            return diskIoStateList;
        }
        catch (Exception e) {
            logger.error("\u67e5\u8be2\u78c1\u76d8\u8bfb\u5199\u901f\u7387\u56fe\u8868\u6570\u636e\u9519\u8bef", (Throwable)e);
            return diskIoStateList;
        }
    }

    public List<MessageDto> getDayListParamForDisk() {
        MessageDto dto1 = new MessageDto();
        dto1.setCode("am1");
        dto1.setMsg("\u6700\u8fd11\u5929");
        MessageDto dto2 = new MessageDto();
        dto2.setCode("am2");
        dto2.setMsg("\u6700\u8fd12\u5929");
        MessageDto dto3 = new MessageDto();
        dto3.setCode("am3");
        dto3.setMsg("\u6700\u8fd13\u5929");
        MessageDto dto4 = new MessageDto();
        dto4.setCode("am4");
        dto4.setMsg("\u6700\u8fd14\u5929");
        MessageDto dto5 = new MessageDto();
        dto5.setCode("am5");
        dto5.setMsg("\u6700\u8fd15\u5929");
        MessageDto dto6 = new MessageDto();
        dto6.setCode("am6");
        dto6.setMsg("\u6700\u8fd16\u5929");
        MessageDto dto7 = new MessageDto();
        dto7.setCode("am7");
        dto7.setMsg("\u6700\u8fd11\u5468");
        ArrayList<MessageDto> timeList = new ArrayList<MessageDto>();
        timeList.add(dto1);
        timeList.add(dto2);
        timeList.add(dto3);
        timeList.add(dto4);
        timeList.add(dto5);
        timeList.add(dto6);
        timeList.add(dto7);
        return timeList;
    }

    public void setDateParamForDisk(String am, Map<String, Object> params, Model model) {
        if ("null".equals(am)) {
            am = "";
        }
        try {
            String nowTime = DateUtil.getCurrentDateTime();
            if (!StringUtils.isEmpty((CharSequence)am)) {
                if ("am1".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(24));
                    params.put("endTime", nowTime);
                }
                if ("am2".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(48));
                    params.put("endTime", nowTime);
                }
                if ("am3".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(72));
                    params.put("endTime", nowTime);
                }
                if ("am4".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(96));
                    params.put("endTime", nowTime);
                }
                if ("am5".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(120));
                    params.put("endTime", nowTime);
                }
                if ("am6".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(144));
                    params.put("endTime", nowTime);
                }
                if ("am7".equals(am)) {
                    params.put("startTime", DateUtil.beforeHourToNowDate(168));
                    params.put("endTime", nowTime);
                }
                model.addAttribute("am", (Object)am);
                return;
            }
            params.put("startTime", DateUtil.beforeHourToNowDate(24));
            params.put("endTime", nowTime);
            model.addAttribute("am", "am1");
        }
        catch (Exception e) {
            logger.error("\u67e5\u770b\u78c1\u76d8\u8bfb\u5199\u901f\u7387\u56fe\u8868\u7ec4\u88c5\u65e5\u671f\u67e5\u8be2\u6761\u4ef6\u9519\u8bef", (Throwable)e);
        }
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.diskIoStateMapper.deleteByDate(map);
    }

    public DiskIoState selectMaxByHostname(Map<String, Object> params) throws Exception {
        return this.diskIoStateMapper.selectMaxByHostname(params);
    }

    public void setWarnValue(SystemInfo systemInfo, Model model) {
        try {
            Double diskIoSpeedWarnVal = this.mailConfig.getDiskIoSpeedWarnVal();
            HostWarnDiy hostWarnDiyDto = StaticKeys.HOST_WARN_MAP.get(systemInfo.getHostname());
            model.addAttribute("diskIoSpeedWarnVal", "no");
            if (null != hostWarnDiyDto && null != hostWarnDiyDto.getDiskIoSpeedWarnVal()) {
                diskIoSpeedWarnVal = hostWarnDiyDto.getDiskIoSpeedWarnVal();
                if ("yes".equals(hostWarnDiyDto.getDiskIoSpeedWarnMail()) && "true".equals(this.mailConfig.getDiskIoSpeedWarnMail())) {
                    model.addAttribute("diskIoSpeedWarnVal", (Object)(diskIoSpeedWarnVal + "MB/s"));
                }
            } else if ("true".equals(this.mailConfig.getDiskIoSpeedWarnMail())) {
                model.addAttribute("diskIoSpeedWarnVal", (Object)(diskIoSpeedWarnVal + "MB/s"));
            }
        }
        catch (Exception e) {
            logger.error("\u78c1\u76d8IO\u901f\u7387\u544a\u8b66\u503c\u5c55\u73b0\u8bbe\u7f6e\u9519\u8bef", (Throwable)e);
        }
    }

    public void findMaxVal(List<DiskIoState> diskIoStateList, Model model) {
        double maxBytesVal = 1.0;
        double maxDiskIOCountVal = 1.0;
        Double maxReadIoAvg = 0.0;
        Double avgReadIoAvg = 0.0;
        Double minReadIoAvg = 99999.0;
        Double sumReadIoAvg = 0.0;
        Double maxWriteIoAvg = 0.0;
        Double minWriteIoAvg = 99999.0;
        Double avgWriteIoAvg = 0.0;
        Double sumWriteIoAvg = 0.0;
        Double maxReadIoCountAvg = 0.0;
        Double avgReadIoCountAvg = 0.0;
        Double minReadIoCountAvg = 999999.0;
        Double sumReadIoCountAvg = 0.0;
        Double maxWriteIoCountAvg = 0.0;
        Double minWriteIoCountAvg = 999999.0;
        Double avgWriteIoCountAvg = 0.0;
        Double sumWriteIoCountAvg = 0.0;
        if (!CollectionUtil.isEmpty(diskIoStateList)) {
            for (DiskIoState diskIoState : diskIoStateList) {
                try {
                    diskIoState.setHostname("");
                    if (null != diskIoState.getReadIoAvgDouble() && diskIoState.getReadIoAvgDouble() > maxBytesVal) {
                        maxBytesVal = diskIoState.getReadIoAvgDouble();
                    }
                    if (null != diskIoState.getWriteIoAvgDouble() && diskIoState.getWriteIoAvgDouble() > maxBytesVal) {
                        maxBytesVal = diskIoState.getWriteIoAvgDouble();
                    }
                    if (null != diskIoState.getReadIoCountAvgDouble() && diskIoState.getReadIoCountAvgDouble() > maxDiskIOCountVal) {
                        maxDiskIOCountVal = diskIoState.getReadIoCountAvgDouble();
                    }
                    if (null != diskIoState.getWriteIoCountAvgDouble() && diskIoState.getWriteIoCountAvgDouble() > maxDiskIOCountVal) {
                        maxDiskIOCountVal = diskIoState.getWriteIoCountAvgDouble();
                    }
                    if (null != diskIoState.getReadIoAvgDouble()) {
                        if (diskIoState.getReadIoAvgDouble() > maxReadIoAvg) {
                            maxReadIoAvg = diskIoState.getReadIoAvgDouble();
                        }
                        if (diskIoState.getReadIoAvgDouble() < minReadIoAvg) {
                            minReadIoAvg = diskIoState.getReadIoAvgDouble();
                        }
                        sumReadIoAvg = sumReadIoAvg + diskIoState.getReadIoAvgDouble();
                    }
                    if (null != diskIoState.getWriteIoAvgDouble()) {
                        if (diskIoState.getWriteIoAvgDouble() > maxWriteIoAvg) {
                            maxWriteIoAvg = diskIoState.getWriteIoAvgDouble();
                        }
                        if (diskIoState.getWriteIoAvgDouble() < minWriteIoAvg) {
                            minWriteIoAvg = diskIoState.getWriteIoAvgDouble();
                        }
                        sumWriteIoAvg = sumWriteIoAvg + diskIoState.getWriteIoAvgDouble();
                    }
                    if (null != diskIoState.getReadIoCountAvgDouble()) {
                        if (diskIoState.getReadIoCountAvgDouble() > maxReadIoCountAvg) {
                            maxReadIoCountAvg = diskIoState.getReadIoCountAvgDouble();
                        }
                        if (diskIoState.getWriteIoCountAvgDouble() < minReadIoCountAvg) {
                            minReadIoCountAvg = diskIoState.getWriteIoCountAvgDouble();
                        }
                        sumReadIoCountAvg = sumReadIoCountAvg + diskIoState.getReadIoCountAvgDouble();
                    }
                    if (null == diskIoState.getWriteIoCountAvgDouble()) continue;
                    if (diskIoState.getWriteIoCountAvgDouble() > maxWriteIoCountAvg) {
                        maxWriteIoCountAvg = diskIoState.getWriteIoCountAvgDouble();
                    }
                    if (diskIoState.getWriteIoCountAvgDouble() < minWriteIoCountAvg) {
                        minWriteIoCountAvg = diskIoState.getWriteIoCountAvgDouble();
                    }
                    sumWriteIoCountAvg = sumWriteIoCountAvg + diskIoState.getWriteIoCountAvgDouble();
                }
                catch (Exception e) {
                    logger.error("\u8bbe\u7f6e\u78c1\u76d8IO\u8bfb\u5199\u901f\u7387\u56fe\u8868\u526f\u6807\u9898\u9519\u8bef", (Throwable)e);
                }
            }
        }
        model.addAttribute("diskIoStateBytMaxVal", (Object)Math.ceil(maxBytesVal));
        model.addAttribute("diskIoStateCountMaxVal", (Object)Math.ceil(maxDiskIOCountVal));
        if (diskIoStateList.size() > 0) {
            avgReadIoAvg = sumReadIoAvg / (double)diskIoStateList.size();
            avgWriteIoAvg = sumWriteIoAvg / (double)diskIoStateList.size();
            avgReadIoCountAvg = sumReadIoCountAvg / (double)diskIoStateList.size();
            avgWriteIoCountAvg = sumWriteIoCountAvg / (double)diskIoStateList.size();
        } else {
            avgReadIoAvg = 0.0;
            avgWriteIoAvg = 0.0;
            minReadIoAvg = 0.0;
            minWriteIoAvg = 0.0;
            avgReadIoCountAvg = 0.0;
            avgWriteIoCountAvg = 0.0;
            minReadIoCountAvg = 0.0;
            minWriteIoCountAvg = 0.0;
        }
        SubtitleDto diskIoStateReadSubtitleDto = new SubtitleDto();
        diskIoStateReadSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgReadIoAvg, 2) + "MB/s");
        diskIoStateReadSubtitleDto.setMaxValue(maxReadIoAvg + "MB/s");
        diskIoStateReadSubtitleDto.setMinValue(minReadIoAvg + "MB/s");
        model.addAttribute("diskIoStateReadSubtitleDto", (Object)diskIoStateReadSubtitleDto);
        SubtitleDto diskIoStateWriteSubtitleDto = new SubtitleDto();
        diskIoStateWriteSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgWriteIoAvg, 2) + "MB/s");
        diskIoStateWriteSubtitleDto.setMaxValue(maxWriteIoAvg + "MB/s");
        diskIoStateWriteSubtitleDto.setMinValue(minWriteIoAvg + "MB/s");
        model.addAttribute("diskIoStateWriteSubtitleDto", (Object)diskIoStateWriteSubtitleDto);
        SubtitleDto diskIoCountReadSubtitleDto = new SubtitleDto();
        diskIoCountReadSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgReadIoCountAvg, 2) + "\u6b21");
        diskIoCountReadSubtitleDto.setMaxValue(maxReadIoCountAvg + "\u6b21");
        diskIoCountReadSubtitleDto.setMinValue(minReadIoCountAvg + "\u6b21");
        model.addAttribute("diskIoCountReadSubtitleDto", (Object)diskIoCountReadSubtitleDto);
        SubtitleDto diskIoCountWriteSubtitleDto = new SubtitleDto();
        diskIoCountWriteSubtitleDto.setAvgValue(FormatUtil.formatDouble(avgWriteIoCountAvg, 2) + "\u6b21");
        diskIoCountWriteSubtitleDto.setMaxValue(maxWriteIoCountAvg + "\u6b21");
        diskIoCountWriteSubtitleDto.setMinValue(minWriteIoCountAvg + "\u6b21");
        model.addAttribute("diskIoCountWriteSubtitleDto", (Object)diskIoCountWriteSubtitleDto);
    }
}

