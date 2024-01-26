package com.github.wgzhao.dbquery.service;

import com.github.wgzhao.dbquery.entities.QueryResult;

import java.util.Map;

public interface DBQueryService
{

    /**
     * 数据查询
     * 依据 selectId，获得查询 SQL 模板，并根据 参数 params 值进行填充后查询，并返回结果
     * @param selectId {@link String} 查询ID
     * @param appId @{link String} AppId
     * @param lowerQueryParams {@link Map} url 上传递的参数，其中 key 已经转为小写
     * @return {@link MyPair} 查询结果
     */
    MyPair<String, QueryResult> query(String selectId, String appId, Map<String, String> lowerQueryParams);
}
