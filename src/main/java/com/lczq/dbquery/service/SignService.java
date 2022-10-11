package com.lczq.dbquery.service;

import com.lczq.dbquery.entities.SignEntity;

public interface SignService
{
    SignEntity querySign(String appId);

    SignEntity genSign(String applier);
}
