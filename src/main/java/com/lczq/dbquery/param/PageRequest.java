package com.lczq.dbquery.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

@ApiModel("分页请求参数")
public class PageRequest
        implements Serializable
{
    private static final long serialVersionUID = 6612473519063971590L;
    @ApiModelProperty(value = "页码，从0开始，默认值0", example = "0", dataType = "java.lang.Integer")
    private Integer pageNum = 0;
    @ApiModelProperty(value = "每页数据条数，默认值10", example = "10", dataType = "java.lang.Integer")
    private Integer pageSize = 10;

    public boolean equals(Object o)
    {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PageRequest)) {
            return false;
        }
        PageRequest other = (PageRequest) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$pageNum = getPageNum(), other$pageNum = other.getPageNum();
        if (!Objects.equals(this$pageNum, other$pageNum)) {
            return false;
        }
        Object this$pageSize = getPageSize(), other$pageSize = other.getPageSize();
        return Objects.equals(this$pageSize, other$pageSize);
    }

    protected boolean canEqual(Object other)
    {
        return other instanceof PageRequest;
    }

    public int hashCode()
    {
        int PRIME = 59;
        int result = 1;
        Object $pageNum = getPageNum();
        result = result * 59 + (($pageNum == null) ? 0 : $pageNum.hashCode());
        Object $pageSize = getPageSize();
        return result * 59 + (($pageSize == null) ? 0 : $pageSize.hashCode());
    }

    public String toString()
    {
        return "PageRequest(pageNum=" + getPageNum() + ", pageSize=" + getPageSize() + ")";
    }

    public Integer getPageNum()
    {
        return this.pageNum;
    }

    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }

    public Integer getPageSize()
    {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }
}
