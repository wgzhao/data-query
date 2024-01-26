package com.github.wgzhao.dbquery.service.impl;

import com.github.wgzhao.dbquery.entities.SignEntity;

import com.github.wgzhao.dbquery.repo.SignRepo;
import com.github.wgzhao.dbquery.service.SignService;
import com.github.wgzhao.dbquery.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignServiceImpl
        implements SignService
{

    @Autowired
    private SignRepo signRepo;

    public SignEntity querySign(String appId)
    {
        return signRepo.findById(appId).orElse(null);
    }

    public SignEntity genSign(String applier)
    {
        while (true){
            SignEntity signEntity = SignUtil.generateSign();
            if (! signRepo.existsById(signEntity.getAppId())){
                // no conflict
                // save to db
                signEntity.setApplier(applier);
                return signRepo.save(signEntity);
            }
        }
    }
}
