package com.lczq.dbquery.service;

import com.lczq.dbquery.entities.QueryResult;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface DBQueryService
{

    Pair<String, QueryResult> query(String selectId, Map<String, String> params);
}
