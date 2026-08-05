/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.RedisMonitor;
import com.wgcloud.mapper.RedisMonitorMapper;
import com.wgcloud.service.LogInfoService;
import com.wgcloud.util.HostUtil;
import com.wgcloud.util.UUIDUtil;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisMonitorService {
    @Autowired
    private RedisMonitorMapper redisMonitorMapper;
    @Autowired
    private LogInfoService logInfoService;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<RedisMonitor> list = this.redisMonitorMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(RedisMonitor redisMonitor) throws Exception {
        redisMonitor.setId(UUIDUtil.getUUID());
        redisMonitor.setCreateTime(new Date());
        this.redisMonitorMapper.save(redisMonitor);
    }

    public int deleteByRedisName(String redisName) throws Exception {
        return this.redisMonitorMapper.deleteByRedisName(redisName);
    }

    public int downByRedisName(String redisName) throws Exception {
        return this.redisMonitorMapper.downByRedisName(redisName);
    }

    public int deleteById(String[] id) throws Exception {
        return this.redisMonitorMapper.deleteById(id);
    }

    public RedisMonitor selectById(String id) throws Exception {
        return this.redisMonitorMapper.selectById(id);
    }

    public List<RedisMonitor> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.redisMonitorMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.redisMonitorMapper.deleteByDate(map);
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.redisMonitorMapper.countByParams(params);
    }

    public void saveLog(HttpServletRequest request, String action, RedisMonitor redisMonitor) {
        if (null == redisMonitor) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u4e2d\u95f4\u4ef6Redis\u76d1\u6d4b\u4fe1\u606f\uff1a" + redisMonitor.getRedisName(), redisMonitor.getRedisNodeInfo(), "2");
    }
}

