package com.lczq.dbquery.service;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface DBQueryService
{

    Pair<String, List<Map<String, Object>>> query(String selectId, Map<String, String> params);
}
