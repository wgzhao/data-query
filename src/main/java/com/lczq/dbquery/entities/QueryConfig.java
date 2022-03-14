package com.lczq.dbquery.entities;

import javax.persistence.Entity;

public class QueryConfig
{
    private String selectId;
    private String querySql;
    private String dataSource;
    private int cacheTime;
    private String note;
    private boolean isEnable;
    private boolean enableCache;

    public String getSelectId()
    {
        return selectId;
    }

    public void setSelectId(String selectId)
    {
        this.selectId = selectId;
    }

    public String getQuerySql()
    {
        return querySql;
    }

    public void setQuerySql(String querySql)
    {
        this.querySql = querySql;
    }

    public String getDataSource()
    {
        return dataSource;
    }

    public void setDataSource(String dataSource)
    {
        this.dataSource = dataSource;
    }

    public int getCacheTime()
    {
        return cacheTime;
    }

    public void setCacheTime(int cacheTime)
    {
        this.cacheTime = cacheTime;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public boolean isEnable()
    {
        return isEnable;
    }

    public void setEnable(boolean enable)
    {
        isEnable = enable;
    }

    public boolean isEnableCache()
    {
        return enableCache;
    }

    public void setEnableCache(boolean enableCache)
    {
        this.enableCache = enableCache;
    }
}
