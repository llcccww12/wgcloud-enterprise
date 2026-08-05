/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.service;

import com.wgcloud.entity.MailSet;
import com.wgcloud.mapper.MailSetMapper;
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
public class MailSetService {
    @Autowired
    private MailSetMapper mailSetMapper;
    @Autowired
    private LogInfoService logInfoService;

    public void save(MailSet mailSet) throws Exception {
        mailSet.setId(UUIDUtil.getUUID());
        mailSet.setCreateTime(new Date());
        mailSet.setFromMailName(mailSet.getFromMailName().trim());
        mailSet.setFromPwd(mailSet.getFromPwd().trim());
        mailSet.setToMail(mailSet.getToMail().trim());
        mailSet.setSmtpHost(mailSet.getSmtpHost().trim());
        this.mailSetMapper.save(mailSet);
    }

    public void saveLog(HttpServletRequest request, String action, MailSet mailSet) {
        if (null == mailSet) {
            return;
        }
        this.logInfoService.save(HostUtil.getAccountByRequest(request).getAccount() + action + "\u90ae\u4ef6\u8bbe\u7f6e\u4fe1\u606f", "\u63a5\u53d7\u90ae\u4ef6\uff1a" + mailSet.getToMail(), "2");
    }

    public int deleteById(String[] id) throws Exception {
        return this.mailSetMapper.deleteById(id);
    }

    public List<MailSet> selectAllByParams(Map<String, Object> params) throws Exception {
        return this.mailSetMapper.selectAllByParams(params);
    }

    public int updateById(MailSet MailSet2) throws Exception {
        return this.mailSetMapper.updateById(MailSet2);
    }
}

