/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.dto.ChartInfo;
import com.wgcloud.dto.NetworkInfoDto;
import com.wgcloud.entity.AccountInfo;
import com.wgcloud.entity.AppExceptionInfo;
import com.wgcloud.entity.DiskState;
import com.wgcloud.entity.SystemInfo;
import com.wgcloud.service.AccountInfoService;
import com.wgcloud.service.SystemInfoService;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.license.LicenseUtil;
import com.wgcloud.util.redis.RedisDataUtil;
import com.wgcloud.util.staticvar.StaticKeys;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

public class HostUtil {
    private static final Logger logger = LoggerFactory.getLogger(HostUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    private static AccountInfoService accountInfoService = ApplicationContextHelper.getBean(AccountInfoService.class);
    private static SystemInfoService systemInfoService = ApplicationContextHelper.getBean(SystemInfoService.class);
    public static List<DiskState> DISK_LIST_COMPUTE = Collections.synchronizedList(new ArrayList());

    public static void addAccountListModel(Model model) {
        try {
            if (!"true".equals(commonConfig.getUserInfoManage())) {
                return;
            }
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<AccountInfo> accountInfoList = accountInfoService.selectAllByParams(params);
            if (!CollectionUtil.isEmpty(accountInfoList)) {
                model.addAttribute("accountList", accountInfoList);
            }
        }
        catch (Exception e) {
            logger.error("addAccountListModel", (Throwable)e);
        }
    }

    public static void addAccountquery(HttpServletRequest request, Map<String, Object> params) {
        if (null == request || null == params) {
            return;
        }
        AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
        if (!"admin".equals(accountInfo.getRole()) && !"guest".equals(accountInfo.getRole()) && null != accountInfo.getAccount()) {
            params.put("account", accountInfo.getAccount());
        }
    }

    public static void addAccountqueryDaping(HttpServletRequest request, Map<String, Object> params) {
        if (null == request || null == params) {
            return;
        }
        if (!"1".equals(request.getParameter("viewDapingNowAccount"))) {
            return;
        }
        AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
        if (!"admin".equals(accountInfo.getRole()) && !"guest".equals(accountInfo.getRole()) && null != accountInfo.getAccount()) {
            params.put("account", accountInfo.getAccount());
        }
    }

    public static void setDiskSumPer(List<DiskState> diskStates, SystemInfo systemInfo) {
        try {
            Double sumSize = 0.0;
            Double useSize = 0.0;
            for (DiskState diskState : diskStates) {
                if (StringUtils.isEmpty((CharSequence)diskState.getDiskSize()) || StringUtils.isEmpty((CharSequence)diskState.getUsed())) continue;
                sumSize = sumSize + Double.valueOf(diskState.getDiskSize().replace("G", ""));
                useSize = useSize + Double.valueOf(diskState.getUsed().replace("G", ""));
            }
            systemInfo.setDiskPer(0.0);
            systemInfo.setDiskSumSize("0G");
            if (sumSize != 0.0) {
                systemInfo.setDiskPer(FormatUtil.formatDouble(useSize / sumSize * 100.0, 2));
            }
            systemInfo.setDiskSumSize(FormatUtil.formatDouble(sumSize, 2) + "G");
        }
        catch (Exception e) {
            logger.error("\u8bbe\u7f6e\u78c1\u76d8\u603b\u4f7f\u7528\u7387\u9519\u8bef", (Throwable)e);
        }
    }

    public static void setDiskListSumSize(List<DiskState> diskStates) {
        try {
            DiskState diskStateSum = new DiskState();
            Double sumSize = 0.0;
            Double useSize = 0.0;
            Double availSize = 0.0;
            for (DiskState diskState : diskStates) {
                if (!StringUtils.isEmpty((CharSequence)diskState.getDiskSize())) {
                    sumSize = sumSize + Double.valueOf(diskState.getDiskSize().replace("G", ""));
                }
                if (!StringUtils.isEmpty((CharSequence)diskState.getUsed())) {
                    useSize = useSize + Double.valueOf(diskState.getUsed().replace("G", ""));
                }
                if (StringUtils.isEmpty((CharSequence)diskState.getAvail())) continue;
                availSize = availSize + Double.valueOf(diskState.getAvail().replace("G", ""));
            }
            diskStateSum.setUsePer("0%");
            if (sumSize != 0.0) {
                diskStateSum.setUsePer(FormatUtil.formatDouble(useSize / sumSize * 100.0, 2) + "%");
            }
            diskStateSum.setUsed(FormatUtil.formatDouble(useSize, 2) + "G");
            diskStateSum.setDiskSize(FormatUtil.formatDouble(sumSize, 2) + "G");
            diskStateSum.setAvail(FormatUtil.formatDouble(availSize, 2) + "G");
            diskStateSum.setFileSystem("\u603b\u8ba1");
            diskStates.add(diskStateSum);
        }
        catch (Exception e) {
            logger.error("\u8bbe\u7f6e\u78c1\u76d8\u603b\u8ba1\u9519\u8bef", (Throwable)e);
        }
    }

    public static void setSysImage(SystemInfo systemInfo) {
        if (!StringUtils.isEmpty((CharSequence)systemInfo.getPlatForm())) {
            String platForm = systemInfo.getPlatForm().toLowerCase();
            if (platForm.contains("windows")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/windows.png");
            } else if (platForm.contains("centos")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/centos.png");
            } else if (platForm.contains("hat")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/redhat.png");
            } else if (platForm.contains("ubuntu")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/ubuntu.png");
            } else if (platForm.contains("debian")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/debian.png");
            } else if (platForm.contains("darwin")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/darwin.png");
            } else if (platForm.contains("android")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/android.png");
            } else if (platForm.contains("suse")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/suse.png");
            } else if (platForm.contains("fedora")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/fedora.png");
            } else if (platForm.contains("freebsd")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/freebsd.png");
            } else if (platForm.contains("kylin")) {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/kylin.png");
            } else {
                systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/linux.png");
            }
        } else {
            systemInfo.setImage(StaticKeys.SERVER_SERVLET_CONTEXT_PATH + "/static/images/linux.png");
        }
    }

    public static void setSysFontAwesome(SystemInfo systemInfo) {
        if (!StringUtils.isEmpty((CharSequence)systemInfo.getPlatForm())) {
            String platForm = systemInfo.getPlatForm().toLowerCase();
            if (platForm.contains("windows")) {
                systemInfo.setImage("<i class=\"fa-brands fa-windows\"></i>");
            } else if (platForm.contains("centos")) {
                systemInfo.setImage("<i class=\"fa-brands fa-centos\"></i>");
            } else if (platForm.contains("hat")) {
                systemInfo.setImage("<i class=\"fa-brands fa-redhat\"></i>");
            } else if (platForm.contains("ubuntu")) {
                systemInfo.setImage("<i class=\"fa-brands fa-ubuntu\"></i>");
            } else if (platForm.contains("debian")) {
                systemInfo.setImage("<i class=\"fa-brands fa-debian\"></i>");
            } else if (platForm.contains("darwin")) {
                systemInfo.setImage("<i class=\"fa-brands fa-apple\"></i>");
            } else if (platForm.contains("android")) {
                systemInfo.setImage("<i class=\"fa-brands fa-android\"></i>");
            } else if (platForm.contains("suse")) {
                systemInfo.setImage("<i class=\"fa-brands fa-suse\"></i>");
            } else if (platForm.contains("fedora")) {
                systemInfo.setImage("<i class=\"fa-brands fa-fedora\"></i>");
            } else if (platForm.contains("freebsd")) {
                systemInfo.setImage("<i class=\"fa-brands fa-freebsd\"></i>");
            } else {
                systemInfo.setImage("<i style=\"color:#343a40\" class=\"fa-brands fa-linux\"></i>");
            }
        } else {
            systemInfo.setImage("<i style=\"color:#343a40\" class=\"fa-brands fa-linux\"></i>");
        }
    }

    public static List<ChartInfo> getSystemTypeList(List<SystemInfo> systemInfoList) {
        ArrayList<ChartInfo> chartInfoList = new ArrayList<ChartInfo>();
        Map<String, Integer> map = HostUtil.getSystemTypeMap(systemInfoList);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            ChartInfo chartInfo = new ChartInfo();
            chartInfo.setItem(entry.getKey());
            chartInfo.setCount(entry.getValue());
            chartInfoList.add(chartInfo);
        }
        return chartInfoList;
    }

