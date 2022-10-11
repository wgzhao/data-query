package com.lczq.dbquery.service.impl;

import com.lczq.dbquery.entities.SignEntity;
import com.lczq.dbquery.mapper.QueryMapper;
import com.lczq.dbquery.service.SignService;
import com.lczq.dbquery.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignServiceImpl
        implements SignService
{
    @Autowired
    private QueryMapper queryMapper;

    public SignEntity querySign(String appId)
    {
        return queryMapper.querySignByAppId(appId);
    }

    public SignEntity genSign(String applier)
    {
        while (true){
            SignEntity signEntity = SignUtil.generateSign();
            if (queryMapper.querySignByAppId(signEntity.getAppId()) == null){
                // no conflict
                // save to db
                signEntity.setApplier(applier);
                queryMapper.saveSign(signEntity);
                return signEntity;
            }
        }

    }
}
