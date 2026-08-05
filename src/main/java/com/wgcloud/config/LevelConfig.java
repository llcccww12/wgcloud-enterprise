/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="level")
public class LevelConfig {
    private String speedWarn;
    private String memWarn;
    private String sysLoadWarn;
    private String cpuWarn;
    private String netConnectionsWarn;
    private String cpuTemperatureWarn;
    private String diskWarn;
    private String smartWarn;
    private String hostDownWarn;
    private String hostLoginWarn;
    private String shellWarn;
    private String defaultWarn;
    private String addToWarnContent;

    public String getSpeedWarn() {
        if (StringUtils.isEmpty((CharSequence)this.speedWarn)) {
            return "WARN";
        }
        return this.speedWarn;
    }

    public void setSpeedWarn(String speedWarn) {
        this.speedWarn = speedWarn;
    }

    public String getMemWarn() {
        if (StringUtils.isEmpty((CharSequence)this.memWarn)) {
            return "WARN";
        }
        return this.memWarn;
    }

    public void setMemWarn(String memWarn) {
        this.memWarn = memWarn;
    }

    public String getSysLoadWarn() {
        if (StringUtils.isEmpty((CharSequence)this.sysLoadWarn)) {
            return "WARN";
        }
        return this.sysLoadWarn;
    }

    public void setSysLoadWarn(String sysLoadWarn) {
        this.sysLoadWarn = sysLoadWarn;
    }

    public String getCpuWarn() {
        if (StringUtils.isEmpty((CharSequence)this.cpuWarn)) {
            return "WARN";
        }
        return this.cpuWarn;
    }

    public void setCpuWarn(String cpuWarn) {
        this.cpuWarn = cpuWarn;
    }

    public String getCpuTemperatureWarn() {
        if (StringUtils.isEmpty((CharSequence)this.cpuTemperatureWarn)) {
            return "WARN";
        }
        return this.cpuTemperatureWarn;
    }

    public void setCpuTemperatureWarn(String cpuTemperatureWarn) {
        this.cpuTemperatureWarn = cpuTemperatureWarn;
    }

    public String getDiskWarn() {
        if (StringUtils.isEmpty((CharSequence)this.diskWarn)) {
            return "WARN";
        }
        return this.diskWarn;
    }

    public void setDiskWarn(String diskWarn) {
        this.diskWarn = diskWarn;
    }

    public String getSmartWarn() {
        if (StringUtils.isEmpty((CharSequence)this.smartWarn)) {
            return "WARN";
        }
        return this.smartWarn;
    }

    public void setSmartWarn(String smartWarn) {
        this.smartWarn = smartWarn;
    }

    public String getHostDownWarn() {
        if (StringUtils.isEmpty((CharSequence)this.hostDownWarn)) {
            return "WARN";
        }
        return this.hostDownWarn;
    }

    public void setHostDownWarn(String hostDownWarn) {
        this.hostDownWarn = hostDownWarn;
    }

    public String getHostLoginWarn() {
        if (StringUtils.isEmpty((CharSequence)this.hostLoginWarn)) {
            return "WARN";
        }
        return this.hostLoginWarn;
    }

    public void setHostLoginWarn(String hostLoginWarn) {
        this.hostLoginWarn = hostLoginWarn;
    }

    public String getDefaultWarn() {
        if (StringUtils.isEmpty((CharSequence)this.defaultWarn)) {
            return "INFO";
        }
        return this.defaultWarn;
    }

    public void setDefaultWarn(String defaultWarn) {
        this.defaultWarn = defaultWarn;
    }

    public String getShellWarn() {
        if (StringUtils.isEmpty((CharSequence)this.shellWarn)) {
            return "INFO";
        }
        return this.shellWarn;
    }

    public void setShellWarn(String shellWarn) {
        this.shellWarn = shellWarn;
    }

    public String getAddToWarnContent() {
        if (StringUtils.isEmpty((CharSequence)this.addToWarnContent)) {
            return "true";
        }
        return this.addToWarnContent;
    }

    public void setAddToWarnContent(String addToWarnContent) {
        this.addToWarnContent = addToWarnContent;
    }

    public String getNetConnectionsWarn() {
        if (StringUtils.isEmpty((CharSequence)this.netConnectionsWarn)) {
            return "WARN";
        }
        return this.netConnectionsWarn;
    }

