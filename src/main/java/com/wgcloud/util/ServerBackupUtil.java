/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.json.JSONObject;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.DbInfo;
import com.wgcloud.entity.DceInfo;
import com.wgcloud.entity.FtpInfo;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.entity.SnmpDeepInfo;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.util.IpUtil;
import com.wgcloud.util.redis.RedisDataUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerBackupUtil {
    private static final Logger logger = LoggerFactory.getLogger(ServerBackupUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    public static List<String> DB_INFO_ID_LIST = Collections.synchronizedList(new ArrayList());
    public static List<String> DCE_INFO_ID_LIST = Collections.synchronizedList(new ArrayList());
    public static List<String> SNMP_INFO_ID_LIST = Collections.synchronizedList(new ArrayList());
    public static List<String> HEATH_MONITOR_ID_LIST = Collections.synchronizedList(new ArrayList());
    public static List<String> FTP_ID_LIST = Collections.synchronizedList(new ArrayList());
    private static final String REDIS_SERVER_BACKUP_PREFIX = "SERVER_BACKUP_";
    public static List<String> SERVER_BACKUP_IP_LIST = Collections.synchronizedList(new ArrayList());
    private static final String REDIS_SERVER_BACKUP_BINDIP_LIST = "REDIS_SERVER_BACKUP_BINDIP_LIST";

    public static void clearCacheIdList() {
        logger.info("\u6e05\u7a7aserver-backup\u8282\u70b9\u5904\u7406\u7684\u7f13\u5b58ID");
        DB_INFO_ID_LIST.clear();
        DCE_INFO_ID_LIST.clear();
        SNMP_INFO_ID_LIST.clear();
        HEATH_MONITOR_ID_LIST.clear();
        FTP_ID_LIST.clear();
        SERVER_BACKUP_IP_LIST.clear();
    }

    public static void cacheSaveFtpInfoId(List<FtpInfo> ftpInfoList) {
        try {
            if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                for (FtpInfo ftpInfo : ftpInfoList) {
                    if (FTP_ID_LIST.contains(ftpInfo.getId())) continue;
                    FTP_ID_LIST.add(ftpInfo.getId());
                }
            } else {
                for (FtpInfo ftpInfo : ftpInfoList) {
                    RedisDataUtil.setValue(REDIS_SERVER_BACKUP_PREFIX + ftpInfo.getId(), ftpInfo.getId());
                }
            }
        }
        catch (Exception e) {
            logger.error("server-backup\u7f13\u5b58\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public static boolean isExistFtpInfoId(String id) {
        try {
            String value;
            return StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl()) ? FTP_ID_LIST.contains(id) : !StringUtils.isEmpty((CharSequence)(value = RedisDataUtil.getValue(REDIS_SERVER_BACKUP_PREFIX + id)));
        }
        catch (Exception e) {
            logger.error("server-backup\u67e5\u627e\u6570\u636e\u9519\u8bef", (Throwable)e);
            return false;
        }
    }

    public static void cacheSaveDbInfoId(List<DbInfo> dbInfoList) {
        try {
            if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                for (DbInfo dbInfo : dbInfoList) {
                    if (DB_INFO_ID_LIST.contains(dbInfo.getId())) continue;
                    DB_INFO_ID_LIST.add(dbInfo.getId());
                }
            } else {
                for (DbInfo dbInfo : dbInfoList) {
                    RedisDataUtil.setValue(REDIS_SERVER_BACKUP_PREFIX + dbInfo.getId(), dbInfo.getId());
                }
            }
        }
        catch (Exception e) {
            logger.error("server-backup\u7f13\u5b58\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public static boolean isExistDbInfoId(String id) {
        try {
            String value;
            return StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl()) ? DB_INFO_ID_LIST.contains(id) : !StringUtils.isEmpty((CharSequence)(value = RedisDataUtil.getValue(REDIS_SERVER_BACKUP_PREFIX + id)));
        }
        catch (Exception e) {
            logger.error("server-backup\u67e5\u627e\u6570\u636e\u9519\u8bef", (Throwable)e);
            return false;
        }
    }

    public static void cacheSaveDceInfoId(List<DceInfo> dceInfoList) {
        try {
            if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                for (DceInfo dceInfo : dceInfoList) {
                    if (DCE_INFO_ID_LIST.contains(dceInfo.getId())) continue;
                    DCE_INFO_ID_LIST.add(dceInfo.getId());
                }
            } else {
                for (DceInfo dceInfo : dceInfoList) {
                    RedisDataUtil.setValue(REDIS_SERVER_BACKUP_PREFIX + dceInfo.getId(), dceInfo.getId());
                }
            }
        }
        catch (Exception e) {
            logger.error("server-backup\u7f13\u5b58\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public static boolean isExistDceInfoId(String id) {
        try {
            String value;
            return StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl()) ? DCE_INFO_ID_LIST.contains(id) : !StringUtils.isEmpty((CharSequence)(value = RedisDataUtil.getValue(REDIS_SERVER_BACKUP_PREFIX + id)));
        }
        catch (Exception e) {
            logger.error("server-backup\u67e5\u627e\u6570\u636e\u9519\u8bef", (Throwable)e);
            return false;
        }
    }

    public static void cacheSaveSnmpInfoId(List<SnmpInfo> snmpInfoList) {
        try {
            if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                for (SnmpInfo snmpInfo : snmpInfoList) {
                    if (SNMP_INFO_ID_LIST.contains(snmpInfo.getId())) continue;
                    SNMP_INFO_ID_LIST.add(snmpInfo.getId());
                }
            } else {
                for (SnmpInfo snmpInfo : snmpInfoList) {
                    RedisDataUtil.setValue(REDIS_SERVER_BACKUP_PREFIX + snmpInfo.getId(), snmpInfo.getId());
                }
            }
        }
        catch (Exception e) {
            logger.error("server-backup\u7f13\u5b58\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public static void cacheSaveSnmpDeepInfoId(List<SnmpDeepInfo> snmpInfoList) {
        try {
            if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                for (SnmpDeepInfo snmpInfo : snmpInfoList) {
                    if (SNMP_INFO_ID_LIST.contains(snmpInfo.getId())) continue;
                    SNMP_INFO_ID_LIST.add(snmpInfo.getId());
                }
            } else {
                for (SnmpDeepInfo snmpInfo : snmpInfoList) {
                    RedisDataUtil.setValue(REDIS_SERVER_BACKUP_PREFIX + snmpInfo.getId(), snmpInfo.getId());
                }
            }
        }
        catch (Exception e) {
            logger.error("server-backup\u7f13\u5b58\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public static boolean isExistSnmpInfoId(String id) {
        try {
            String value;
            return StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl()) ? SNMP_INFO_ID_LIST.contains(id) : !StringUtils.isEmpty((CharSequence)(value = RedisDataUtil.getValue(REDIS_SERVER_BACKUP_PREFIX + id)));
        }
        catch (Exception e) {
            logger.error("server-backup\u67e5\u627e\u6570\u636e\u9519\u8bef", (Throwable)e);
            return false;
        }
    }

    public static void cacheSaveHeathMonitorId(List<HeathMonitor> heathMonitorList) {
        try {
            if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                for (HeathMonitor heathMontior : heathMonitorList) {
                    if (HEATH_MONITOR_ID_LIST.contains(heathMontior.getId())) continue;
                    HEATH_MONITOR_ID_LIST.add(heathMontior.getId());
                }
            } else {
                for (HeathMonitor heathMontior : heathMonitorList) {
                    RedisDataUtil.setValue(REDIS_SERVER_BACKUP_PREFIX + heathMontior.getId(), heathMontior.getId());
                }
            }
        }
        catch (Exception e) {
            logger.error("server-backup\u7f13\u5b58\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public static boolean isExistHeathMonitorId(String id) {
        try {
            String value;
            return StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl()) ? HEATH_MONITOR_ID_LIST.contains(id) : !StringUtils.isEmpty((CharSequence)(value = RedisDataUtil.getValue(REDIS_SERVER_BACKUP_PREFIX + id)));
        }
        catch (Exception e) {
            logger.error("server-backup\u67e5\u627e\u6570\u636e\u9519\u8bef", (Throwable)e);
            return false;
        }
    }

    public static void cacheSaveServerBackupIP(JSONObject agentJsonObject, HttpServletRequest request) {
        try {
            if (null == agentJsonObject) {
                return;
            }
            String bindIp = agentJsonObject.getStr("bindIp");
            String version = agentJsonObject.getStr("version");
            if (StringUtils.isEmpty((CharSequence)bindIp)) {
                bindIp = IpUtil.getIpAddr(request);
            }
            String resultSave = bindIp + " \uff08\u7248\u672c" + version + "\uff09";
            if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                if (!SERVER_BACKUP_IP_LIST.contains(resultSave)) {
                    SERVER_BACKUP_IP_LIST.add(resultSave);
                }
            } else {
                List<String> resultList = RedisDataUtil.getListValue(REDIS_SERVER_BACKUP_BINDIP_LIST);
                if (!resultList.contains(resultSave)) {
                    RedisDataUtil.setListValue(REDIS_SERVER_BACKUP_BINDIP_LIST, resultSave);
                }
            }
        }
        catch (Exception e) {
            logger.error("server-backup\u7f13\u5b58bindIP\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
    }

    public static List<String> getServerBackupIPList() {
        List<String> resultList = new ArrayList();
        try {
            if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                return SERVER_BACKUP_IP_LIST;
            }
            resultList = RedisDataUtil.getListValue(REDIS_SERVER_BACKUP_BINDIP_LIST);
        }
        catch (Exception e) {
            logger.error("server-backup\u7f13\u5b58bindIP\u6570\u636e\u9519\u8bef", (Throwable)e);
        }
        return resultList;
    }
}

