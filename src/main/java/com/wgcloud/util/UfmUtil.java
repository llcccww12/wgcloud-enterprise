package com.wgcloud.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.entity.UfmMonitor;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

public class UfmUtil {

    private static final String UFM_HOST = "10.33.1.21";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "XMSMzsy_2024";
    private static final String BASE_URL = "https://" + UFM_HOST + "/ufmRest";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        trustAllCertificates();
    }

    /**
     * 信任所有SSL证书（用于自签名证书场景）
     */
    private static void trustAllCertificates() {
        try {
            TrustManager[] trustAll = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAll, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            throw new RuntimeException("Failed to trust all certificates", e);
        }
    }

    /**
     * 生成Basic Auth认证头
     */
    private static String getBasicAuthHeader() {
        String credentials = USERNAME + ":" + PASSWORD;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encoded;
    }

    /**
     * 执行HTTP GET请求，返回JSON字符串
     */
    private static String doGet(String path) throws Exception {
        String urlStr = BASE_URL + path;
        URL url = new URL(urlStr);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", getBasicAuthHeader());
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(120000);
        conn.setDoInput(true);

        int responseCode = conn.getResponseCode();
        String response = readResponse(conn, responseCode);
        conn.disconnect();

        if (responseCode >= 200 && responseCode < 300) {
            return response;
        } else {
            throw new RuntimeException("HTTP GET " + urlStr + " failed with code " + responseCode + ": " + response);
        }
    }

    /**
     * 执行HTTP POST请求，返回JSON字符串
     */
    private static String doPost(String path, String body) throws Exception {
        String urlStr = BASE_URL + path;
        URL url = new URL(urlStr);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", getBasicAuthHeader());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(120000);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        String response = readResponse(conn, responseCode);
        conn.disconnect();

        if (responseCode >= 200 && responseCode < 300) {
            return response;
        } else {
            throw new RuntimeException("HTTP POST " + urlStr + " failed with code " + responseCode + ": " + response);
        }
    }

    /**
     * 读取HTTP响应内容（安全处理 errorStream 为 null 的情况）
     */
    private static String readResponse(HttpsURLConnection conn, int responseCode) throws IOException {
        InputStream is;
        if (responseCode >= 200 && responseCode < 300) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }
        if (is == null) {
            return "";
        }
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    /**
     * 获取所有设备列表
     * GET /ufmRest/resources/systems
     */
    public static List<Map<String, Object>> getDevices() throws Exception {
        String json = doGet("/resources/systems");
        JSONArray arr = JSONUtil.parseArray(json);
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
            result.add(map);
        }
        return result;
    }

    /**
     * 获取监控快照
     * POST /ufmRest/monitoring/snapshot
     */
    public static JSONObject getSnapshot(List<String> guids) throws Exception {
        JSONArray guidArray = new JSONArray();
        for (String guid : guids) {
            guidArray.add("Grid.default." + guid);
        }

        JSONArray attributes = new JSONArray();
        attributes.add("Infiniband_MBOut");
        attributes.add("Infiniband_MBIn");
        attributes.add("Infiniband_PckOut");
        attributes.add("Infiniband_PckIn");
        attributes.add("Infiniband_SymbolErrors");
        attributes.add("Infiniband_LinkRecovers");
        attributes.add("Infiniband_LinkDowned");
        attributes.add("Infiniband_RcvErrors");
        attributes.add("Infiniband_XmtDiscards");
        attributes.add("Infiniband_MBOutRate");
        attributes.add("Infiniband_MBInRate");
        attributes.add("Infiniband_PckOutRate");
        attributes.add("Infiniband_PckInRate");

        JSONArray functions = new JSONArray();
        functions.add("RAW");

        JSONObject requestBody = new JSONObject();
        requestBody.set("attributes", attributes);
        requestBody.set("functions", functions);
        requestBody.set("scope_object", "Device");
        requestBody.set("monitor_object", "Device");
        requestBody.set("interval", 5);
        requestBody.set("objects", guidArray);

        String json = doPost("/monitoring/snapshot", requestBody.toString());
        return JSONUtil.parseObj(json);
    }

    /**
     * 获取告警列表
     * GET /ufmRest/app/alarms
     */
    public static JSONArray getAlarms() throws Exception {
        String json = doGet("/app/alarms");
        return JSONUtil.parseArray(json);
    }

    /**
     * 采集全量数据并转换为 UfmMonitor 实体列表
     */
    public static List<UfmMonitor> collectAllData() throws Exception {
        List<UfmMonitor> result = new ArrayList<>();
        Date now = new Date();

        // 1. 获取所有设备
        List<Map<String, Object>> devices = getDevices();
        if (devices == null || devices.isEmpty()) {
            return result;
        }

        // 收集所有GUID
        List<String> guids = new ArrayList<>();
        Map<String, Map<String, Object>> deviceMap = new HashMap<>();
        for (Map<String, Object> device : devices) {
            String guid = (String) device.get("guid");
            if (guid != null && !guid.isEmpty()) {
                guids.add(guid);
                deviceMap.put(guid, device);
            }
        }

        if (guids.isEmpty()) {
            return result;
        }

        // 2. 获取监控快照
        // UFM snapshot 返回格式: { "2026-06-18 08:34:46": { "Device": { "guid": { ... } } } }
        JSONObject snapshot = getSnapshot(guids);

        // 3. 获取告警并按设备GUID统计告警数
        JSONArray alarms = getAlarms();
        Map<String, Integer> alarmCountMap = new HashMap<>();
        for (int i = 0; i < alarms.size(); i++) {
            JSONObject alarm = alarms.getJSONObject(i);
            String objName = alarm.getStr("object_name");
            if (objName != null && !objName.isEmpty()) {
                alarmCountMap.merge(objName, 1, Integer::sum);
            }
        }

        // 4. 遍历所有设备，构建UfmMonitor实体
        for (String guid : guids) {
            try {
                Map<String, Object> device = deviceMap.get(guid);
                UfmMonitor monitor = new UfmMonitor();

                // 设置UUID作为主键
                monitor.setId(UUID.randomUUID().toString().replace("-", ""));
                monitor.setGuid(guid);
                monitor.setSystemName((String) device.get("system_name"));
                monitor.setDeviceType((String) device.get("type"));
                monitor.setDeviceState((String) device.get("state"));

                // 设置健康等级（从设备信息中获取，若快照中有则从快照获取）
                String severity = (String) device.get("severity");
                monitor.setSeverity(severity != null ? severity : "Info");

                // 解析快照数据
                // UFM 返回格式: { "timestamp": { "Device": { "guid": { "dname":"...", "statistics":{...} } } } }
                // 遍历所有时间戳（通常只有一个）
                if (snapshot != null) {
                    for (String timestampKey : snapshot.keySet()) {
                        JSONObject timeLevel = snapshot.getJSONObject(timestampKey);
                        if (timeLevel == null) continue;
                        for (String scopeKey : timeLevel.keySet()) {
                            JSONObject scopeLevel = timeLevel.getJSONObject(scopeKey);
                            if (scopeLevel == null) continue;
                            JSONObject deviceData = scopeLevel.getJSONObject(guid);
                            if (deviceData != null) {
                                JSONObject stats = deviceData.getJSONObject("statistics");
                                if (stats != null) {
                                    monitor.setMbOut(toSafeString(stats.get("Infiniband_MBOut")));
                                    monitor.setMbIn(toSafeString(stats.get("Infiniband_MBIn")));
                                    monitor.setPckOut(toSafeString(stats.get("Infiniband_PckOut")));
                                    monitor.setPckIn(toSafeString(stats.get("Infiniband_PckIn")));
                                    monitor.setSymbolErrors(toSafeString(stats.get("Infiniband_SymbolErrors")));
                                    monitor.setLinkRecovers(toSafeString(stats.get("Infiniband_LinkRecovers")));
                                    monitor.setLinkDowned(toSafeString(stats.get("Infiniband_LinkDowned")));
                                    monitor.setRcvErrors(toSafeString(stats.get("Infiniband_RcvErrors")));
                                    monitor.setXmtDiscards(toSafeString(stats.get("Infiniband_XmtDiscards")));
                                    monitor.setMbOutRate(toSafeString(stats.get("Infiniband_MBOutRate")));
                                    monitor.setMbInRate(toSafeString(stats.get("Infiniband_MBInRate")));
                                    monitor.setPckOutRate(toSafeString(stats.get("Infiniband_PckOutRate")));
                                    monitor.setPckInRate(toSafeString(stats.get("Infiniband_PckInRate")));
                                }
                                break;
                            }
                        }
                    }
                }

                // 设置告警数
                Integer count = alarmCountMap.get(guid);
                monitor.setAlarmCount(count != null ? String.valueOf(count) : "0");
                monitor.setCreateTime(now);

                result.add(monitor);
            } catch (Exception e) {
                // 单个设备采集失败不影响其他设备
                System.err.println("Failed to collect data for device guid: " + guid + ", error: " + e.getMessage());
            }
        }

        return result;
    }

    /**
     * 将对象安全转换为字符串
     */
    private static String toSafeString(Object obj) {
        if (obj == null) {
            return "0";
        }
        String str = obj.toString().trim();
        return str.isEmpty() ? "0" : str;
    }
}
