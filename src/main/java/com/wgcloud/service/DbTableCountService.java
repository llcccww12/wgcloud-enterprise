/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wgcloud.entity.DbTable;
import com.wgcloud.entity.DbTableCount;
import com.wgcloud.mapper.DbTableCountMapper;
import com.wgcloud.util.DateUtil;
import com.wgcloud.util.FormatUtil;
import com.wgcloud.util.UUIDUtil;
import com.wgcloud.util.jdbc.ConnectionUtil;
import com.wgcloud.util.license.LicenseUtil;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DbTableCountService {
    private static final Logger logger = LoggerFactory.getLogger(DbTableCountService.class);
    @Autowired
    private DbTableCountMapper dbTableCountMapper;
    @Autowired
    private ConnectionUtil connectionUtil;

    public PageInfo selectByParams(Map<String, Object> params, int currPage, int pageSize) throws Exception {
        PageHelper.startPage((int)currPage, (int)pageSize);
        List<DbTableCount> list = this.dbTableCountMapper.selectByParams(params);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public void save(DbTableCount DbTableCount2) throws Exception {
        DbTableCount2.setId(UUIDUtil.getUUID());
        DbTableCount2.setCreateTime(DateUtil.getNowTime());
        this.dbTableCountMapper.save(DbTableCount2);
    }

    public void saveRecord(List<DbTableCount> recordList) {
        try {
            if (recordList.size() < 1) {
                return;
            }
            for (DbTableCount as : recordList) {
                as.setId(UUIDUtil.getUUID());
                as.setDateStr(DateUtil.getDateTimeString(as.getCreateTime()));
            }
            this.dbTableCountMapper.insertList(recordList);
        }
        catch (Exception e) {
            logger.error("DbTableCount saveRecord error", (Throwable)e);
        }
    }

    public int countByParams(Map<String, Object> params) throws Exception {
        return this.dbTableCountMapper.countByParams(params);
    }

    @Transactional
    public int deleteById(String[] id) throws Exception {
        return this.dbTableCountMapper.deleteById(id);
    }

    public void updateById(DbTableCount DbTableCount2) throws Exception {
        this.dbTableCountMapper.updateById(DbTableCount2);
    }

    public DbTableCount selectById(String id) throws Exception {
        return this.dbTableCountMapper.selectById(id);
    }

    public List<DbTableCount> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.dbTableCountMapper.selectAllByParams(params);
    }

    public int deleteByDate(Map<String, Object> map) throws Exception {
        return this.dbTableCountMapper.deleteByDate(map);
    }

    public JSONArray getJsonArray(DbTable dbTable, List<DbTableCount> dbTableCountList) {
        JSONArray jsonArray = new JSONArray();
        String names = dbTable.getTableName();
        if (StringUtils.isEmpty((CharSequence)names)) {
            names = "\u7ed3\u679c\u503c";
        }
        String[] customNameArr = this.connectionUtil.getColNameArray(names);
        if (!LicenseUtil.checkEnterpriseVersion() && customNameArr.length > 1) {
            customNameArr = new String[]{customNameArr[0]};
        }
        for (int i = 0; i < customNameArr.length; ++i) {
            for (DbTableCount dbTableCount : dbTableCountList) {
                String countValue = dbTableCount.getTableCount();
                if (StringUtils.isEmpty((CharSequence)countValue)) continue;
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("customName", (Object)customNameArr[i]);
                String[] customValueArr = countValue.split(",");
                jsonObject.set("dateStr", (Object)dbTableCount.getDateStr());
                jsonObject.set("customValueDouble", (Object)FormatUtil.getCustomValue(i, customValueArr));
                jsonArray.add((Object)jsonObject);
            }
        }
        return jsonArray;
    }
}

