/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.core.collection.CollectionUtil;
import com.wgcloud.common.ApplicationContextHelper;
import com.wgcloud.config.CommonConfig;
import com.wgcloud.entity.SnmpDeepInfo;
import com.wgcloud.entity.SnmpDeepState;
import com.wgcloud.util.SnmpUtil;
import com.wgcloud.util.redis.RedisDataUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class SnmpDeepUtil {
    private static final Logger logger = LoggerFactory.getLogger(SnmpDeepUtil.class);
    private static CommonConfig commonConfig = ApplicationContextHelper.getBean(CommonConfig.class);
    public static Map<String, String> SNMP_DEEP_DATA_MAP = Collections.synchronizedMap(new HashMap());
    private static final String SNMP_DEEP_DATA_PREFIX = "SNMP_DEEP_DATA_";

    public static Map<String, String> getOnLineList(List<SnmpDeepInfo> snmpInfoAllList) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        HashSet<String> set = new HashSet<String>();
        for (SnmpDeepInfo snmpInfo : snmpInfoAllList) {
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String snmpGet(SnmpDeepInfo snmpInfo, List<SnmpDeepState> stateList) {
        String errorMsg;
        block11: {
            if (CollectionUtil.isEmpty(stateList)) {
                return "";
            }
            Snmp snmp = null;
            DefaultUdpTransportMapping transport = null;
            String resultValue = "";
            errorMsg = "";
            Vector vector = null;
            try {
                String ip = snmpInfo.getHostname();
                String community = snmpInfo.getSnmpCommunity();
                String port = snmpInfo.getSnmpPort();
                int snmpVersion = Integer.valueOf(snmpInfo.getSnmpVersion());
                transport = new DefaultUdpTransportMapping();
                transport.listen();
                snmp = new Snmp((TransportMapping)transport);
                Target myTarget = SnmpDeepUtil.createDefault(ip, community, port, snmpVersion, snmp, snmpInfo.getSecurityName(), snmpInfo.getAuthPass(), snmpInfo.getPrivPass());
                PDU request = new PDU();
                for (SnmpDeepState snmpDeepState : stateList) {
                    SnmpDeepUtil.bindingRequestVars(request, snmpDeepState.getOidValue());
                }
                request.setType(-96);
                ResponseEvent responseEvent = snmp.send(request, myTarget);
                PDU response = responseEvent.getResponse();
                vector = response.getVariableBindings();
                block8: for (int i = 0; i < vector.size(); ++i) {
                    try {
                        VariableBinding vb1 = (VariableBinding)vector.get(i);
                        for (SnmpDeepState snmpDeepState : stateList) {
                            if (StringUtils.isEmpty((CharSequence)snmpDeepState.getOidValue()) || !snmpDeepState.getOidValue().contains(String.valueOf(vb1.getOid()))) continue;
                            resultValue = String.valueOf(vb1.getVariable());
                            SnmpDeepUtil.setSnmpDeepResultHandler(snmpDeepState.getId(), resultValue);
                            continue block8;
                        }
                        continue;
                    }
                    catch (Exception e) {
                        logger.error("snmp\u5e94\u7b54pdu\u83b7\u5f97mib\u4fe1\u606f\u9519\u8bef", (Throwable)e);
                        errorMsg = e.toString();
                    }
                }
                SnmpDeepUtil.closeTransport((TransportMapping)transport);
            }
            catch (Exception e) {
                logger.error("snmp\u6df1\u5ea6\u68c0\u6d4b\u9519\u8bef", (Throwable)e);
                errorMsg = e.toString();
                break block11;
            }
            finally {
                SnmpDeepUtil.closeTransport(transport);
                SnmpDeepUtil.closeSnmp(snmp);
            }
            SnmpDeepUtil.closeSnmp(snmp);
        }
        return errorMsg;
    }

    private static void bindingRequestVars(PDU request, String oid) {
        if (!StringUtils.isEmpty((CharSequence)oid)) {
            request.add(new VariableBinding(new OID(oid.trim())));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String walkSnmp(SnmpDeepInfo snmpInfo, List<SnmpDeepState> stateList) {
        if (CollectionUtil.isEmpty(stateList)) {
            return "";
        }
        String errorMsg = "";
        logger.info("walkSnmp Test------" + snmpInfo.getHostname());
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
            for (SnmpDeepState snmpDeepState : stateList) {
                try {
                    String result = "";
                    OID[] columnOids = new OID[]{new OID(snmpDeepState.getOidValue())};
                    List l = utils.getTable((Target)target, columnOids, null, null);
                    for (TableEvent e : (java.util.List<TableEvent>)l) {
                        VariableBinding[] values = e.getColumns();
                        if (values == null) continue;
                        String oidTemp = values[0].getOid().toString();
                        String oidVal = values[0].getVariable().toString();
                        logger.info("TableEvent---------" + e.toString());
                        result = result + oidTemp + " = " + oidVal + "</br>";
                    }
                    SnmpDeepUtil.setSnmpDeepResultHandler(snmpDeepState.getId(), result);
                }
                catch (Exception e) {
                    logger.error("snmp\u5e94\u7b54pdu\u83b7\u5f97mib\u4fe1\u606f\u9519\u8bef", (Throwable)e);
                    errorMsg = e.toString();
                }
            }
            SnmpDeepUtil.closeSnmp(snmp);
        }
        catch (Exception e) {
            logger.error("walkSnmpOperStatus\u9519\u8bef", (Throwable)e);
            errorMsg = e.toString();
        }
        finally {
            SnmpDeepUtil.closeSnmp(snmp);
        }
        return errorMsg;
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

    public static void setSnmpDeepResultHandler(String key, String result) {
        if (StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
            SNMP_DEEP_DATA_MAP.put(key, result);
        } else {
            RedisDataUtil.setValue(SNMP_DEEP_DATA_PREFIX + key, result);
        }
    }

    public static String viewSnmpDeepHandler(String oid) {
        String result = "";
        try {
            if (!StringUtils.isEmpty((CharSequence)commonConfig.getRedisUrl())) {
                result = RedisDataUtil.getValue(SNMP_DEEP_DATA_PREFIX + oid);
                return result;
            }
            result = SNMP_DEEP_DATA_MAP.get(oid);
        }
        catch (Exception e) {
            logger.error("\u67e5\u770bRabbitmq\u76d1\u63a7\u4fe1\u606f\u9519\u8bef", (Throwable)e);
        }
        return result;
    }
}

