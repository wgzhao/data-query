package com.github.wgzhao.dbquery.param;

import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;

public class RestResponse<T>
        implements Serializable
{
    private static final long serialVersionUID = 691617930352197222L;

    private static final Integer[] successStatus = new Integer[] {200};

    private Integer status;

    private String message;

    private Long total;

    private T data;

    public RestResponse() {}

    public RestResponse(Integer status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public RestResponse(Integer status, String message, Long total, T data)
    {
        this.status = status;
        this.message = message;
        this.total = total;
        this.data = data;
    }

    public Integer getStatus()
    {
        return this.status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Boolean isSuccess()
    {
        return (ArrayUtils.indexOf(successStatus, this.status) >= 0);
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public T getData()
    {
        return this.data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public Long getTotal()
    {
        return this.total;
    }

    public void setTotal(Long total)
    {
        this.total = total;
    }
}
