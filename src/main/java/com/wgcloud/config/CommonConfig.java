/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="base")
public class CommonConfig {
    private String account = "admin";
    private String accountPwd = "111111";
    private String accountPhone;
    private String sendPhoneCodeScript;
    private String pwdExpDate;
    private String vercodeCheck;
    private String guestAccount = "guest";
    private String guestAccountPwd = "111111";
    private String wgToken = "";
    private String dashView;
    private String dashViewIpHide;
    private String dapingView;
    private String openDataAPI;
    private Integer dbTableTimes = 3600;
    private Integer heathTimes = 600;
    private Integer ftpTimes = 600;
    private Integer dceTimes = 900;
    private String dceCharset;
    private Integer snmpTimes = 900;
    private Integer warnCacheTimes = 7200;
    private String nodeType = "master";
    private Integer historyDataOut = 10;
    private Integer chartDataMaxShowValue = 10;
    private Integer pageSize = 20;
    private String copyRight;
    private String icoName = "";
    private String logoName = "";
    private String wgName = "";
    private String wgShortName = "";
    private String webSsh;
    private Integer webSshPort = 9998;
    private String showWarnCount;
    private String shellToRun;
    private String shellToRunBlock;
    private String sqlInKeys;
    private String daemonUrl;
    private String mailTitlePrefix;
    private String mailContentSuffix;
    private String hostGroup;
    private String userInfoManage;
    private Integer maxPoolSize;
    private String dashViewAutoData;
    private String dashViewListAutoData;
    private String openSSO;
    private String copyRightLoginContent;
    private String copyRightMainContent;
    private String warnSoundName;
    private Integer dapingRefreshTimes = 600;
    private String showVersion;
    private String redisUrl;
    private String llmData;

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountPwd() {
        return this.accountPwd;
    }

    public void setAccountPwd(String accountPwd) {
        this.accountPwd = accountPwd;
    }

    public String getWgToken() {
        return this.wgToken;
    }

    public void setWgToken(String wgToken) {
        this.wgToken = wgToken;
    }

    public String getDashView() {
        if (StringUtils.isEmpty((CharSequence)this.dashView)) {
            return "true";
        }
        return this.dashView;
    }

    public void setDashView(String dashView) {
        this.dashView = dashView;
    }

    public Integer getDbTableTimes() {
        if (this.dbTableTimes == null) {
            return 3600;
        }
        return this.dbTableTimes;
    }

    public void setDbTableTimes(Integer dbTableTimes) {
        this.dbTableTimes = dbTableTimes;
    }

    public Integer getHeathTimes() {
        if (this.heathTimes == null) {
            return 600;
        }
        return this.heathTimes;
    }

    public void setHeathTimes(Integer heathTimes) {
        this.heathTimes = heathTimes;
    }

