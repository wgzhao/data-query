package com.github.wgzhao.dbquery.service;

import com.github.wgzhao.dbquery.entities.SignEntity;

public interface SignService
{
    SignEntity querySign(String appId);

    SignEntity genSign(String applier);
}
