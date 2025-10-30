package com.github.wgzhao.dbquery.service

import java.sql.SQLException

interface DBQueryService {
    /**
     * 数据查询
     * 依据 selectId，获得查询 SQL 模板，并根据 参数 params 值进行填充后查询，并返回结果
     *
     * @param selectId         [String] 查询ID
     * @param appId            @{link String} AppId
     * @param lowerQueryParams [Map] url 上传递的参数，其中 key 已经转为小写
     * @return [MyPair] 查询结果
     */
    @Throws(ClassNotFoundException::class, SQLException::class, RuntimeException::class)
    fun query(
        selectId: String?,
        appId: String?,
        lowerQueryParams: MutableMap<String?, String?>?
    ): MutableList<MutableMap<String?, Any?>?>?
}
