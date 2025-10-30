package com.github.wgzhao.dbquery.service

import com.github.wgzhao.dbquery.entities.Sign

interface SignService {
    fun querySign(appId: String?): Sign?

    fun genSign(applier: String?): Sign?
}