    public static Map<String, Integer> getSystemTypeMap(List<SystemInfo> systemInfoList) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (SystemInfo systemInfo : systemInfoList) {
            if (!StringUtils.isEmpty((CharSequence)systemInfo.getPlatForm())) {
                String platForm = systemInfo.getPlatForm().toLowerCase();
                if (platForm.contains("windows")) {
                    if (map.get("windows") != null) {
                        map.put("windows", (Integer)map.get("windows") + 1);
                        continue;
                    }
                    map.put("windows", 1);
                    continue;
                }
                if (platForm.contains("centos")) {
                    if (map.get("centos") != null) {
                        map.put("centos", (Integer)map.get("centos") + 1);
                        continue;
                    }
                    map.put("centos", 1);
                    continue;
                }
                if (platForm.contains("redhat")) {
                    if (map.get("redhat") != null) {
                        map.put("redhat", (Integer)map.get("redhat") + 1);
                        continue;
                    }
                    map.put("redhat", 1);
                    continue;
                }
                if (platForm.contains("ubuntu")) {
                    if (map.get("ubuntu") != null) {
                        map.put("ubuntu", (Integer)map.get("ubuntu") + 1);
                        continue;
                    }
                    map.put("ubuntu", 1);
                    continue;
                }
                if (platForm.contains("debian")) {
                    if (map.get("debian") != null) {
                        map.put("debian", (Integer)map.get("debian") + 1);
                        continue;
                    }
                    map.put("debian", 1);
                    continue;
                }
                if (platForm.contains("darwin")) {
                    if (map.get("macOS") != null) {
                        map.put("macOS", (Integer)map.get("macOS") + 1);
                        continue;
                    }
                    map.put("macOS", 1);
                    continue;
                }
                if (platForm.contains("android")) {
                    if (map.get("android") != null) {
                        map.put("android", (Integer)map.get("android") + 1);
                        continue;
                    }
                    map.put("android", 1);
                    continue;
                }
                if (platForm.contains("kylin")) {
                    if (map.get("kylin") != null) {
                        map.put("kylin", (Integer)map.get("kylin") + 1);
                        continue;
                    }
                    map.put("kylin", 1);
                    continue;
                }
                if (map.get("\u5176\u4ed6") != null) {
                    map.put("\u5176\u4ed6", (Integer)map.get("\u5176\u4ed6") + 1);
                    continue;
                }
                map.put("\u5176\u4ed6", 1);
                continue;
            }
            if (map.get("\u5176\u4ed6") != null) {
                map.put("\u5176\u4ed6", (Integer)map.get("\u5176\u4ed6") + 1);
                continue;
            }
            map.put("\u5176\u4ed6", 1);
        }
        return map;
    }

    public static String addRemark(String hostname) {
        if (StringUtils.isEmpty((CharSequence)hostname)) {
            return "";
        }
        String remark = StaticKeys.HOST_MAP.get(hostname);
        remark = !StringUtils.isEmpty((CharSequence)remark) ? "(" + remark + ")" : "";
        return remark;
    }

    public static String getHostState(String hostname) {
        if (StringUtils.isEmpty((CharSequence)hostname)) {
            return "1";
        }
        String state = StaticKeys.HOST_STATE_MAP.get(hostname);
        if (!StringUtils.isEmpty((CharSequence)state) && "2".equals(state)) {
            return "2";
        }
        return "1";
    }

    public static String getAccount(String hostname) {
        if (StringUtils.isEmpty((CharSequence)hostname)) {
            return "";
        }
        String account = StaticKeys.HOST_ACCOUNT_MAP.get(hostname);
        return account;
    }

    public static AccountInfo getAccountByRequest(HttpServletRequest request) {
        AccountInfo accountInfo = (AccountInfo)request.getSession().getAttribute("LOGIN_KEY");
        if (accountInfo == null) {
            return new AccountInfo();
        }
        return accountInfo;
    }

    public static void clearCacheMap() {
        logger.info("\u6e05\u7a7a\u7f13\u5b58\u4e3b\u673a\u7f13\u5b58\u7684map");
        StaticKeys.HOST_MAP.clear();
        StaticKeys.HOST_ACCOUNT_MAP.clear();
        StaticKeys.HOST_STATE_MAP.clear();
        try {
            HashMap<String, Object> params = new HashMap<String, Object>();
            List<SystemInfo> list = systemInfoService.selectAllByParamsForTask(params);
            HostUtil.initHostCacheMap(list);
        }
        catch (Exception e) {
            logger.error("clearCacheMap\u9519\u8bef", (Throwable)e);
        }
    }

    public static void initHostCacheMap(List<SystemInfo> list) throws Exception {
        try {
            if (!CollectionUtil.isEmpty(list)) {
                for (SystemInfo systemInfo : list) {
                    if (!StringUtils.isEmpty((CharSequence)systemInfo.getRemark())) {
                        StaticKeys.HOST_MAP.put(systemInfo.getHostname(), systemInfo.getRemark());
                    }
                    if (!StringUtils.isEmpty((CharSequence)systemInfo.getState())) {
                        StaticKeys.HOST_STATE_MAP.put(systemInfo.getHostname(), systemInfo.getState());
                    }
                    if (!"true".equals(commonConfig.getUserInfoManage()) || StringUtils.isEmpty((CharSequence)systemInfo.getAccount()) || !StaticKeys.LICENSE_STATE.equals("1")) continue;
                    StaticKeys.HOST_ACCOUNT_MAP.put(systemInfo.getHostname(), systemInfo.getAccount());
                }
            }
        }
        catch (Exception e) {
            logger.error("initHostCacheMap\u9519\u8bef", (Throwable)e);
        }
    }

    public static <T> List<T> compressChartListData(List<T> list, Model model) {
        Integer maxValue = commonConfig.getChartDataMaxShowValue();
        if (list.size() <= maxValue || maxValue == 0) {
            return list;
        }
        Integer radixValue = list.size() / maxValue;
        if ((radixValue = Integer.valueOf(radixValue + 1)) < 2) {
            return list;
        }
        ArrayList<T> resultList = new ArrayList<T>();
        for (int i = 0; i < list.size(); ++i) {
            if (i % radixValue != 0) continue;
            resultList.add(list.get(i));
        }
        model.addAttribute("compressDataMsg", (Object)("\u7cfb\u7edf\u5df2\u5bf9\u56fe\u8868\u6570\u636e\u8fdb\u884c\u538b\u7f29\u5747\u8861\u5316\u5904\u7406\uff0c\u5904\u7406\u524d" + list.size() + "\u6761\uff0c\u5904\u7406\u540e" + resultList.size() + "\u6761"));
        return resultList;
    }

    public static void addDapingTipMsg(HttpServletRequest request, Model model) {
        Integer refreshTimes = commonConfig.getDapingRefreshTimes();
        model.addAttribute("dapingRefreshTimes", (Object)(refreshTimes * 1000));
        String msg = "\u5f53\u524d\u5df2\u663e\u793a\u6240\u6709\u76d1\u63a7\u8d44\u6e90\uff0c" + refreshTimes + "\u79d2\u540e\u4f1a\u81ea\u52a8\u5237\u65b0";
        if (!"1".equals(request.getParameter("viewDapingNowAccount"))) {
            model.addAttribute("tipInfoMsg", (Object)msg);
            return;
        }
        AccountInfo accountInfo = HostUtil.getAccountByRequest(request);
        if (!"admin".equals(accountInfo.getRole()) && !"guest".equals(accountInfo.getRole()) && null != accountInfo.getAccount()) {
            msg = "\u5f53\u524d\u5df2\u663e\u793a\u3010" + accountInfo.getAccount() + "\u3011\u6240\u6709\u76d1\u63a7\u8d44\u6e90\uff0c" + refreshTimes + "\u79d2\u540e\u4f1a\u81ea\u52a8\u5237\u65b0";
        }
        model.addAttribute("tipInfoMsg", (Object)msg);
    }

    public static void viewAllProcessHandler(AppExceptionInfo appExceptionInfo, Model model, String id) throws Exception {
        SystemInfo systemInfo = systemInfoService.selectById(id);
        model.addAttribute("systemInfo", (Object)systemInfo);
        HostUtil.setSysFontAwesome(systemInfo);
        if (!LicenseUtil.checkEnterpriseVersion()) {
            model.addAttribute("tipInfo", "\u63d0\u793a: \u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u53ef\u4ee5\u67e5\u770b\u5168\u91cf\u8fd0\u884c\u8fdb\u7a0b\u54e6");
        }
        model.addAttribute("appExceptionInfo", (Object)appExceptionInfo);
        if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            HostUtil.getAllProcessFromRedis(systemInfo, appExceptionInfo, model);
            return;
        }
        List cacheList = (List)StaticKeys.HOST_ALL_PROCESS.get(systemInfo.getHostname());
        HostUtil.sortAllProcess(appExceptionInfo, cacheList);
        ArrayList<AppExceptionInfo> resultList = new ArrayList<AppExceptionInfo>();
        for (AppExceptionInfo app : (java.util.List<AppExceptionInfo>)cacheList) {
            AppExceptionInfo appExceptionInfoTmp = new AppExceptionInfo();
            BeanUtil.copyProperties((Object)app, (Object)appExceptionInfoTmp, (boolean)true);
            appExceptionInfoTmp.setWritesBytes(FormatUtil.mToG(appExceptionInfoTmp.getWritesBytes()));
            appExceptionInfoTmp.setReadBytes(FormatUtil.mToG(appExceptionInfoTmp.getReadBytes()));
            resultList.add(appExceptionInfoTmp);
        }
        model.addAttribute("cacheList", resultList);
        if (null != cacheList) {
            model.addAttribute("cacheListSize", (Object)(" (" + cacheList.size() + ")"));
        }
        String caijiDateTime = (String)StaticKeys.HOST_ALL_PROCESS.get(systemInfo.getHostname() + "_DATETIME");
        model.addAttribute("caijiDateTime", (Object)caijiDateTime);
    }

    private static void sortAllProcess(final AppExceptionInfo appExceptionInfo, List cacheList) {
        if (!StringUtils.isEmpty((CharSequence)appExceptionInfo.getOrderBy())) {
            if ("appName".equals(appExceptionInfo.getOrderBy())) {
                Collections.sort(cacheList, new Comparator<AppExceptionInfo>(){

                    @Override
                    public int compare(AppExceptionInfo o1, AppExceptionInfo o2) {
                        if (o1.getAppName().compareTo(o2.getAppName()) < 0) {
                            if ("ASC".equals(appExceptionInfo.getOrderType())) {
                                return -1;
                            }
                            return 1;
                        }
                        if (o1.getAppName().compareTo(o2.getAppName()) > 0) {
                            if ("ASC".equals(appExceptionInfo.getOrderType())) {
                                return 1;
                            }
                            return -1;
                        }
                        return 0;
                    }
                });
            }
            if ("gatherPid".equals(appExceptionInfo.getOrderBy())) {
                Collections.sort(cacheList, new Comparator<AppExceptionInfo>(){

                    @Override
                    public int compare(AppExceptionInfo o1, AppExceptionInfo o2) {
                        Integer o2Value;
                        Integer o1Value = Integer.valueOf(o1.getGatherPid());
                        if (o1Value.compareTo(o2Value = Integer.valueOf(o2.getGatherPid())) < 0) {
                            if ("ASC".equals(appExceptionInfo.getOrderType())) {
                                return -1;
                            }
                            return 1;
                        }
                        if (o1Value.compareTo(o2Value) > 0) {
                            if ("ASC".equals(appExceptionInfo.getOrderType())) {
                                return 1;
                            }
                            return -1;
                        }
                        return 0;
                    }
                });
            }
            if ("appTimes".equals(appExceptionInfo.getOrderBy())) {
                Collections.sort(cacheList, new Comparator<AppExceptionInfo>(){

                    @Override
                    public int compare(AppExceptionInfo o1, AppExceptionInfo o2) {
                        try {
                            Date o1Value = DateUtil.getDate(o1.getAppTimes());
                            Date o2Value = DateUtil.getDate(o2.getAppTimes());
                        if (o1Value.compareTo(o2Value) < 0) {
                            if ("ASC".equals(appExceptionInfo.getOrderType())) {
                                return -1;
                            }
                            return 1;
                        }
                        if (o1Value.compareTo(o2Value) > 0) {
                            if ("ASC".equals(appExceptionInfo.getOrderType())) {
                                return 1;
                            }
                            return -1;
                        }
                        return 0;
                        } catch (java.text.ParseException pe) {
                            return 0;
                        }
                    }
                });
            }
            if ("cpuPer".equals(appExceptionInfo.getOrderBy())) {
                Collections.sort(cacheList, new Comparator<AppExceptionInfo>(){

                    @Override
                    public int compare(AppExceptionInfo o1, AppExceptionInfo o2) {
                        Double o2Value;
                        Double o1Value = (double)o1.getCpuPer();
                        if (o1Value.compareTo(o2Value = Double.valueOf(o2.getCpuPer())) < 0) {
                            if ("ASC".equals(appExceptionInfo.getOrderType())) {
                                return -1;
                            }
                            return 1;
                        }
                        if (o1Value.compareTo(o2Value) > 0) {
                            if ("ASC".equals(appExceptionInfo.getOrderType())) {
                                return 1;
                            }
                            return -1;
                        }
                        return 0;
                    }
                });
            }
            if ("memPer".equals(appExceptionInfo.getOrderBy())) {
                Collections.sort(cacheList, new Comparator<AppExceptionInfo>(){

                    @Override
                    public int compare(AppExceptionInfo o1, AppExceptionInfo o2) {
                        Double o2Value;
                        Double o1Value = (double)o1.getMemPer();
                        if (o1Value.compareTo(o2Value = Double.valueOf(o2.getMemPer())) < 0) {
                            if ("ASC".equals(appExceptionInfo.getOrderType())) {
                                return -1;
                            }
                            return 1;
                        }
                        if (o1Value.compareTo(o2Value) > 0) {
                            if ("ASC".equals(appExceptionInfo.getOrderType())) {
                                return 1;
                            }
                            return -1;
                        }
                        return 0;
                    }
                });
            }
        }
    }

    public static void getAllProcessFromRedis(SystemInfo systemInfo, AppExceptionInfo appExceptionInfo, Model model) {
        try {
            String jsonString = RedisDataUtil.getValue(systemInfo.getHostname() + "_ALL_PROCESS");
            if (!StringUtils.isEmpty((CharSequence)jsonString)) {
                JSONArray jsonArray = JSONUtil.parseArray((String)jsonString);
                List cacheList = JSONUtil.toList((JSONArray)jsonArray, AppExceptionInfo.class);
                HostUtil.sortAllProcess(appExceptionInfo, cacheList);
                ArrayList<AppExceptionInfo> resultList = new ArrayList<AppExceptionInfo>();
                for (AppExceptionInfo app : (java.util.List<AppExceptionInfo>)cacheList) {
                    AppExceptionInfo appExceptionInfoTmp = new AppExceptionInfo();
                    BeanUtil.copyProperties((Object)app, (Object)appExceptionInfoTmp, (boolean)true);
                    appExceptionInfoTmp.setWritesBytes(FormatUtil.mToG(appExceptionInfoTmp.getWritesBytes()));
                    appExceptionInfoTmp.setReadBytes(FormatUtil.mToG(appExceptionInfoTmp.getReadBytes()));
                    resultList.add(appExceptionInfoTmp);
                }
                model.addAttribute("cacheList", resultList);
                if (null != jsonArray) {
                    model.addAttribute("cacheListSize", (Object)(" (" + jsonArray.size() + ")"));
                }
                model.addAttribute("caijiDateTime", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_ALL_PROCESS" + "_DATETIME"));
            }
        }
        catch (Exception e) {
            logger.error("\u4eceredis\u4e2d\u83b7\u53d6\u5168\u91cf\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
    }

    public static void setAllProcessHandler(String bindIp, List<AppExceptionInfo> willCacheList, Date date) {
        if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            StaticKeys.HOST_ALL_PROCESS.put(bindIp, willCacheList);
            StaticKeys.HOST_ALL_PROCESS.put(bindIp + "_DATETIME", DateUtil.getDateTimeString(date));
        } else {
            RedisDataUtil.setValue(bindIp + "_ALL_PROCESS", JSONUtil.toJsonStr(willCacheList));
            RedisDataUtil.setValue(bindIp + "_ALL_PROCESS" + "_DATETIME", DateUtil.getDateTimeString(date));
        }
    }

    public static void setImportDataHandler(String key, String data, Date date) {
        if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            StaticKeys.HOST_IMPORT_DATA.put(key, data);
            StaticKeys.HOST_IMPORT_DATA.put(key + "_DATETIME", DateUtil.getDateTimeString(date));
        } else {
            RedisDataUtil.setValue(key, data);
            RedisDataUtil.setValue(key + "_DATETIME", DateUtil.getDateTimeString(date));
        }
    }

    public static void viewImportInfoHandler(Model model, String id) throws Exception {
        SystemInfo systemInfo = systemInfoService.selectById(id);
        model.addAttribute("systemInfo", (Object)systemInfo);
        HostUtil.setSysFontAwesome(systemInfo);
        if (!LicenseUtil.checkEnterpriseVersion()) {
            model.addAttribute("tipInfo", "\u63d0\u793a: \u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u53ef\u4ee5\u67e5\u770bGPU\u3001\u9632\u706b\u5899\u3001CRONTAB\u4fe1\u606f\u3001\u4e2a\u6027\u5316\u91c7\u96c6\u54e6");
        }
        if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            model.addAttribute("gpuInfo", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_GPU"));
            model.addAttribute("gpuInfoDateTime", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_GPU" + "_DATETIME"));
            model.addAttribute("fireWallInfo", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_FIREWALL"));
            model.addAttribute("fireWallDateTime", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_FIREWALL" + "_DATETIME"));
            model.addAttribute("crontabInfo", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_CRONTAB"));
            model.addAttribute("crontabDateTime", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_CRONTAB" + "_DATETIME"));
            model.addAttribute("likeShellInfo", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_LIKE_SHELL"));
            model.addAttribute("likeShellDateTime", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_LIKE_SHELL" + "_DATETIME"));
            return;
        }
        model.addAttribute("gpuInfo", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_GPU"));
        model.addAttribute("gpuInfoDateTime", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_GPU" + "_DATETIME"));
        model.addAttribute("fireWallInfo", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_FIREWALL"));
        model.addAttribute("fireWallDateTime", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_FIREWALL" + "_DATETIME"));
        model.addAttribute("crontabInfo", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_CRONTAB"));
        model.addAttribute("crontabDateTime", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_CRONTAB" + "_DATETIME"));
        model.addAttribute("likeShellInfo", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_LIKE_SHELL"));
        model.addAttribute("likeShellDateTime", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_LIKE_SHELL" + "_DATETIME"));
    }

    public static void viewAllPortInfoHandler(Model model, String id) throws Exception {
        SystemInfo systemInfo = systemInfoService.selectById(id);
        model.addAttribute("systemInfo", (Object)systemInfo);
        HostUtil.setSysFontAwesome(systemInfo);
        if (!LicenseUtil.checkEnterpriseVersion()) {
            model.addAttribute("tipInfo", "\u63d0\u793a: \u5347\u7ea7\u5230\u4f01\u4e1a\u7248\u53ef\u4ee5\u67e5\u770b\u4e3b\u673a\u7684\u5168\u91cf\u7aef\u53e3\u5217\u8868\u54e6");
        }
        if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            model.addAttribute("allPortInfo", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_ALLPORT"));
            model.addAttribute("allPortDateTime", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_ALLPORT" + "_DATETIME"));
            return;
        }
        model.addAttribute("allPortInfo", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_ALLPORT"));
        model.addAttribute("allPortDateTime", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_ALLPORT" + "_DATETIME"));
    }

    public static void viewAllNetworkHandler(Model model, String hostname) {
        try {
            String jsonString = "";
            if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                jsonString = RedisDataUtil.getValue(hostname + "_ALL_NETWORK");
            } else if (null != StaticKeys.HOST_IMPORT_DATA.get(hostname + "_ALL_NETWORK")) {
                jsonString = StaticKeys.HOST_IMPORT_DATA.get(hostname + "_ALL_NETWORK").toString();
            }
            if (!StringUtils.isEmpty((CharSequence)jsonString)) {
                JSONArray jsonArray = JSONUtil.parseArray((String)jsonString);
                List allNetworkList = JSONUtil.toList((JSONArray)jsonArray, NetworkInfoDto.class);
                model.addAttribute("allNetworkList", (Object)allNetworkList);
            }
        }
        catch (Exception e) {
            logger.error("\u6839\u636eIP\u67e5\u8be2\u670d\u52a1\u5668\u7684\u5168\u90e8\u7f51\u5361\u540d\u79f0\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
    }

    public static void viewIfconfigInfoHandler(Model model, String id) throws Exception {
        SystemInfo systemInfo = systemInfoService.selectById(id);
        model.addAttribute("systemInfo", (Object)systemInfo);
        HostUtil.setSysFontAwesome(systemInfo);
        if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            model.addAttribute("ifconfigInfo", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_IFCONFIG"));
            model.addAttribute("ifconfigInfoDateTime", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_IFCONFIG" + "_DATETIME"));
            return;
        }
        model.addAttribute("ifconfigInfo", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_IFCONFIG"));
        model.addAttribute("ifconfigInfoDateTime", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_IFCONFIG" + "_DATETIME"));
    }

    public static void viewLastUserInfoHandler(Model model, String id) {
        try {
            SystemInfo systemInfo = systemInfoService.selectById(id);
            if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                model.addAttribute("hostUsersList", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_LASTUSER"));
                model.addAttribute("hostUsersListDateTime", (Object)RedisDataUtil.getValue(systemInfo.getHostname() + "_LASTUSER" + "_DATETIME"));
                return;
            }
            model.addAttribute("hostUsersList", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_LASTUSER"));
            model.addAttribute("hostUsersListDateTime", StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_LASTUSER" + "_DATETIME"));
        }
        catch (Exception e) {
            logger.error("\u6839\u636eIP\u67e5\u8be2\u670d\u52a1\u5668\u7528\u6237\u7684\u6700\u8fd1\u767b\u5f55\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
    }

    public static void viewBiosBoardHandler(SystemInfo systemInfo) {
        try {
            String hostname = systemInfo.getHostname();
            String biosInfo = "";
            if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                biosInfo = RedisDataUtil.getValue(hostname + "_HOST_BIOS");
            } else if (null != StaticKeys.HOST_IMPORT_DATA.get(hostname + "_HOST_BIOS")) {
                biosInfo = StaticKeys.HOST_IMPORT_DATA.get(hostname + "_HOST_BIOS").toString();
            }
            if (StringUtils.isEmpty((CharSequence)biosInfo)) {
                biosInfo = "\u65e0";
            }
            systemInfo.setBiosInfo(biosInfo);
            String baseBoard = "";
            if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                baseBoard = RedisDataUtil.getValue(hostname + "_BASE_BOARD");
            } else if (null != StaticKeys.HOST_IMPORT_DATA.get(hostname + "_BASE_BOARD")) {
                baseBoard = StaticKeys.HOST_IMPORT_DATA.get(hostname + "_BASE_BOARD").toString();
            }
            if (StringUtils.isEmpty((CharSequence)baseBoard)) {
                baseBoard = "\u65e0";
            }
            systemInfo.setBaseBoard(baseBoard);
        }
        catch (Exception e) {
            logger.error("\u6839\u636eIP\u67e5\u8be2\u670d\u52a1\u5668\u7684bios\u548c\u4e3b\u677f\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
    }

    public static void viewWinServicesHandler(Model model, String id) throws Exception {
        SystemInfo systemInfo = systemInfoService.selectById(id);
        model.addAttribute("systemInfo", (Object)systemInfo);
        HostUtil.setSysFontAwesome(systemInfo);
        String winServicesData = "";
        String winServicesDateTime = "";
        if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            winServicesData = RedisDataUtil.getValue(systemInfo.getHostname() + "_WIN_SERVICES");
            winServicesDateTime = RedisDataUtil.getValue(systemInfo.getHostname() + "_WIN_SERVICES" + "_DATETIME");
        } else {
            if (null != StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_WIN_SERVICES")) {
                winServicesData = StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_WIN_SERVICES").toString();
            }
            if (null != StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_WIN_SERVICES" + "_DATETIME")) {
                winServicesDateTime = StaticKeys.HOST_IMPORT_DATA.get(systemInfo.getHostname() + "_WIN_SERVICES" + "_DATETIME").toString();
            }
        }
        ArrayList<String> winServicesList = new ArrayList();
        if (!StringUtils.isEmpty((CharSequence)winServicesData)) {
            winServicesList = new ArrayList<String>(Arrays.asList(winServicesData.split(",")));
        }
        model.addAttribute("winServicesList", winServicesList);
        model.addAttribute("winServicesDateTime", (Object)winServicesDateTime);
        model.addAttribute("cacheListSize", (Object)winServicesList.size());
    }
}