    public String getNodeType() {
        return this.nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getHistoryDataOut() {
        if (this.historyDataOut == null) {
            return 10;
        }
        return this.historyDataOut;
    }

    public void setHistoryDataOut(Integer historyDataOut) {
        this.historyDataOut = historyDataOut;
    }

    public Integer getWarnCacheTimes() {
        if (this.warnCacheTimes == null) {
            return 7200;
        }
        return this.warnCacheTimes;
    }

    public void setWarnCacheTimes(Integer warnCacheTimes) {
        this.warnCacheTimes = warnCacheTimes;
    }

    public String getIcoName() {
        return this.icoName;
    }

    public void setIcoName(String icoName) {
        this.icoName = icoName;
    }

    public String getLogoName() {
        return this.logoName;
    }

    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    public String getWgName() {
        return this.wgName;
    }

    public void setWgName(String wgName) {
        this.wgName = wgName;
    }

    public String getWgShortName() {
        return this.wgShortName;
    }

    public void setWgShortName(String wgShortName) {
        this.wgShortName = wgShortName;
    }

    public String getDapingView() {
        if (StringUtils.isEmpty((CharSequence)this.dapingView)) {
            return "true";
        }
        return this.dapingView;
    }

    public void setDapingView(String dapingView) {
        this.dapingView = dapingView;
    }

    public String getWebSsh() {
        if (StringUtils.isEmpty((CharSequence)this.webSsh)) {
            return "true";
        }
        return this.webSsh;
    }

    public void setWebSsh(String webSsh) {
        this.webSsh = webSsh;
    }

    public Integer getWebSshPort() {
        if (this.warnCacheTimes == null) {
            return 9998;
        }
        return this.webSshPort;
    }

    public void setWebSshPort(Integer webSshPort) {
        this.webSshPort = webSshPort;
    }

    public Integer getDceTimes() {
        if (this.dceTimes == null) {
            return 900;
        }
        return this.dceTimes;
    }

    public void setDceTimes(Integer dceTimes) {
        this.dceTimes = dceTimes;
    }

    public String getShellToRun() {
        if (StringUtils.isEmpty((CharSequence)this.shellToRun)) {
            return "false";
        }
        return this.shellToRun;
    }

    public void setShellToRun(String shellToRun) {
        this.shellToRun = shellToRun;
    }

    public String getShellToRunBlock() {
        return this.shellToRunBlock;
    }

    public void setShellToRunBlock(String shellToRunBlock) {
        this.shellToRunBlock = shellToRunBlock;
    }

    public String getDaemonUrl() {
        if (StringUtils.isEmpty((CharSequence)this.daemonUrl)) {
            return "http://localhost:9997";
        }
        return this.daemonUrl;
    }

    public void setDaemonUrl(String daemonUrl) {
        this.daemonUrl = daemonUrl;
    }

    public String getGuestAccount() {
        return this.guestAccount;
    }

    public void setGuestAccount(String guestAccount) {
        this.guestAccount = guestAccount;
    }

    public String getGuestAccountPwd() {
        return this.guestAccountPwd;
    }

    public void setGuestAccountPwd(String guestAccountPwd) {
        this.guestAccountPwd = guestAccountPwd;
    }

    public String getMailTitlePrefix() {
        return this.mailTitlePrefix;
    }

    public void setMailTitlePrefix(String mailTitlePrefix) {
        this.mailTitlePrefix = mailTitlePrefix;
    }

    public String getMailContentSuffix() {
        return this.mailContentSuffix;
    }

    public void setMailContentSuffix(String mailContentSuffix) {
        this.mailContentSuffix = mailContentSuffix;
    }

    public String getHostGroup() {
        if (StringUtils.isEmpty((CharSequence)this.hostGroup)) {
            return "false";
        }
        return this.hostGroup;
    }

    public void setHostGroup(String hostGroup) {
        this.hostGroup = hostGroup;
    }

    public String getUserInfoManage() {
        if (StringUtils.isEmpty((CharSequence)this.userInfoManage)) {
            return "false";
        }
        return this.userInfoManage;
    }

    public void setUserInfoManage(String userInfoManage) {
        this.userInfoManage = userInfoManage;
    }

    public String getCopyRight() {
        if (StringUtils.isEmpty((CharSequence)this.copyRight)) {
            return "true";
        }
        return this.copyRight;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    public Integer getPageSize() {
        if (this.pageSize < 10) {
            this.pageSize = 10;
        }
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getSnmpTimes() {
        if (this.snmpTimes == null) {
            return 1200;
        }
        return this.snmpTimes;
    }

    public void setSnmpTimes(Integer snmpTimes) {
        this.snmpTimes = snmpTimes;
    }

    public String getSqlInKeys() {
        if (StringUtils.isEmpty((CharSequence)this.sqlInKeys)) {
            return "execute ,delete ,drop ,alter ,rename ,modify ";
        }
        return this.sqlInKeys;
    }

    public void setSqlInKeys(String sqlInKeys) {
        this.sqlInKeys = sqlInKeys;
    }

    public String getShowWarnCount() {
        if (StringUtils.isEmpty((CharSequence)this.showWarnCount)) {
            return "false";
        }
        return this.showWarnCount;
    }

    public void setShowWarnCount(String showWarnCount) {
        this.showWarnCount = showWarnCount;
    }

    public String getDashViewIpHide() {
        if (StringUtils.isEmpty((CharSequence)this.dashViewIpHide)) {
            return "true";
        }
        return this.dashViewIpHide;
    }

    public void setDashViewIpHide(String dashViewIpHide) {
        this.dashViewIpHide = dashViewIpHide;
    }

    public Integer getFtpTimes() {
        return this.ftpTimes;
    }

    public void setFtpTimes(Integer ftpTimes) {
        this.ftpTimes = ftpTimes;
    }

    public String getOpenDataAPI() {
        if (StringUtils.isEmpty((CharSequence)this.openDataAPI)) {
            return "true";
        }
        return this.openDataAPI;
    }

    public void setOpenDataAPI(String openDataAPI) {
        this.openDataAPI = openDataAPI;
    }

    public Integer getMaxPoolSize() {
        return this.maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public String getDashViewAutoData() {
        if (StringUtils.isEmpty((CharSequence)this.dashViewAutoData)) {
            return "false";
        }
        return this.dashViewAutoData;
    }

    public void setDashViewAutoData(String dashViewAutoData) {
        this.dashViewAutoData = dashViewAutoData;
    }

    public String getDashViewListAutoData() {
        if (StringUtils.isEmpty((CharSequence)this.dashViewListAutoData)) {
            return "false";
        }
        return this.dashViewListAutoData;
    }

    public void setDashViewListAutoData(String dashViewListAutoData) {
        this.dashViewListAutoData = dashViewListAutoData;
    }

    public String getOpenSSO() {
        return this.openSSO;
    }

    public void setOpenSSO(String openSSO) {
        this.openSSO = openSSO;
    }

    public String getCopyRightLoginContent() {
        return this.copyRightLoginContent;
    }

    public void setCopyRightLoginContent(String copyRightLoginContent) {
        this.copyRightLoginContent = copyRightLoginContent;
    }

    public String getCopyRightMainContent() {
        return this.copyRightMainContent;
    }

    public void setCopyRightMainContent(String copyRightMainContent) {
        this.copyRightMainContent = copyRightMainContent;
    }

    public Integer getChartDataMaxShowValue() {
        if (null == this.chartDataMaxShowValue) {
            return 4000;
        }
        return this.chartDataMaxShowValue;
    }

    public void setChartDataMaxShowValue(Integer chartDataMaxShowValue) {
        this.chartDataMaxShowValue = chartDataMaxShowValue;
    }

    public String getWarnSoundName() {
        return this.warnSoundName;
    }

    public void setWarnSoundName(String warnSoundName) {
        this.warnSoundName = warnSoundName;
    }

    public Integer getDapingRefreshTimes() {
        if (null == this.dapingRefreshTimes) {
            return 600;
        }
        return this.dapingRefreshTimes;
    }

    public void setDapingRefreshTimes(Integer dapingRefreshTimes) {
        this.dapingRefreshTimes = dapingRefreshTimes;
    }

    public String getVercodeCheck() {
        if (StringUtils.isEmpty((CharSequence)this.vercodeCheck)) {
            return "false";
        }
        return this.vercodeCheck;
    }

    public void setVercodeCheck(String vercodeCheck) {
        this.vercodeCheck = vercodeCheck;
    }

    public String getShowVersion() {
        if (StringUtils.isEmpty((CharSequence)this.showVersion)) {
            return "true";
        }
        return this.showVersion;
    }

    public void setShowVersion(String showVersion) {
        this.showVersion = showVersion;
    }

    public String getRedisUrl() {
        return this.redisUrl;
    }

    public void setRedisUrl(String redisUrl) {
        this.redisUrl = redisUrl;
    }

    public String getPwdExpDate() {
        if (StringUtils.isEmpty((CharSequence)this.pwdExpDate)) {
            return "2099-11-10";
        }
        return this.pwdExpDate;
    }

    public void setPwdExpDate(String pwdExpDate) {
        this.pwdExpDate = pwdExpDate;
    }

    public String getLlmData() {
        if (StringUtils.isEmpty((CharSequence)this.llmData)) {
            return "true";
        }
        return this.llmData;
    }

    public void setLlmData(String llmData) {
        this.llmData = llmData;
    }

    public String getDceCharset() {
        if (StringUtils.isEmpty((CharSequence)this.dceCharset)) {
            return "UTF-8";
        }
        return this.dceCharset;
    }

    public void setDceCharset(String dceCharset) {
        this.dceCharset = dceCharset;
    }

    public String getAccountPhone() {
        return this.accountPhone;
    }

    public void setAccountPhone(String accountPhone) {
        this.accountPhone = accountPhone;
    }

    public String getSendPhoneCodeScript() {
        return this.sendPhoneCodeScript;
    }

    public void setSendPhoneCodeScript(String sendPhoneCodeScript) {
        this.sendPhoneCodeScript = sendPhoneCodeScript;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommonConfig)) {
            return false;
        }
        CommonConfig other = (CommonConfig)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$dbTableTimes = this.getDbTableTimes();
        Integer other$dbTableTimes = other.getDbTableTimes();
        if (this$dbTableTimes == null ? other$dbTableTimes != null : !((Object)this$dbTableTimes).equals(other$dbTableTimes)) {
            return false;
        }
        Integer this$heathTimes = this.getHeathTimes();
        Integer other$heathTimes = other.getHeathTimes();
        if (this$heathTimes == null ? other$heathTimes != null : !((Object)this$heathTimes).equals(other$heathTimes)) {
            return false;
        }
        Integer this$ftpTimes = this.getFtpTimes();
        Integer other$ftpTimes = other.getFtpTimes();
        if (this$ftpTimes == null ? other$ftpTimes != null : !((Object)this$ftpTimes).equals(other$ftpTimes)) {
            return false;
        }
        Integer this$dceTimes = this.getDceTimes();
        Integer other$dceTimes = other.getDceTimes();
        if (this$dceTimes == null ? other$dceTimes != null : !((Object)this$dceTimes).equals(other$dceTimes)) {
            return false;
        }
        Integer this$snmpTimes = this.getSnmpTimes();
        Integer other$snmpTimes = other.getSnmpTimes();
        if (this$snmpTimes == null ? other$snmpTimes != null : !((Object)this$snmpTimes).equals(other$snmpTimes)) {
            return false;
        }
        Integer this$warnCacheTimes = this.getWarnCacheTimes();
        Integer other$warnCacheTimes = other.getWarnCacheTimes();
        if (this$warnCacheTimes == null ? other$warnCacheTimes != null : !((Object)this$warnCacheTimes).equals(other$warnCacheTimes)) {
            return false;
        }
        Integer this$historyDataOut = this.getHistoryDataOut();
        Integer other$historyDataOut = other.getHistoryDataOut();
        if (this$historyDataOut == null ? other$historyDataOut != null : !((Object)this$historyDataOut).equals(other$historyDataOut)) {
            return false;
        }
        Integer this$chartDataMaxShowValue = this.getChartDataMaxShowValue();
        Integer other$chartDataMaxShowValue = other.getChartDataMaxShowValue();
        if (this$chartDataMaxShowValue == null ? other$chartDataMaxShowValue != null : !((Object)this$chartDataMaxShowValue).equals(other$chartDataMaxShowValue)) {
            return false;
        }
        Integer this$pageSize = this.getPageSize();
        Integer other$pageSize = other.getPageSize();
        if (this$pageSize == null ? other$pageSize != null : !((Object)this$pageSize).equals(other$pageSize)) {
            return false;
        }
        Integer this$webSshPort = this.getWebSshPort();
        Integer other$webSshPort = other.getWebSshPort();
        if (this$webSshPort == null ? other$webSshPort != null : !((Object)this$webSshPort).equals(other$webSshPort)) {
            return false;
        }
        Integer this$maxPoolSize = this.getMaxPoolSize();
        Integer other$maxPoolSize = other.getMaxPoolSize();
        if (this$maxPoolSize == null ? other$maxPoolSize != null : !((Object)this$maxPoolSize).equals(other$maxPoolSize)) {
            return false;
        }
        Integer this$dapingRefreshTimes = this.getDapingRefreshTimes();
        Integer other$dapingRefreshTimes = other.getDapingRefreshTimes();
        if (this$dapingRefreshTimes == null ? other$dapingRefreshTimes != null : !((Object)this$dapingRefreshTimes).equals(other$dapingRefreshTimes)) {
            return false;
        }
        String this$account = this.getAccount();
        String other$account = other.getAccount();
        if (this$account == null ? other$account != null : !this$account.equals(other$account)) {
            return false;
        }
        String this$accountPwd = this.getAccountPwd();
        String other$accountPwd = other.getAccountPwd();
        if (this$accountPwd == null ? other$accountPwd != null : !this$accountPwd.equals(other$accountPwd)) {
            return false;
        }
        String this$accountPhone = this.getAccountPhone();
        String other$accountPhone = other.getAccountPhone();
        if (this$accountPhone == null ? other$accountPhone != null : !this$accountPhone.equals(other$accountPhone)) {
            return false;
        }
        String this$sendPhoneCodeScript = this.getSendPhoneCodeScript();
        String other$sendPhoneCodeScript = other.getSendPhoneCodeScript();
        if (this$sendPhoneCodeScript == null ? other$sendPhoneCodeScript != null : !this$sendPhoneCodeScript.equals(other$sendPhoneCodeScript)) {
            return false;
        }
        String this$pwdExpDate = this.getPwdExpDate();
        String other$pwdExpDate = other.getPwdExpDate();
        if (this$pwdExpDate == null ? other$pwdExpDate != null : !this$pwdExpDate.equals(other$pwdExpDate)) {
            return false;
        }
        String this$vercodeCheck = this.getVercodeCheck();
        String other$vercodeCheck = other.getVercodeCheck();
        if (this$vercodeCheck == null ? other$vercodeCheck != null : !this$vercodeCheck.equals(other$vercodeCheck)) {
            return false;
        }
        String this$guestAccount = this.getGuestAccount();
        String other$guestAccount = other.getGuestAccount();
        if (this$guestAccount == null ? other$guestAccount != null : !this$guestAccount.equals(other$guestAccount)) {
            return false;
        }
        String this$guestAccountPwd = this.getGuestAccountPwd();
        String other$guestAccountPwd = other.getGuestAccountPwd();
        if (this$guestAccountPwd == null ? other$guestAccountPwd != null : !this$guestAccountPwd.equals(other$guestAccountPwd)) {
            return false;
        }
        String this$wgToken = this.getWgToken();
        String other$wgToken = other.getWgToken();
        if (this$wgToken == null ? other$wgToken != null : !this$wgToken.equals(other$wgToken)) {
            return false;
        }
        String this$dashView = this.getDashView();
        String other$dashView = other.getDashView();
        if (this$dashView == null ? other$dashView != null : !this$dashView.equals(other$dashView)) {
            return false;
        }
        String this$dashViewIpHide = this.getDashViewIpHide();
        String other$dashViewIpHide = other.getDashViewIpHide();
        if (this$dashViewIpHide == null ? other$dashViewIpHide != null : !this$dashViewIpHide.equals(other$dashViewIpHide)) {
            return false;
        }
        String this$dapingView = this.getDapingView();
        String other$dapingView = other.getDapingView();
        if (this$dapingView == null ? other$dapingView != null : !this$dapingView.equals(other$dapingView)) {
            return false;
        }
        String this$openDataAPI = this.getOpenDataAPI();
        String other$openDataAPI = other.getOpenDataAPI();
        if (this$openDataAPI == null ? other$openDataAPI != null : !this$openDataAPI.equals(other$openDataAPI)) {
            return false;
        }
        String this$dceCharset = this.getDceCharset();
        String other$dceCharset = other.getDceCharset();
        if (this$dceCharset == null ? other$dceCharset != null : !this$dceCharset.equals(other$dceCharset)) {
            return false;
        }
        String this$nodeType = this.getNodeType();
        String other$nodeType = other.getNodeType();
        if (this$nodeType == null ? other$nodeType != null : !this$nodeType.equals(other$nodeType)) {
            return false;
        }
        String this$copyRight = this.getCopyRight();
        String other$copyRight = other.getCopyRight();
        if (this$copyRight == null ? other$copyRight != null : !this$copyRight.equals(other$copyRight)) {
            return false;
        }
        String this$icoName = this.getIcoName();
        String other$icoName = other.getIcoName();
        if (this$icoName == null ? other$icoName != null : !this$icoName.equals(other$icoName)) {
            return false;
        }
        String this$logoName = this.getLogoName();
        String other$logoName = other.getLogoName();
        if (this$logoName == null ? other$logoName != null : !this$logoName.equals(other$logoName)) {
            return false;
        }
        String this$wgName = this.getWgName();
        String other$wgName = other.getWgName();
        if (this$wgName == null ? other$wgName != null : !this$wgName.equals(other$wgName)) {
            return false;
        }
        String this$wgShortName = this.getWgShortName();
        String other$wgShortName = other.getWgShortName();
        if (this$wgShortName == null ? other$wgShortName != null : !this$wgShortName.equals(other$wgShortName)) {
            return false;
        }
        String this$webSsh = this.getWebSsh();
        String other$webSsh = other.getWebSsh();
        if (this$webSsh == null ? other$webSsh != null : !this$webSsh.equals(other$webSsh)) {
            return false;
        }
        String this$showWarnCount = this.getShowWarnCount();
        String other$showWarnCount = other.getShowWarnCount();
        if (this$showWarnCount == null ? other$showWarnCount != null : !this$showWarnCount.equals(other$showWarnCount)) {
            return false;
        }
        String this$shellToRun = this.getShellToRun();
        String other$shellToRun = other.getShellToRun();
        if (this$shellToRun == null ? other$shellToRun != null : !this$shellToRun.equals(other$shellToRun)) {
            return false;
        }
        String this$shellToRunBlock = this.getShellToRunBlock();
        String other$shellToRunBlock = other.getShellToRunBlock();
        if (this$shellToRunBlock == null ? other$shellToRunBlock != null : !this$shellToRunBlock.equals(other$shellToRunBlock)) {
            return false;
        }
        String this$sqlInKeys = this.getSqlInKeys();
        String other$sqlInKeys = other.getSqlInKeys();
        if (this$sqlInKeys == null ? other$sqlInKeys != null : !this$sqlInKeys.equals(other$sqlInKeys)) {
            return false;
        }
        String this$daemonUrl = this.getDaemonUrl();
        String other$daemonUrl = other.getDaemonUrl();
        if (this$daemonUrl == null ? other$daemonUrl != null : !this$daemonUrl.equals(other$daemonUrl)) {
            return false;
        }
        String this$mailTitlePrefix = this.getMailTitlePrefix();
        String other$mailTitlePrefix = other.getMailTitlePrefix();
        if (this$mailTitlePrefix == null ? other$mailTitlePrefix != null : !this$mailTitlePrefix.equals(other$mailTitlePrefix)) {
            return false;
        }
        String this$mailContentSuffix = this.getMailContentSuffix();
        String other$mailContentSuffix = other.getMailContentSuffix();
        if (this$mailContentSuffix == null ? other$mailContentSuffix != null : !this$mailContentSuffix.equals(other$mailContentSuffix)) {
            return false;
        }
        String this$hostGroup = this.getHostGroup();
        String other$hostGroup = other.getHostGroup();
        if (this$hostGroup == null ? other$hostGroup != null : !this$hostGroup.equals(other$hostGroup)) {
            return false;
        }
        String this$userInfoManage = this.getUserInfoManage();
        String other$userInfoManage = other.getUserInfoManage();
        if (this$userInfoManage == null ? other$userInfoManage != null : !this$userInfoManage.equals(other$userInfoManage)) {
            return false;
        }
        String this$dashViewAutoData = this.getDashViewAutoData();
        String other$dashViewAutoData = other.getDashViewAutoData();
        if (this$dashViewAutoData == null ? other$dashViewAutoData != null : !this$dashViewAutoData.equals(other$dashViewAutoData)) {
            return false;
        }
        String this$dashViewListAutoData = this.getDashViewListAutoData();
        String other$dashViewListAutoData = other.getDashViewListAutoData();
        if (this$dashViewListAutoData == null ? other$dashViewListAutoData != null : !this$dashViewListAutoData.equals(other$dashViewListAutoData)) {
            return false;
        }
        String this$openSSO = this.getOpenSSO();
        String other$openSSO = other.getOpenSSO();
        if (this$openSSO == null ? other$openSSO != null : !this$openSSO.equals(other$openSSO)) {
            return false;
        }
        String this$copyRightLoginContent = this.getCopyRightLoginContent();
        String other$copyRightLoginContent = other.getCopyRightLoginContent();
        if (this$copyRightLoginContent == null ? other$copyRightLoginContent != null : !this$copyRightLoginContent.equals(other$copyRightLoginContent)) {
            return false;
        }
        String this$copyRightMainContent = this.getCopyRightMainContent();
        String other$copyRightMainContent = other.getCopyRightMainContent();
        if (this$copyRightMainContent == null ? other$copyRightMainContent != null : !this$copyRightMainContent.equals(other$copyRightMainContent)) {
            return false;
        }
        String this$warnSoundName = this.getWarnSoundName();
        String other$warnSoundName = other.getWarnSoundName();
        if (this$warnSoundName == null ? other$warnSoundName != null : !this$warnSoundName.equals(other$warnSoundName)) {
            return false;
        }
        String this$showVersion = this.getShowVersion();
        String other$showVersion = other.getShowVersion();
        if (this$showVersion == null ? other$showVersion != null : !this$showVersion.equals(other$showVersion)) {
            return false;
        }
        String this$redisUrl = this.getRedisUrl();
        String other$redisUrl = other.getRedisUrl();
        if (this$redisUrl == null ? other$redisUrl != null : !this$redisUrl.equals(other$redisUrl)) {
            return false;
        }
        String this$llmData = this.getLlmData();
        String other$llmData = other.getLlmData();
        return !(this$llmData == null ? other$llmData != null : !this$llmData.equals(other$llmData));
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommonConfig;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $dbTableTimes = this.getDbTableTimes();
        result = result * 59 + ($dbTableTimes == null ? 43 : ((Object)$dbTableTimes).hashCode());
        Integer $heathTimes = this.getHeathTimes();
        result = result * 59 + ($heathTimes == null ? 43 : ((Object)$heathTimes).hashCode());
        Integer $ftpTimes = this.getFtpTimes();
        result = result * 59 + ($ftpTimes == null ? 43 : ((Object)$ftpTimes).hashCode());
        Integer $dceTimes = this.getDceTimes();
        result = result * 59 + ($dceTimes == null ? 43 : ((Object)$dceTimes).hashCode());
        Integer $snmpTimes = this.getSnmpTimes();
        result = result * 59 + ($snmpTimes == null ? 43 : ((Object)$snmpTimes).hashCode());
        Integer $warnCacheTimes = this.getWarnCacheTimes();
        result = result * 59 + ($warnCacheTimes == null ? 43 : ((Object)$warnCacheTimes).hashCode());
        Integer $historyDataOut = this.getHistoryDataOut();
        result = result * 59 + ($historyDataOut == null ? 43 : ((Object)$historyDataOut).hashCode());
        Integer $chartDataMaxShowValue = this.getChartDataMaxShowValue();
        result = result * 59 + ($chartDataMaxShowValue == null ? 43 : ((Object)$chartDataMaxShowValue).hashCode());
        Integer $pageSize = this.getPageSize();
        result = result * 59 + ($pageSize == null ? 43 : ((Object)$pageSize).hashCode());
        Integer $webSshPort = this.getWebSshPort();
        result = result * 59 + ($webSshPort == null ? 43 : ((Object)$webSshPort).hashCode());
        Integer $maxPoolSize = this.getMaxPoolSize();
        result = result * 59 + ($maxPoolSize == null ? 43 : ((Object)$maxPoolSize).hashCode());
        Integer $dapingRefreshTimes = this.getDapingRefreshTimes();
        result = result * 59 + ($dapingRefreshTimes == null ? 43 : ((Object)$dapingRefreshTimes).hashCode());
        String $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        String $accountPwd = this.getAccountPwd();
        result = result * 59 + ($accountPwd == null ? 43 : $accountPwd.hashCode());
        String $accountPhone = this.getAccountPhone();
        result = result * 59 + ($accountPhone == null ? 43 : $accountPhone.hashCode());
        String $sendPhoneCodeScript = this.getSendPhoneCodeScript();
        result = result * 59 + ($sendPhoneCodeScript == null ? 43 : $sendPhoneCodeScript.hashCode());
        String $pwdExpDate = this.getPwdExpDate();
        result = result * 59 + ($pwdExpDate == null ? 43 : $pwdExpDate.hashCode());
        String $vercodeCheck = this.getVercodeCheck();
        result = result * 59 + ($vercodeCheck == null ? 43 : $vercodeCheck.hashCode());
        String $guestAccount = this.getGuestAccount();
        result = result * 59 + ($guestAccount == null ? 43 : $guestAccount.hashCode());
        String $guestAccountPwd = this.getGuestAccountPwd();
        result = result * 59 + ($guestAccountPwd == null ? 43 : $guestAccountPwd.hashCode());
        String $wgToken = this.getWgToken();
        result = result * 59 + ($wgToken == null ? 43 : $wgToken.hashCode());
        String $dashView = this.getDashView();
        result = result * 59 + ($dashView == null ? 43 : $dashView.hashCode());
        String $dashViewIpHide = this.getDashViewIpHide();
        result = result * 59 + ($dashViewIpHide == null ? 43 : $dashViewIpHide.hashCode());
        String $dapingView = this.getDapingView();
        result = result * 59 + ($dapingView == null ? 43 : $dapingView.hashCode());
        String $openDataAPI = this.getOpenDataAPI();
        result = result * 59 + ($openDataAPI == null ? 43 : $openDataAPI.hashCode());
        String $dceCharset = this.getDceCharset();
        result = result * 59 + ($dceCharset == null ? 43 : $dceCharset.hashCode());
        String $nodeType = this.getNodeType();
        result = result * 59 + ($nodeType == null ? 43 : $nodeType.hashCode());
        String $copyRight = this.getCopyRight();
        result = result * 59 + ($copyRight == null ? 43 : $copyRight.hashCode());
        String $icoName = this.getIcoName();
        result = result * 59 + ($icoName == null ? 43 : $icoName.hashCode());
        String $logoName = this.getLogoName();
        result = result * 59 + ($logoName == null ? 43 : $logoName.hashCode());
        String $wgName = this.getWgName();
        result = result * 59 + ($wgName == null ? 43 : $wgName.hashCode());
        String $wgShortName = this.getWgShortName();
        result = result * 59 + ($wgShortName == null ? 43 : $wgShortName.hashCode());
        String $webSsh = this.getWebSsh();
        result = result * 59 + ($webSsh == null ? 43 : $webSsh.hashCode());
        String $showWarnCount = this.getShowWarnCount();
        result = result * 59 + ($showWarnCount == null ? 43 : $showWarnCount.hashCode());
        String $shellToRun = this.getShellToRun();
        result = result * 59 + ($shellToRun == null ? 43 : $shellToRun.hashCode());
        String $shellToRunBlock = this.getShellToRunBlock();
        result = result * 59 + ($shellToRunBlock == null ? 43 : $shellToRunBlock.hashCode());
        String $sqlInKeys = this.getSqlInKeys();
        result = result * 59 + ($sqlInKeys == null ? 43 : $sqlInKeys.hashCode());
        String $daemonUrl = this.getDaemonUrl();
        result = result * 59 + ($daemonUrl == null ? 43 : $daemonUrl.hashCode());
        String $mailTitlePrefix = this.getMailTitlePrefix();
        result = result * 59 + ($mailTitlePrefix == null ? 43 : $mailTitlePrefix.hashCode());
        String $mailContentSuffix = this.getMailContentSuffix();
        result = result * 59 + ($mailContentSuffix == null ? 43 : $mailContentSuffix.hashCode());
        String $hostGroup = this.getHostGroup();
        result = result * 59 + ($hostGroup == null ? 43 : $hostGroup.hashCode());
        String $userInfoManage = this.getUserInfoManage();
        result = result * 59 + ($userInfoManage == null ? 43 : $userInfoManage.hashCode());
        String $dashViewAutoData = this.getDashViewAutoData();
        result = result * 59 + ($dashViewAutoData == null ? 43 : $dashViewAutoData.hashCode());
        String $dashViewListAutoData = this.getDashViewListAutoData();
        result = result * 59 + ($dashViewListAutoData == null ? 43 : $dashViewListAutoData.hashCode());
        String $openSSO = this.getOpenSSO();
        result = result * 59 + ($openSSO == null ? 43 : $openSSO.hashCode());
        String $copyRightLoginContent = this.getCopyRightLoginContent();
        result = result * 59 + ($copyRightLoginContent == null ? 43 : $copyRightLoginContent.hashCode());
        String $copyRightMainContent = this.getCopyRightMainContent();
        result = result * 59 + ($copyRightMainContent == null ? 43 : $copyRightMainContent.hashCode());
        String $warnSoundName = this.getWarnSoundName();
        result = result * 59 + ($warnSoundName == null ? 43 : $warnSoundName.hashCode());
        String $showVersion = this.getShowVersion();
        result = result * 59 + ($showVersion == null ? 43 : $showVersion.hashCode());
        String $redisUrl = this.getRedisUrl();
        result = result * 59 + ($redisUrl == null ? 43 : $redisUrl.hashCode());
        String $llmData = this.getLlmData();
        result = result * 59 + ($llmData == null ? 43 : $llmData.hashCode());
        return result;
    }

    public String toString() {
        return "CommonConfig(account=" + this.getAccount() + ", accountPwd=" + this.getAccountPwd() + ", accountPhone=" + this.getAccountPhone() + ", sendPhoneCodeScript=" + this.getSendPhoneCodeScript() + ", pwdExpDate=" + this.getPwdExpDate() + ", vercodeCheck=" + this.getVercodeCheck() + ", guestAccount=" + this.getGuestAccount() + ", guestAccountPwd=" + this.getGuestAccountPwd() + ", wgToken=" + this.getWgToken() + ", dashView=" + this.getDashView() + ", dashViewIpHide=" + this.getDashViewIpHide() + ", dapingView=" + this.getDapingView() + ", openDataAPI=" + this.getOpenDataAPI() + ", dbTableTimes=" + this.getDbTableTimes() + ", heathTimes=" + this.getHeathTimes() + ", ftpTimes=" + this.getFtpTimes() + ", dceTimes=" + this.getDceTimes() + ", dceCharset=" + this.getDceCharset() + ", snmpTimes=" + this.getSnmpTimes() + ", warnCacheTimes=" + this.getWarnCacheTimes() + ", nodeType=" + this.getNodeType() + ", historyDataOut=" + this.getHistoryDataOut() + ", chartDataMaxShowValue=" + this.getChartDataMaxShowValue() + ", pageSize=" + this.getPageSize() + ", copyRight=" + this.getCopyRight() + ", icoName=" + this.getIcoName() + ", logoName=" + this.getLogoName() + ", wgName=" + this.getWgName() + ", wgShortName=" + this.getWgShortName() + ", webSsh=" + this.getWebSsh() + ", webSshPort=" + this.getWebSshPort() + ", showWarnCount=" + this.getShowWarnCount() + ", shellToRun=" + this.getShellToRun() + ", shellToRunBlock=" + this.getShellToRunBlock() + ", sqlInKeys=" + this.getSqlInKeys() + ", daemonUrl=" + this.getDaemonUrl() + ", mailTitlePrefix=" + this.getMailTitlePrefix() + ", mailContentSuffix=" + this.getMailContentSuffix() + ", hostGroup=" + this.getHostGroup() + ", userInfoManage=" + this.getUserInfoManage() + ", maxPoolSize=" + this.getMaxPoolSize() + ", dashViewAutoData=" + this.getDashViewAutoData() + ", dashViewListAutoData=" + this.getDashViewListAutoData() + ", openSSO=" + this.getOpenSSO() + ", copyRightLoginContent=" + this.getCopyRightLoginContent() + ", copyRightMainContent=" + this.getCopyRightMainContent() + ", warnSoundName=" + this.getWarnSoundName() + ", dapingRefreshTimes=" + this.getDapingRefreshTimes() + ", showVersion=" + this.getShowVersion() + ", redisUrl=" + this.getRedisUrl() + ", llmData=" + this.getLlmData() + ")";
    }
}

