package com.github.wgzhao.dbquery.service;

import com.github.wgzhao.dbquery.entities.Sign;

public interface SignService
{
    Sign querySign(String appId);

    Sign genSign(String applier);
}
