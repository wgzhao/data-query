package com.lczq.dbquery.entities;


public class QueryParams
{
    private String selectId;
    private String paramName;
    private String paramType;

    public String getSelectId()
    {
        return selectId;
    }

    public void setSelectId(String selectId)
    {
        this.selectId = selectId;
    }

    public String getParamName()
    {
        return paramName;
    }

    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }

    public String getParamType()
    {
        return paramType;
    }

    public void setParamType(String paramType)
    {
        this.paramType = paramType;
    }
}
