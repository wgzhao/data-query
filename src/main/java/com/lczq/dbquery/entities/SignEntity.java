package com.lczq.dbquery.entities;

public class SignEntity
{
    private String appId;
    private String appKey;

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppKey()
    {
        return appKey;
    }

    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public String toString()
    {
        return this.appId + this.appKey;
    }
}
