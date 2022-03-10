package com.lczq.dbquery.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryResult implements Serializable
{
    private static final long serialVersionUID = 5237730257103305078L;

    private List<Map<String, Object>> result = new ArrayList<>();

    public List<Map<String, Object>> getResult()
    {
        return result;
    }

    public void setResult(List<Map<String, Object>> result)
    {
        this.result = result;
    }

    public void setOneMap(Map<String, Object> map)
    {
        this.result.add(map);
    }
}
