package com.github.wgzhao.dbquery.service.impl;

import com.github.wgzhao.dbquery.entities.Sign;

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

    public Sign querySign(String appId)
    {
        return signRepo.findById(appId).orElse(null);
    }

    public Sign genSign(String applier)
    {
        while (true){
            Sign sign = SignUtil.generateSign();
            if (! signRepo.existsById(sign.getAppId())){
                // no conflict
                // save to db
                sign.setApplier(applier);
                return signRepo.save(sign);
            }
        }
    }
}
