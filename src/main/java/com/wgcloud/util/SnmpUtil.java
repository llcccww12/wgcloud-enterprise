/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.core.collection.CollectionUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.wgcloud.entity.SnmpInfo;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.PingUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Session;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthenticationProtocol;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.PrivacyProtocol;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class SnmpUtil {
    private static final Logger logger = LoggerFactory.getLogger(SnmpUtil.class);
    private static final String SEND_PREFIX = "send_";
    private static final String RECEIVE_PREFIX = "receive_";
    private static final String GROUP_BY_AVG_PREFIX = "avg";
    private static final String GROUP_BY_MIN_PREFIX = "min";
    private static final String GROUP_BY_MAX_PREFIX = "max";
    private static final String GROUP_BY_SUM_PREFIX = "sum";
    private static final String GROUP_BY_COUNT_PREFIX = "count";

    public static Target createDefault(String ip, String community, String port, int snmpVersion, Snmp snmp, String securityName, String authPassphrase, String privPassphrase) {
        if (StringUtils.isBlank((CharSequence)ip)) {
            logger.error("ip is null.");
            return null;
        }
        if (StringUtils.isBlank((CharSequence)community)) {
            logger.error("community is null.");
            return null;
        }
        Address address = GenericAddress.parse((String)("udp:" + ip + "/" + port));
        Target target = null;
        if (snmpVersion == 3) {
            target = new UserTarget();
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel((SecurityModel)usm);
            SecurityProtocols securityProtocols = SecurityProtocols.getInstance();
            securityProtocols.addAuthenticationProtocol((AuthenticationProtocol)new AuthMD5());
            securityProtocols.addPrivacyProtocol((PrivacyProtocol)new PrivDES());
            UsmUser user = new UsmUser(new OctetString(securityName), AuthMD5.ID, new OctetString(authPassphrase), PrivDES.ID, new OctetString(privPassphrase));
            snmp.getUSM().addUser(new OctetString(securityName), user);
            ((UserTarget)target).setSecurityLevel(3);
            ((UserTarget)target).setSecurityName(new OctetString(securityName));
        } else {
            target = new CommunityTarget();
            ((CommunityTarget)target).setCommunity(new OctetString(community));
            if (snmpVersion == 1) {
                target.setSecurityModel(2);
            }
        }
        target.setVersion(snmpVersion);
        target.setAddress(address);
        target.setTimeout(3000L);
        target.setRetries(2);
        return target;
    }

    public static Map<String, String> getOnLineList(List<SnmpInfo> snmpInfoAllList) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        HashSet<String> set = new HashSet<String>();
        for (SnmpInfo snmpInfo : snmpInfoAllList) {
            set.add(snmpInfo.getHostname());
        }
        for (String hostName : set) {
            if (!SnmpUtil.isEthernetConnection(hostName)) {
                resultMap.put(hostName, "2");
                continue;
            }
            resultMap.put(hostName, "1");
        }
        logger.info("snmp\u8bbe\u5907\u5728\u7ebf\u96c6\u5408\uff1a" + ((Object)resultMap).toString());
        return resultMap;
    }

    public static boolean isEthernetConnection(String ip) {
        try {
            long resTimes = PingUtil.ping(ip, 1, 4);
            if (resTimes > 0L) {
                return true;
            }
        }
        catch (Exception e) {
            logger.error("isEthernetConnection\u9519\u8bef", (Throwable)e);
        }
        return false;
    }

    public static SnmpInfo getAvgSnmpInfo(SnmpInfo snmpInfo) {
        SnmpInfo snmpInfoResult = new SnmpInfo();
        try {
            HashMap<String, Long> everyIfOctetMapBegin = new HashMap<String, Long>();
            snmpInfoResult = SnmpUtil.snmpGet(snmpInfo, everyIfOctetMapBegin);
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getRecvOID()) && !StringUtils.isEmpty((CharSequence)snmpInfo.getSentOID())) {
                if (!"1.3.6.1.2.1.2.2.1.10".equals(snmpInfo.getRecvOID()) && !"1.3.6.1.2.1.2.2.1.16".equals(snmpInfo.getSentOID())) {
                    Thread.sleep(2000L);
                    SnmpInfo snmpInfoForSpeed = new SnmpInfo();
                    snmpInfoForSpeed.setHostname(snmpInfo.getHostname());
                    snmpInfoForSpeed.setSnmpPort(snmpInfo.getSnmpPort());
                    snmpInfoForSpeed.setSnmpCommunity(snmpInfo.getSnmpCommunity());
                    snmpInfoForSpeed.setSnmpVersion(snmpInfo.getSnmpVersion());
                    snmpInfoForSpeed.setSentOID(snmpInfo.getSentOID());
                    snmpInfoForSpeed.setRecvOID(snmpInfo.getRecvOID());
                    HashMap<String, Long> everyIfOctetMapEnd = new HashMap<String, Long>();
                    SnmpUtil.snmpGet(snmpInfoForSpeed, everyIfOctetMapEnd);
                    SnmpUtil.calcEveryIfOctetSpeedForPart(everyIfOctetMapBegin, everyIfOctetMapEnd, snmpInfoResult, snmpInfoForSpeed);
                }
                SnmpUtil.noInOutOidHanler(snmpInfo, snmpInfoResult);
            }
            if (!StringUtils.isEmpty((CharSequence)snmpInfo.getIfOperStatusOid())) {
                String allIfOperStatus = SnmpUtil.walkSnmpIfOperStatus(snmpInfo);
                snmpInfoResult.setIfOperStatusValue(allIfOperStatus);
                if (!StringUtils.isEmpty((CharSequence)snmpInfo.getTestErrorMsg())) {
                    snmpInfoResult.setTestErrorMsg(snmpInfoResult.getTestErrorMsg() + "," + snmpInfo.getTestErrorMsg());
                }
            }
            SnmpUtil.snmpWalkForGroupBy(snmpInfo, snmpInfoResult);
            return snmpInfoResult;
        }
        catch (Exception e) {
            logger.error("getAvgSnmpInfo\u9519\u8bef", (Throwable)e);
            snmpInfoResult.setRecvAvg("");
            snmpInfoResult.setSentAvg("");
            snmpInfoResult.setMemPer("0");
            snmpInfoResult.setTemperatureValue("0");
            snmpInfoResult.setCpuPer("0");
            snmpInfoResult.setTestErrorMsg(snmpInfoResult.getTestErrorMsg() + "," + e.toString());
            return snmpInfoResult;
        }
    }

    private static void calcEveryIfOctetSpeedForPart(Map<String, Long> everyIfOctetMapBegin, Map<String, Long> everyIfOctetMapEnd, SnmpInfo snmpInfoResult, SnmpInfo snmpInfoForSpeed) {
        String[] recvOIDArray;
        ArrayList<String> ifOperOidAllList = new ArrayList<String>();
        for (String recvOID : recvOIDArray = snmpInfoForSpeed.getRecvOID().split("\\r\\n")) {
            String indexOper = SnmpUtil.getLastIfOctetIndex(recvOID);
            ifOperOidAllList.add(indexOper);
        }
        SnmpUtil.calcEveryIfOctetSpeed(everyIfOctetMapBegin, everyIfOctetMapEnd, snmpInfoResult, ifOperOidAllList);
    }

    private static String getLastIfOctetIndex(String octetOid) {
        if (StringUtils.isEmpty((CharSequence)octetOid)) {
            return "";
        }
        if (!octetOid.contains(".")) {
            return octetOid;
        }
        try {
            String indexOper = octetOid.substring(octetOid.lastIndexOf(".") + 1);
            return indexOper;
        }
        catch (Exception e) {
            logger.error("\u83b7\u53d6\u63a5\u53e3\u7684\u7f16\u53f7\u9519\u8bef", (Throwable)e);
            return "";
        }
    }

    private static void calcEveryIfOctetSpeed(Map<String, Long> everyIfOctetMapBegin, Map<String, Long> everyIfOctetMapEnd, SnmpInfo snmpInfoResult, List<String> ifOperOidList) {
        StringBuffer sendSpeedValues = new StringBuffer();
        StringBuffer receiveSpeedValues = new StringBuffer();
        Double recvAvgSum = 0.0;
        Double sentAvgSum = 0.0;
        for (String key : ifOperOidList) {
            Long diffSend;
            double valueAvgSend;
            Long valueByteBeginSend;
            Long diff;
            double valueAvgRece;
            Long valueByteBeginReceive;
            String indexOper = SnmpUtil.getLastIfOctetIndex(key);
            Long valueByteEndReceive = everyIfOctetMapEnd.get(RECEIVE_PREFIX + key);
            if (null == valueByteEndReceive) {
                valueByteEndReceive = 0L;
            }
            if (null == (valueByteBeginReceive = everyIfOctetMapBegin.get(RECEIVE_PREFIX + key))) {
                valueByteBeginReceive = 0L;
            }
            if ((valueAvgRece = (double)(diff = Long.valueOf(valueByteEndReceive - valueByteBeginReceive)).longValue() / 2.0) < 0.0) {
                valueAvgRece = Math.abs(valueAvgRece);
            }
            valueAvgRece = FormatUtil.formatDouble(valueAvgRece / 1024.0, 2);
            receiveSpeedValues.append(indexOper + ":" + valueAvgRece + ",");
            recvAvgSum = recvAvgSum + valueAvgRece;
            Long valueByteEndSend = everyIfOctetMapEnd.get(SEND_PREFIX + key);
            if (null == valueByteEndSend) {
                valueByteEndSend = 0L;
            }
            if (null == (valueByteBeginSend = everyIfOctetMapBegin.get(SEND_PREFIX + key))) {
                valueByteBeginSend = 0L;
            }
            if ((valueAvgSend = (double)(diffSend = Long.valueOf(valueByteEndSend - valueByteBeginSend)).longValue() / 2.0) < 0.0) {
                valueAvgSend = Math.abs(valueAvgSend);
            }
            valueAvgSend = FormatUtil.formatDouble(valueAvgSend / 1024.0, 2);
            sendSpeedValues.append(indexOper + ":" + valueAvgSend + ",");
            sentAvgSum = sentAvgSum + valueAvgSend;
        }
        snmpInfoResult.setSentAvg(sendSpeedValues.toString());
        snmpInfoResult.setRecvAvg(receiveSpeedValues.toString());
        snmpInfoResult.setRecvAvgSum(FormatUtil.formatDouble(recvAvgSum, 2) + "");
        snmpInfoResult.setSentAvgSum(FormatUtil.formatDouble(sentAvgSum, 2) + "");
    }

    private static void noInOutOidHanler(SnmpInfo snmpInfo, SnmpInfo snmpInfoResult) {
        SnmpInfo snmpInfoDefaultAllOpers = new SnmpInfo();
        if ("1.3.6.1.2.1.2.2.1.10".equals(snmpInfo.getRecvOID()) && "1.3.6.1.2.1.2.2.1.16".equals(snmpInfo.getSentOID())) {
            snmpInfoDefaultAllOpers = SnmpUtil.getDefaultAllOpersBytes(snmpInfo);
            snmpInfoResult.setBytesRecv(snmpInfoDefaultAllOpers.getBytesRecv());
            snmpInfoResult.setBytesSent(snmpInfoDefaultAllOpers.getBytesSent());
            snmpInfoResult.setRecvAvg(snmpInfoDefaultAllOpers.getRecvAvg());
            snmpInfoResult.setSentAvg(snmpInfoDefaultAllOpers.getSentAvg());
            snmpInfoResult.setRecvAvgSum(snmpInfoDefaultAllOpers.getRecvAvgSum());
            snmpInfoResult.setSentAvgSum(snmpInfoDefaultAllOpers.getSentAvgSum());
            if (!StringUtils.isEmpty((CharSequence)snmpInfoDefaultAllOpers.getTestErrorMsg())) {
                snmpInfoResult.setTestErrorMsg(snmpInfoResult.getTestErrorMsg() + "," + snmpInfoDefaultAllOpers.getTestErrorMsg());
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static SnmpInfo snmpGet(SnmpInfo snmpInfo, Map<String, Long> everyIfOctetMap) {
        SnmpInfo snmpInfoRes;
        block29: {
            SnmpInfo snmpInfo2;
            Snmp snmp = null;
            DefaultUdpTransportMapping transport = null;
            snmpInfoRes = new SnmpInfo();
            Vector vector = null;
            try {
                String diskPerOid;
                String voltageOID;
                String temperatureOID;
                String memSizeOID;
                String cpuPerOID;
                String receiveOID;
                String ip = snmpInfo.getHostname();
                String community = snmpInfo.getSnmpCommunity();
                String port = snmpInfo.getSnmpPort();
                int snmpVersion = Integer.valueOf(snmpInfo.getSnmpVersion());
                String sendOID = snmpInfo.getSentOID();
                if (StringUtils.isEmpty((CharSequence)sendOID)) {
                    sendOID = "";
                }
                if (StringUtils.isEmpty((CharSequence)(receiveOID = snmpInfo.getRecvOID()))) {
                    receiveOID = "";
                }
                if (SnmpUtil.checkGroupByFunc(cpuPerOID = snmpInfo.getCpuPerOID())) {
                    cpuPerOID = "";
                }
                if (SnmpUtil.checkGroupByFunc(memSizeOID = snmpInfo.getMemSizeOID())) {
                    memSizeOID = "";
                }
                if (SnmpUtil.checkGroupByFunc(temperatureOID = snmpInfo.getTemperatureOid())) {
                    temperatureOID = "";
                }
                if (SnmpUtil.checkGroupByFunc(voltageOID = snmpInfo.getVoltageOid())) {
                    voltageOID = "";
                }
                if (SnmpUtil.checkGroupByFunc(diskPerOid = snmpInfo.getDiskPerOid())) {
                    diskPerOid = "";
                }
                String sysDescOid = snmpInfo.getSysDescOid();
                if (StringUtils.isEmpty((CharSequence)sendOID) || StringUtils.isEmpty((CharSequence)receiveOID)) {
                    snmpInfoRes.setBytesSent("0");
                    snmpInfoRes.setBytesRecv("0");
                }
                transport = new DefaultUdpTransportMapping();
                transport.listen();
                snmp = new Snmp((TransportMapping)transport);
                Target myTarget = SnmpUtil.createDefault(ip, community, port, snmpVersion, snmp, snmpInfo.getSecurityName(), snmpInfo.getAuthPass(), snmpInfo.getPrivPass());
                PDU request = new PDU();
                if (!"1.3.6.1.2.1.2.2.1.16".equals(snmpInfo.getSentOID())) {
                    SnmpUtil.bindingRequestVars(request, sendOID.split("\\r\\n"));
                }
                if (!"1.3.6.1.2.1.2.2.1.10".equals(snmpInfo.getRecvOID())) {
                    SnmpUtil.bindingRequestVars(request, receiveOID.split("\\r\\n"));
                }
                HashMap<String, Double> otherOIDValMap = new HashMap<String, Double>();
                if (!StringUtils.isEmpty((CharSequence)cpuPerOID)) {
                    List<String> cpuPerOIDList = SnmpUtil.compileExpression(cpuPerOID);
                    for (String cpuPerOIDTmp : cpuPerOIDList) {
                        SnmpUtil.bindingRequestVars(request, cpuPerOIDTmp.trim());
                        otherOIDValMap.put(cpuPerOIDTmp.trim(), 0.0);
                    }
                }
                if (!StringUtils.isEmpty((CharSequence)memSizeOID)) {
                    List<String> memSizeOIDList = SnmpUtil.compileExpression(memSizeOID);
                    for (String memSizeOIDTmp : memSizeOIDList) {
                        SnmpUtil.bindingRequestVars(request, memSizeOIDTmp.trim());
                        otherOIDValMap.put(memSizeOIDTmp.trim(), 0.0);
                    }
                }
                if (!StringUtils.isEmpty((CharSequence)diskPerOid)) {
                    List<String> diskPerOidList = SnmpUtil.compileExpression(diskPerOid);
                    for (String diskPerOidTmp : diskPerOidList) {
                        SnmpUtil.bindingRequestVars(request, diskPerOidTmp.trim());
                        otherOIDValMap.put(diskPerOidTmp.trim(), 0.0);
                    }
                }
                SnmpUtil.bindingRequestVars(request, temperatureOID);
                SnmpUtil.bindingRequestVars(request, voltageOID);
                SnmpUtil.bindingRequestVars(request, sysDescOid);
                request.setType(-96);
                ResponseEvent responseEvent = snmp.send(request, myTarget);
                PDU response = responseEvent.getResponse();
                vector = response.getVariableBindings();
                long bytesSentSum = 0L;
                long bytesRecvSum = 0L;
                for (int i = 0; i < vector.size(); ++i) {
                    try {
                        VariableBinding vb1 = (VariableBinding)vector.get(i);

                        // 安全获取变量值，跳过 noSuchInstance/noSuchObject 等无效值
                        String varValue = safeGetVariableValue(vb1.getVariable());
                        if (varValue == null) {
                            logger.warn("SNMP\u53d8\u91cf\u503c\u65e0\u6548(noSuchInstance/noSuchObject/null)\uff0c\u8df3\u8fc7OID: {}", vb1.getOid());
                            continue;
                        }

                        if (!StringUtils.isEmpty((CharSequence)sendOID) && sendOID.contains(String.valueOf(vb1.getOid()))) {
                            long sendBytes = Long.valueOf(varValue);
                            bytesSentSum += sendBytes;
                            everyIfOctetMap.put(SEND_PREFIX + SnmpUtil.getLastIfOctetIndex(String.valueOf(vb1.getOid())), sendBytes);
                            continue;
                        }
                        if (!StringUtils.isEmpty((CharSequence)receiveOID) && receiveOID.contains(String.valueOf(vb1.getOid()))) {
                            long receiveBytes = Long.valueOf(varValue);
                            bytesRecvSum += receiveBytes;
                            everyIfOctetMap.put(RECEIVE_PREFIX + SnmpUtil.getLastIfOctetIndex(String.valueOf(vb1.getOid())), receiveBytes);
                            continue;
                        }
                        for (Map.Entry entry : otherOIDValMap.entrySet()) {
                            if (!((String)entry.getKey()).contains(String.valueOf(vb1.getOid()))) continue;
                            otherOIDValMap.put((String)entry.getKey(), Double.valueOf(varValue));
                            break;
                        }
                        if (!StringUtils.isEmpty((CharSequence)temperatureOID) && temperatureOID.contains(String.valueOf(vb1.getOid()))) {
                            snmpInfoRes.setTemperatureValue(FormatUtil.formatDouble(varValue, 2) + "");
                            continue;
                        }
                        if (!StringUtils.isEmpty((CharSequence)voltageOID) && voltageOID.contains(String.valueOf(vb1.getOid()))) {
                            snmpInfoRes.setVoltageValue(FormatUtil.formatDouble(varValue, 2) + "");
                            continue;
                        }
                        if (StringUtils.isEmpty((CharSequence)sysDescOid) || !sysDescOid.contains(String.valueOf(vb1.getOid()))) continue;
                        // 系统描述可能很长，截断到200字符避免数据库字段溢出
                        if (varValue.length() > 200) {
                            varValue = varValue.substring(0, 200);
                        }
                        snmpInfoRes.setSysDescVal(varValue);
                        continue;
                    }
                    catch (Exception e) {
                        logger.error("snmp\u5e94\u7b54pdu\u83b7\u5f97mib\u4fe1\u606f\u9519\u8bef", (Throwable)e);
                        snmpInfoRes.setTestErrorMsg(snmpInfoRes.getTestErrorMsg() + "," + e.toString());
                    }
                }
                snmpInfoRes.setBytesSent(String.valueOf(bytesSentSum));
                snmpInfoRes.setBytesRecv(String.valueOf(bytesRecvSum));
                snmpInfoRes.setCpuPer(SnmpUtil.computeExpressionValue(cpuPerOID, otherOIDValMap) + "");
                snmpInfoRes.setMemPer(SnmpUtil.computeExpressionValue(memSizeOID, otherOIDValMap) + "");
                snmpInfoRes.setDiskPer(SnmpUtil.computeExpressionValue(diskPerOid, otherOIDValMap) + "");
                snmpInfo2 = snmpInfoRes;
                SnmpUtil.closeTransport((TransportMapping)transport);
            }
            catch (Exception e) {
                logger.error("snmp\u68c0\u6d4b\u9519\u8bef", (Throwable)e);
                snmpInfoRes.setTestErrorMsg(snmpInfoRes.getTestErrorMsg() + "," + e.toString());
                break block29;
            }
            finally {
                SnmpUtil.closeTransport(transport);
                SnmpUtil.closeSnmp(snmp);
            }
            SnmpUtil.closeSnmp(snmp);
            return snmpInfo2;
        }
        return snmpInfoRes;
    }

    private static void bindingRequestVars(PDU request, String[] oids) {
        if (oids != null) {
            for (String oid : oids) {
                request.add(new VariableBinding(new OID(oid.trim())));
            }
        }
    }

    private static void bindingRequestVars(PDU request, String oid) {
        if (!StringUtils.isEmpty((CharSequence)oid)) {
            request.add(new VariableBinding(new OID(oid.trim())));
        }
    }

    public static SnmpInfo getDefaultAllOpersBytes(SnmpInfo snmpInfo) {
        SnmpInfo snmpInfoWalkResult = null;
        try {
            SnmpInfo snmpInfoWalkEnd;
            ArrayList<String> ifOperOidList = new ArrayList<String>();
            HashMap<String, Long> everyIfOctetMapBegin = new HashMap<String, Long>();
            SnmpInfo snmpInfoWalkBegin = SnmpUtil.walkSnmp(snmpInfo, everyIfOctetMapBegin, null);
            Thread.sleep(2000L);
            HashMap<String, Long> everyIfOctetMapEnd = new HashMap<String, Long>();
            snmpInfoWalkResult = snmpInfoWalkEnd = SnmpUtil.walkSnmp(snmpInfo, everyIfOctetMapEnd, ifOperOidList);
            SnmpUtil.calcEveryIfOctetSpeed(everyIfOctetMapBegin, everyIfOctetMapEnd, snmpInfoWalkResult, ifOperOidList);
            return snmpInfoWalkResult;
        }
        catch (Exception e) {
            logger.error("getDefaultAllOpersBytes\u9519\u8bef", (Throwable)e);
            if (null != snmpInfoWalkResult) {
                snmpInfoWalkResult.setTestErrorMsg(snmpInfoWalkResult.getTestErrorMsg() + "," + e.toString());
            }
            return snmpInfoWalkResult;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static SnmpInfo walkSnmp(SnmpInfo snmpInfo, Map<String, Long> everyIfOctetMap, List<String> ifOperOidList) {
        Snmp snmp = null;
        SnmpInfo snmpInfoRes = new SnmpInfo();
        try {
            long bytesSentSum = 0L;
            long bytesRecvSum = 0L;
            String allReceiveOID = "1.3.6.1.2.1.2.2.1.10";
            String allSendOID = "1.3.6.1.2.1.2.2.1.16";
            String ip = snmpInfo.getHostname();
            String community = snmpInfo.getSnmpCommunity();
            String port = snmpInfo.getSnmpPort();
            int snmpVersion = Integer.valueOf(snmpInfo.getSnmpVersion());
            snmp = new Snmp((TransportMapping)new DefaultUdpTransportMapping());
            snmp.listen();
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setVersion(snmpVersion);
            target.setAddress((Address)new UdpAddress(ip + "/" + port));
            target.setTimeout(60000L);
            target.setRetries(1);
            TableUtils utils = new TableUtils((Session)snmp, (PDUFactory)new DefaultPDUFactory(-91));
            utils.setMaxNumRowsPerPDU(5);
            OID[] columnOids = new OID[]{new OID(allReceiveOID), new OID(allSendOID)};
            List l = utils.getTable((Target)target, columnOids, null, null);
            for (TableEvent e : (java.util.List<TableEvent>)l) {
                logger.info("IfOperByte TableEvent---------" + e.toString());
                VariableBinding[] values = e.getColumns();
                if (values == null) continue;
                String oidTemp = values[0].getOid().toString();
                if (null != ifOperOidList) {
                    ifOperOidList.add(oidTemp);
                }
                Long receiveByte = Long.valueOf(values[0].getVariable().toString());
                everyIfOctetMap.put(RECEIVE_PREFIX + oidTemp, receiveByte);
                Long sendByte = Long.valueOf(values[1].getVariable().toString());
                everyIfOctetMap.put(SEND_PREFIX + oidTemp, sendByte);
                bytesRecvSum += receiveByte.longValue();
                bytesSentSum += sendByte.longValue();
            }
            snmpInfoRes.setBytesSent(String.valueOf(bytesSentSum));
            snmpInfoRes.setBytesRecv(String.valueOf(bytesRecvSum));
            SnmpInfo snmpInfo2 = snmpInfoRes;
            SnmpUtil.closeSnmp(snmp);
            return snmpInfo2;
        }
        catch (Exception e) {
            logger.error("walkSnmp\u9519\u8bef", (Throwable)e);
        }
        finally {
            SnmpUtil.closeSnmp(snmp);
        }
        return snmpInfoRes;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String walkSnmpIfOperStatus(SnmpInfo snmpInfo) {
        if (StringUtils.isEmpty((CharSequence)snmpInfo.getIfOperStatusOid())) {
            return "";
        }
        logger.info("walkSnmpIfOperStatus------" + snmpInfo.getHostname());
        Snmp snmp = null;
        try {
            String allOperStatusOid = snmpInfo.getIfOperStatusOid();
            String ip = snmpInfo.getHostname();
            String community = snmpInfo.getSnmpCommunity();
            String port = snmpInfo.getSnmpPort();
            int snmpVersion = Integer.valueOf(snmpInfo.getSnmpVersion());
            snmp = new Snmp((TransportMapping)new DefaultUdpTransportMapping());
            snmp.listen();
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setVersion(snmpVersion);
            target.setAddress((Address)new UdpAddress(ip + "/" + port));
            target.setTimeout(60000L);
            target.setRetries(1);
            TableUtils utils = new TableUtils((Session)snmp, (PDUFactory)new DefaultPDUFactory(-91));
            utils.setMaxNumRowsPerPDU(5);
            OID[] columnOids = new OID[]{new OID(allOperStatusOid)};
            List l = utils.getTable((Target)target, columnOids, null, null);
            StringBuffer operStatusResult = new StringBuffer("");
            String oidTemp = "";
            String indexOperTemp = "";
            String operStatusTemp = "";
            for (TableEvent e : (java.util.List<TableEvent>)l) {
                logger.info("IfOperStatus TableEvent---------" + e.toString());
                VariableBinding[] values = e.getColumns();
                if (values == null) continue;
                oidTemp = values[0].getOid().toString();
                indexOperTemp = oidTemp.substring(oidTemp.lastIndexOf(".") + 1);
                operStatusTemp = values[0].getVariable().toString();
                operStatusResult.append(indexOperTemp + ":" + operStatusTemp + ",");
            }
            String string = operStatusResult.toString();
            SnmpUtil.closeSnmp(snmp);
            return string;
        }
        catch (Exception e) {
            logger.error("walkSnmpOperStatus\u9519\u8bef", (Throwable)e);
            snmpInfo.setTestErrorMsg(snmpInfo.getTestErrorMsg() + "," + e.toString());
        }
        finally {
            SnmpUtil.closeSnmp(snmp);
        }
        return "";
    }

    private static void closeTransport(TransportMapping transport) {
        try {
            if (null != transport) {
                transport.close();
            }
        }
        catch (Exception e) {
            logger.error("closeTransport\u9519\u8bef", (Throwable)e);
        }
    }

    private static void closeSnmp(Snmp snmp) {
        try {
            if (null != snmp) {
                snmp.close();
            }
        }
        catch (Exception e) {
            logger.error("closeSnmp\u9519\u8bef", (Throwable)e);
        }
    }

    /**
     * 安全获取SNMP变量的原始字符串值。<br>
     * 如果变量为null、Null类型、或值为"noSuchInstance"/"noSuchObject"，则返回null。
     */
    private static String safeGetVariableValue(Variable variable) {
        if (variable == null || variable instanceof Null) {
            return null;
        }
        String val = variable.toString();
        if ("noSuchInstance".equalsIgnoreCase(val) || "noSuchObject".equalsIgnoreCase(val) || "null".equalsIgnoreCase(val)) {
            return null;
        }
        return val;
    }

    private static List<String> compileExpression(String expression) {
        ArrayList<String> oids = new ArrayList<String>();
        try {
            if (!StringUtils.isEmpty((CharSequence)expression) && expression.indexOf("[") < 0) {
                oids.add(expression);
                return oids;
            }
            String regex = "\\[(.*?)\\]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(expression);
            String extractedContent = "";
            while (matcher.find()) {
                extractedContent = matcher.group(1);
                oids.add(extractedContent);
            }
        }
        catch (Exception e) {
            logger.error("\u89e3\u6790\u8868\u8fbe\u5f0f\u63d0\u53d6\u5176\u4e2d\u7684OID\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return oids;
    }

    private static Double computeExpressionValue(String expression, Map<String, Double> env) {
        Double result = 0.0;
        try {
            if (StringUtils.isEmpty((CharSequence)expression) || null == env) {
                return result;
            }
            List<String> oidList = SnmpUtil.compileExpression(expression);
            HashMap<String, Object> envNew = new HashMap<String, Object>();
            String key = "";
            for (int i = 0; i < oidList.size(); ++i) {
                key = "w" + i;
                expression = expression.replace("[" + oidList.get(i) + "]", key).replace(oidList.get(i), key);
                envNew.put(key, env.get(oidList.get(i).trim()));
            }
            Expression compiledExp = AviatorEvaluator.compile((String)expression);
            result = Double.valueOf(String.valueOf(compiledExp.execute(envNew)));
            if (result == null) {
                return 0.0;
            }
            result = FormatUtil.formatDouble(result, 2);
        }
        catch (Exception e) {
            logger.error("computeExpressionValue\u9519\u8bef", (Throwable)e);
        }
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void snmpWalkForGroupBy(SnmpInfo snmpInfo, SnmpInfo snmpInfoResult) {
        String diskPerOid;
        String voltageOID;
        String temperatureOID;
        String memSizeOID;
        ArrayList<String> oidList = new ArrayList<String>();
        String cpuPerOID = snmpInfo.getCpuPerOID();
        if (SnmpUtil.checkGroupByFunc(cpuPerOID)) {
            oidList.add(cpuPerOID);
        }
        if (SnmpUtil.checkGroupByFunc(memSizeOID = snmpInfo.getMemSizeOID())) {
            oidList.add(memSizeOID);
        }
        if (SnmpUtil.checkGroupByFunc(temperatureOID = snmpInfo.getTemperatureOid())) {
            oidList.add(temperatureOID);
        }
        if (SnmpUtil.checkGroupByFunc(voltageOID = snmpInfo.getVoltageOid())) {
            oidList.add(voltageOID);
        }
        if (SnmpUtil.checkGroupByFunc(diskPerOid = snmpInfo.getDiskPerOid())) {
            oidList.add(diskPerOid);
        }
        if (CollectionUtil.isEmpty(oidList)) {
            return;
        }
        logger.info("snmpWalkForGroupBy------" + ((Object)oidList).toString());
        Snmp snmp = null;
        try {
            String ip = snmpInfo.getHostname();
            String community = snmpInfo.getSnmpCommunity();
            String port = snmpInfo.getSnmpPort();
            int snmpVersion = Integer.valueOf(snmpInfo.getSnmpVersion());
            snmp = new Snmp((TransportMapping)new DefaultUdpTransportMapping());
            snmp.listen();
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString(community));
            target.setVersion(snmpVersion);
            target.setAddress((Address)new UdpAddress(ip + "/" + port));
            target.setTimeout(60000L);
            target.setRetries(1);
            TableUtils utils = new TableUtils((Session)snmp, (PDUFactory)new DefaultPDUFactory(-91));
            utils.setMaxNumRowsPerPDU(5);
            for (String oidValue : oidList) {
                try {
                    List<String> oidChildList = SnmpUtil.compileExpression(oidValue);
                    if (CollectionUtil.isEmpty(oidChildList)) continue;
                    String result = "";
                    OID[] columnOids = new OID[]{new OID(oidChildList.get(0))};
                    List tableList = utils.getTable((Target)target, columnOids, null, null);
                    ArrayList<Double> valueList = new ArrayList<Double>();
                    for (TableEvent e : (java.util.List<TableEvent>)tableList) {
                        VariableBinding[] values = e.getColumns();
                        if (values == null) continue;
                        valueList.add(Double.valueOf(String.valueOf(values[0].getVariable())));
                        logger.info("TableEvent---------" + e.toString());
                    }
                    if (oidValue.equals(snmpInfo.getCpuPerOID())) {
                        snmpInfoResult.setCpuPer(SnmpUtil.getDataGroupByFunc(valueList, oidValue) + "");
                        if (snmpInfoResult.getCpuPer().length() > 10) {
                            snmpInfoResult.setCpuPer(snmpInfoResult.getCpuPer().substring(0, 10));
                        }
                    }
                    if (oidValue.equals(snmpInfo.getMemSizeOID())) {
                        snmpInfoResult.setMemPer(SnmpUtil.getDataGroupByFunc(valueList, oidValue) + "");
                        if (snmpInfoResult.getMemPer().length() > 10) {
                            snmpInfoResult.setMemPer(snmpInfoResult.getMemPer().substring(0, 10));
                        }
                    }
                    if (oidValue.equals(snmpInfo.getTemperatureOid())) {
                        snmpInfoResult.setTemperatureValue(SnmpUtil.getDataGroupByFunc(valueList, oidValue) + "");
                        if (snmpInfoResult.getTemperatureValue().length() > 10) {
                            snmpInfoResult.setTemperatureValue(snmpInfoResult.getTemperatureValue().substring(0, 10));
                        }
                    }
                    if (oidValue.equals(snmpInfo.getVoltageOid())) {
                        snmpInfoResult.setVoltageValue(SnmpUtil.getDataGroupByFunc(valueList, oidValue) + "");
                        if (snmpInfoResult.getVoltageValue().length() > 10) {
                            snmpInfoResult.setVoltageValue(snmpInfoResult.getVoltageValue().substring(0, 10));
                        }
                    }
                    if (!oidValue.equals(snmpInfo.getDiskPerOid())) continue;
                    snmpInfoResult.setDiskPer(SnmpUtil.getDataGroupByFunc(valueList, oidValue) + "");
                    if (snmpInfoResult.getDiskPer().length() <= 10) continue;
                    snmpInfoResult.setDiskPer(snmpInfoResult.getDiskPer().substring(0, 10));
                }
                catch (Exception e) {
                    logger.error("snmp\u5e94\u7b54pdu\u83b7\u5f97mib\u4fe1\u606f\u9519\u8bef", (Throwable)e);
                }
            }
        }
        catch (Exception e) {
            try {
                logger.error("snmpWalkForGroupBy\u9519\u8bef", (Throwable)e);
                snmpInfoResult.setTestErrorMsg(snmpInfoResult.getTestErrorMsg() + "," + e.toString());
            }
            catch (Throwable throwable) {
                SnmpUtil.closeSnmp(snmp);
                throw throwable;
            }
            SnmpUtil.closeSnmp(snmp);
        }
        SnmpUtil.closeSnmp(snmp);
    }

    public static boolean checkGroupByFunc(String oid) {
        boolean sign = false;
        if (StringUtils.isEmpty((CharSequence)oid)) {
            return sign;
        }
        String oidLow = oid.trim().toLowerCase();
        if (oidLow.startsWith(GROUP_BY_AVG_PREFIX) || oidLow.startsWith(GROUP_BY_MIN_PREFIX) || oidLow.startsWith(GROUP_BY_MAX_PREFIX) || oidLow.startsWith(GROUP_BY_SUM_PREFIX) || oidLow.startsWith(GROUP_BY_COUNT_PREFIX)) {
            sign = true;
        }
        return sign;
    }

    public static Double getDataGroupByFunc(List<Double> valueList, String oid) {
        double result = 0.0;
        try {
            if (CollectionUtil.isEmpty(valueList)) {
                return 0.0;
            }
            String oidLow = oid.trim().toLowerCase();
            if (oidLow.startsWith(GROUP_BY_AVG_PREFIX)) {
                Double sum = 0.0;
                for (Double number : valueList) {
                    sum = sum + number;
                }
                result = sum / (double)valueList.size();
                result = FormatUtil.formatDouble(result, 2);
                return result;
            }
            if (oidLow.startsWith(GROUP_BY_MIN_PREFIX)) {
                return (Double)CollectionUtil.min(valueList);
            }
            if (oidLow.startsWith(GROUP_BY_MAX_PREFIX)) {
                return (Double)CollectionUtil.max(valueList);
            }
            if (oidLow.startsWith(GROUP_BY_SUM_PREFIX)) {
                Double sum = 0.0;
                for (Double number : valueList) {
                    sum = sum + number;
                }
                result = FormatUtil.formatDouble(sum, 2);
                return result;
            }
            if (oidLow.startsWith(GROUP_BY_COUNT_PREFIX)) {
                return (double) valueList.size();
            }
        }
        catch (Exception e) {
            logger.error("\u8ba1\u7b97\u805a\u5408\u51fd\u6570\u7684\u503c\u9519\u8bef", (Throwable)e);
        }
        return 0.0;
    }
}

