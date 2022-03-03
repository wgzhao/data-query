package com.lczq.dbquery.param;

import java.io.Serializable;
import java.util.Objects;

public class PageRequest
        implements Serializable
{
    private static final long serialVersionUID = 6612473519063971590L;
    private Integer pageNum = 0;
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
