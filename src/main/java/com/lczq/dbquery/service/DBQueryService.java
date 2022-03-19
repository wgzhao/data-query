package com.lczq.dbquery.service;

import com.lczq.dbquery.entities.QueryResult;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface DBQueryService
{

    /**
     * 数据查询
     * 依据 selectId，获得查询 SQL 模板，并根据 参数 params 值进行填充后查询，并返回结果
     * @param selectId {@link String} 查询ID
     * @param lowerQueryParams {@link Map} url 上传递的参数，其中 key 已经转为小写
     * @return {@link MyPair} 查询结果
     */
    MyPair<String, QueryResult> query(String selectId, Map<String, String> lowerQueryParams);
}
