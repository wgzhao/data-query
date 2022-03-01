package com.lczq.dbquery.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;

@ApiModel("接口响应信息")
public class RestResponse<T>
        implements Serializable
{
    private static final long serialVersionUID = 691617930352197222L;

    private static final Integer[] succStatus = new Integer[] {200};

    @ApiModelProperty(value = "状态码", dataType = "java.lang.Integer", allowableValues = "200, 500", position = 1)
    private Integer status;

    @ApiModelProperty(value = "响应信息", dataType = "java.lang.String", position = 2)
    private String message;

    @ApiModelProperty(value = "返回数据总条数", dataType = "java.lang.Long", position = 3)
    private Long total;

    @ApiModelProperty(value = "返回数据", position = 4)
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

    public Boolean isSucc()
    {
        return (ArrayUtils.indexOf((Object[]) succStatus, this.status) >= 0);
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
