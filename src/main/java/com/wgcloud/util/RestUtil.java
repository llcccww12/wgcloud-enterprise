/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wgcloud.dto.HeathMonitorResDto;
import com.wgcloud.entity.HeathMonitor;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.ThreadPoolUtil;
import com.wgcloud.util.msg.WarnOtherUtil;
import com.wgcloud.util.msg.WarnPools;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestUtil {
    private Logger logger = LoggerFactory.getLogger(RestUtil.class);
    @Autowired
    private RestTemplate restTemplate;

    public HeathMonitorResDto post(HeathMonitor heathMonitor, HashMap<String, String> headerMap) {
        HeathMonitorResDto heathMonitorResDto = new HeathMonitorResDto();
        try {
            heathMonitorResDto.setResTimes(20000);
            long startTimes = System.currentTimeMillis();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Accept", "*/*");
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            this.addHeaderMap(headers, headerMap);
            HttpEntity httpEntity = new HttpEntity((Object)heathMonitor.getPostStr(), (MultiValueMap)headers);
            ResponseEntity responseEntity = this.restTemplate.postForEntity(heathMonitor.getHeathUrl(), (Object)httpEntity, String.class, new Object[0]);
            long endTimes = System.currentTimeMillis();
            String resTimes = endTimes - startTimes + "";
            heathMonitorResDto.setResTimes(Integer.valueOf(resTimes));
            heathMonitorResDto.setHeathStatus(responseEntity.getStatusCodeValue());
            this.logger.debug((String)responseEntity.getBody());
            Integer bodyBytes = ((String)responseEntity.getBody()).getBytes(StandardCharsets.UTF_8).length;
            String bodySize = FormatUtil.bytesFormatUnit(String.valueOf(bodyBytes), "byte");
            heathMonitor.setResponseBodySize(bodySize);
            this.responseBodyHandle((ResponseEntity<String>)responseEntity, heathMonitor, heathMonitorResDto);
            return heathMonitorResDto;
        }
        catch (HttpClientErrorException e) {
            this.logger.error("\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            heathMonitorResDto.setHeathStatus(e.getRawStatusCode());
            heathMonitor.setResponseBodySize("0B");
            heathMonitorResDto.setErrorMsg(e.toString());
            return heathMonitorResDto;
        }
        catch (Exception e) {
            this.logger.error("\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            heathMonitorResDto.setHeathStatus(500);
            heathMonitor.setResponseBodySize("0B");
            heathMonitorResDto.setErrorMsg(e.toString());
            return heathMonitorResDto;
        }
    }

    public HeathMonitorResDto postFormData(HeathMonitor heathMonitor, HashMap<String, String> headerMap, HashMap<String, String> formMap) {
        HeathMonitorResDto heathMonitorResDto = new HeathMonitorResDto();
        try {
            heathMonitorResDto.setResTimes(20000);
            long startTimes = System.currentTimeMillis();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("Accept", "*/*");
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            this.addHeaderMap(headers, headerMap);
            LinkedMultiValueMap params = new LinkedMultiValueMap();
            if (null != formMap && formMap.size() > 0) {
                params.setAll(formMap);
            }
            HttpEntity httpEntity = new HttpEntity((Object)params, (MultiValueMap)headers);
            ResponseEntity responseEntity = this.restTemplate.postForEntity(heathMonitor.getHeathUrl(), (Object)httpEntity, String.class, new Object[0]);
            long endTimes = System.currentTimeMillis();
            String resTimes = endTimes - startTimes + "";
            heathMonitorResDto.setResTimes(Integer.valueOf(resTimes));
            heathMonitorResDto.setHeathStatus(responseEntity.getStatusCodeValue());
            this.logger.debug((String)responseEntity.getBody());
            Integer bodyBytes = ((String)responseEntity.getBody()).getBytes(StandardCharsets.UTF_8).length;
            String bodySize = FormatUtil.bytesFormatUnit(String.valueOf(bodyBytes), "byte");
            heathMonitor.setResponseBodySize(bodySize);
            this.responseBodyHandle((ResponseEntity<String>)responseEntity, heathMonitor, heathMonitorResDto);
            return heathMonitorResDto;
        }
        catch (HttpClientErrorException e) {
            this.logger.error("\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            heathMonitorResDto.setHeathStatus(e.getRawStatusCode());
            heathMonitor.setResponseBodySize("0B");
            heathMonitorResDto.setErrorMsg(e.toString());
            return heathMonitorResDto;
        }
        catch (Exception e) {
            this.logger.error("\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            heathMonitorResDto.setHeathStatus(500);
            heathMonitor.setResponseBodySize("0B");
            heathMonitorResDto.setErrorMsg(e.toString());
            return heathMonitorResDto;
        }
    }

    public JSONObject post(String url, JSONObject jsonObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity httpEntity = new HttpEntity((Object)JSONUtil.parse((Object)jsonObject).toString(), (MultiValueMap)headers);
        ResponseEntity responseEntity = this.restTemplate.postForEntity(url, (Object)httpEntity, String.class, new Object[0]);
        return JSONUtil.parseObj((String)((String)responseEntity.getBody()));
    }

    public JSONObject post(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity httpEntity = new HttpEntity("", (MultiValueMap)headers);
        ResponseEntity responseEntity = this.restTemplate.postForEntity(url, (Object)httpEntity, String.class, new Object[0]);
        return JSONUtil.parseObj((String)((String)responseEntity.getBody()));
    }

    public HeathMonitorResDto get(HeathMonitor heathMonitor, HashMap<String, String> headerMap) {
        HeathMonitorResDto heathMonitorResDto = new HeathMonitorResDto();
        try {
            heathMonitorResDto.setResTimes(20000);
            long startTimes = System.currentTimeMillis();
            ResponseEntity responseEntity = null;
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Accept", "*/*");
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
            this.addHeaderMap(headers, headerMap);
            HttpEntity httpEntity = new HttpEntity("", (MultiValueMap)headers);
            responseEntity = this.restTemplate.exchange(heathMonitor.getHeathUrl(), HttpMethod.GET, httpEntity, String.class, new Object[0]);
            long endTimes = System.currentTimeMillis();
            String resTimes = endTimes - startTimes + "";
            heathMonitorResDto.setResTimes(Integer.valueOf(resTimes));
            heathMonitorResDto.setHeathStatus(responseEntity.getStatusCodeValue());
            this.logger.debug((String)responseEntity.getBody());
            Integer bodyBytes = ((String)responseEntity.getBody()).getBytes(StandardCharsets.UTF_8).length;
            String bodySize = FormatUtil.bytesFormatUnit(String.valueOf(bodyBytes), "byte");
            heathMonitor.setResponseBodySize(bodySize);
            this.responseBodyHandle((ResponseEntity<String>)responseEntity, heathMonitor, heathMonitorResDto);
            return heathMonitorResDto;
        }
        catch (HttpClientErrorException e) {
            this.logger.error("\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            heathMonitorResDto.setHeathStatus(e.getRawStatusCode());
            heathMonitor.setResponseBodySize("0B");
            heathMonitorResDto.setErrorMsg(e.toString());
            return heathMonitorResDto;
        }
        catch (Exception e) {
            this.logger.error("\u670d\u52a1\u63a5\u53e3\u68c0\u6d4b\u4efb\u52a1\u9519\u8bef", (Throwable)e);
            heathMonitorResDto.setHeathStatus(500);
            heathMonitor.setResponseBodySize("0B");
            heathMonitorResDto.setErrorMsg(e.toString());
            return heathMonitorResDto;
        }
    }

    public String get(String url) {
        try {
            ResponseEntity responseEntity = null;
            if (url.startsWith("https")) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Accept", MediaType.APPLICATION_JSON.toString());
                headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
                HttpEntity httpEntity = new HttpEntity("", (MultiValueMap)headers);
                responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, new Object[0]);
            } else {
                responseEntity = this.restTemplate.getForEntity(url, String.class, new Object[0]);
            }
            return (String)responseEntity.getBody();
        }
        catch (HttpClientErrorException e) {
            this.logger.error("\u8bf7\u6c42\u5b88\u62a4\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            return "";
        }
        catch (Exception e) {
            this.logger.error("\u8bf7\u6c42\u5b88\u62a4\u8fdb\u7a0b\u4fe1\u606f\u9519\u8bef", (Throwable)e);
            Runnable runnable = () -> {
                if (StringUtils.isEmpty((CharSequence)WarnPools.getWarnMark("wgcloud-daemon-release-error"))) {
                    WarnOtherUtil.sendUtil("\u8bf7\u6c42server\u7aef\u5b88\u62a4\u8fdb\u7a0b\u9519\u8bef", "\u8bf7\u6c42server\u7aef\u5b88\u62a4\u8fdb\u7a0b\u9519\u8bef\uff0c\u8bf7\u68c0\u67e5\u5b88\u62a4\u8fdb\u7a0b\u662f\u5426\u5904\u4e8e\u8fd0\u884c\u72b6\u6001\uff0c\u5982\u679c\u6ca1\u6709\u8fd0\u884c\uff0c\u90a3\u4e48\u76d1\u63a7\u4e3b\u673a\u53ef\u80fd\u4f1a\u51fa\u73b0\u79bb\u7ebf\u60c5\u51b5\u3002" + e.toString(), "", "wgcloud-daemon-release-error", true, "ERROR", "");
                }
            };
            ThreadPoolUtil.executor.execute(runnable);
            return "";
        }
    }

    private void responseBodyHandle(ResponseEntity<String> responseEntity, HeathMonitor heathMonitor, HeathMonitorResDto heathMonitorResDto) {
        heathMonitorResDto.setBodyInfo((String)responseEntity.getBody());
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getResKeyword()) && !((String)responseEntity.getBody()).contains(heathMonitor.getResKeyword())) {
            this.logger.error(heathMonitor.getHeathUrl() + "----\u54cd\u5e94\u5185\u5bb9\u6ca1\u6709\u542b\u6807\u8bc6-------" + heathMonitor.getResKeyword());
            heathMonitorResDto.setHeathStatus(500);
            heathMonitorResDto.setErrorMsg("\u54cd\u5e94\u5185\u5bb9\u6ca1\u6709\u542b\u6807\u8bc6\uff1a" + heathMonitor.getResKeyword());
        }
        if (!StringUtils.isEmpty((CharSequence)heathMonitor.getResNoKeyword())) {
            String[] noKeyWords;
            for (String noKeyWordChar : noKeyWords = heathMonitor.getResNoKeyword().split(",")) {
                if (!((String)responseEntity.getBody()).contains(noKeyWordChar)) continue;
                this.logger.error(heathMonitor.getHeathUrl() + "----\u54cd\u5e94\u5185\u5bb9\u5305\u542b(\u8bbe\u7f6e\u4e0d\u80fd\u5305\u542b\u7684\u5173\u952e\u5b57)\u6807\u8bc6-------" + noKeyWordChar);
                heathMonitorResDto.setHeathStatus(500);
                heathMonitorResDto.setErrorMsg("\u54cd\u5e94\u5185\u5bb9\u5305\u542b(\u8bbe\u7f6e\u4e0d\u80fd\u5305\u542b\u7684\u5173\u952e\u5b57)\u6807\u8bc6\uff1a" + noKeyWordChar);
                break;
            }
        }
    }

    private void addHeaderMap(HttpHeaders httpHeaders, HashMap<String, String> headerMap) {
        if (null != headerMap && headerMap.size() > 0) {
            httpHeaders.setAll(headerMap);
            if (!StringUtils.isEmpty((CharSequence)headerMap.get("AuthorizationUsername")) && !StringUtils.isEmpty((CharSequence)headerMap.get("AuthorizationPassword"))) {
                String Username = headerMap.get("AuthorizationUsername");
                httpHeaders.remove("AuthorizationUsername");
                String Password = headerMap.get("AuthorizationPassword");
                httpHeaders.remove("AuthorizationPassword");
                String authStr = Username.concat(":").concat(Password);
                String authStrEnc = new String(Base64.encodeBase64((byte[])authStr.getBytes()));
                httpHeaders.set("Authorization", "Basic ".concat(authStrEnc));
            }
            if (!StringUtils.isEmpty((CharSequence)headerMap.get("AuthorizationBearerToken"))) {
                String bearerToken = headerMap.get("AuthorizationBearerToken");
                httpHeaders.remove("AuthorizationBearerToken");
                httpHeaders.set("Authorization", "Bearer " + bearerToken);
            }
        }
    }
}