    public void setNetConnectionsWarn(String netConnectionsWarn) {
        this.netConnectionsWarn = netConnectionsWarn;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LevelConfig)) {
            return false;
        }
        LevelConfig other = (LevelConfig)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$speedWarn = this.getSpeedWarn();
        String other$speedWarn = other.getSpeedWarn();
        if (this$speedWarn == null ? other$speedWarn != null : !this$speedWarn.equals(other$speedWarn)) {
            return false;
        }
        String this$memWarn = this.getMemWarn();
        String other$memWarn = other.getMemWarn();
        if (this$memWarn == null ? other$memWarn != null : !this$memWarn.equals(other$memWarn)) {
            return false;
        }
        String this$sysLoadWarn = this.getSysLoadWarn();
        String other$sysLoadWarn = other.getSysLoadWarn();
        if (this$sysLoadWarn == null ? other$sysLoadWarn != null : !this$sysLoadWarn.equals(other$sysLoadWarn)) {
            return false;
        }
        String this$cpuWarn = this.getCpuWarn();
        String other$cpuWarn = other.getCpuWarn();
        if (this$cpuWarn == null ? other$cpuWarn != null : !this$cpuWarn.equals(other$cpuWarn)) {
            return false;
        }
        String this$netConnectionsWarn = this.getNetConnectionsWarn();
        String other$netConnectionsWarn = other.getNetConnectionsWarn();
        if (this$netConnectionsWarn == null ? other$netConnectionsWarn != null : !this$netConnectionsWarn.equals(other$netConnectionsWarn)) {
            return false;
        }
        String this$cpuTemperatureWarn = this.getCpuTemperatureWarn();
        String other$cpuTemperatureWarn = other.getCpuTemperatureWarn();
        if (this$cpuTemperatureWarn == null ? other$cpuTemperatureWarn != null : !this$cpuTemperatureWarn.equals(other$cpuTemperatureWarn)) {
            return false;
        }
        String this$diskWarn = this.getDiskWarn();
        String other$diskWarn = other.getDiskWarn();
        if (this$diskWarn == null ? other$diskWarn != null : !this$diskWarn.equals(other$diskWarn)) {
            return false;
        }
        String this$smartWarn = this.getSmartWarn();
        String other$smartWarn = other.getSmartWarn();
        if (this$smartWarn == null ? other$smartWarn != null : !this$smartWarn.equals(other$smartWarn)) {
            return false;
        }
        String this$hostDownWarn = this.getHostDownWarn();
        String other$hostDownWarn = other.getHostDownWarn();
        if (this$hostDownWarn == null ? other$hostDownWarn != null : !this$hostDownWarn.equals(other$hostDownWarn)) {
            return false;
        }
        String this$hostLoginWarn = this.getHostLoginWarn();
        String other$hostLoginWarn = other.getHostLoginWarn();
        if (this$hostLoginWarn == null ? other$hostLoginWarn != null : !this$hostLoginWarn.equals(other$hostLoginWarn)) {
            return false;
        }
        String this$shellWarn = this.getShellWarn();
        String other$shellWarn = other.getShellWarn();
        if (this$shellWarn == null ? other$shellWarn != null : !this$shellWarn.equals(other$shellWarn)) {
            return false;
        }
        String this$defaultWarn = this.getDefaultWarn();
        String other$defaultWarn = other.getDefaultWarn();
        if (this$defaultWarn == null ? other$defaultWarn != null : !this$defaultWarn.equals(other$defaultWarn)) {
            return false;
        }
        String this$addToWarnContent = this.getAddToWarnContent();
        String other$addToWarnContent = other.getAddToWarnContent();
        return !(this$addToWarnContent == null ? other$addToWarnContent != null : !this$addToWarnContent.equals(other$addToWarnContent));
    }

    protected boolean canEqual(Object other) {
        return other instanceof LevelConfig;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $speedWarn = this.getSpeedWarn();
        result = result * 59 + ($speedWarn == null ? 43 : $speedWarn.hashCode());
        String $memWarn = this.getMemWarn();
        result = result * 59 + ($memWarn == null ? 43 : $memWarn.hashCode());
        String $sysLoadWarn = this.getSysLoadWarn();
        result = result * 59 + ($sysLoadWarn == null ? 43 : $sysLoadWarn.hashCode());
        String $cpuWarn = this.getCpuWarn();
        result = result * 59 + ($cpuWarn == null ? 43 : $cpuWarn.hashCode());
        String $netConnectionsWarn = this.getNetConnectionsWarn();
        result = result * 59 + ($netConnectionsWarn == null ? 43 : $netConnectionsWarn.hashCode());
        String $cpuTemperatureWarn = this.getCpuTemperatureWarn();
        result = result * 59 + ($cpuTemperatureWarn == null ? 43 : $cpuTemperatureWarn.hashCode());
        String $diskWarn = this.getDiskWarn();
        result = result * 59 + ($diskWarn == null ? 43 : $diskWarn.hashCode());
        String $smartWarn = this.getSmartWarn();
        result = result * 59 + ($smartWarn == null ? 43 : $smartWarn.hashCode());
        String $hostDownWarn = this.getHostDownWarn();
        result = result * 59 + ($hostDownWarn == null ? 43 : $hostDownWarn.hashCode());
        String $hostLoginWarn = this.getHostLoginWarn();
        result = result * 59 + ($hostLoginWarn == null ? 43 : $hostLoginWarn.hashCode());
        String $shellWarn = this.getShellWarn();
        result = result * 59 + ($shellWarn == null ? 43 : $shellWarn.hashCode());
        String $defaultWarn = this.getDefaultWarn();
        result = result * 59 + ($defaultWarn == null ? 43 : $defaultWarn.hashCode());
        String $addToWarnContent = this.getAddToWarnContent();
        result = result * 59 + ($addToWarnContent == null ? 43 : $addToWarnContent.hashCode());
        return result;
    }

    public String toString() {
        return "LevelConfig(speedWarn=" + this.getSpeedWarn() + ", memWarn=" + this.getMemWarn() + ", sysLoadWarn=" + this.getSysLoadWarn() + ", cpuWarn=" + this.getCpuWarn() + ", netConnectionsWarn=" + this.getNetConnectionsWarn() + ", cpuTemperatureWarn=" + this.getCpuTemperatureWarn() + ", diskWarn=" + this.getDiskWarn() + ", smartWarn=" + this.getSmartWarn() + ", hostDownWarn=" + this.getHostDownWarn() + ", hostLoginWarn=" + this.getHostLoginWarn() + ", shellWarn=" + this.getShellWarn() + ", defaultWarn=" + this.getDefaultWarn() + ", addToWarnContent=" + this.getAddToWarnContent() + ")";
    }
}

