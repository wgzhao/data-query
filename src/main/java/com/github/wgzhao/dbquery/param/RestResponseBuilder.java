package com.github.wgzhao.dbquery.param;

import com.github.wgzhao.dbquery.entities.QueryResult;

public class RestResponseBuilder
{
    public static <T> RestResponse<T> of(Integer status, String message)
    {
        return new RestResponse<>(status, message);
    }

    public static <T> RestResponse<T> of(Integer status, String message, Long total, T data)
    {
        return new RestResponse<>(status, message, total, data);
    }

    public static <T> RestResponse<T>  succ()
    {
        return of(200, "success");
    }

    public static <T> RestResponse<T>  succ(String message)
    {
        return of(200, message);
    }

    public static <T> RestResponse<T> succ(Long total, T data)
    {
        return of(200, "success", total, data);
    }

    public static <T> RestResponse<T> succ(T data)
    {
        return of(200, "success", 1L, data);
    }

    public static <T> RestResponse<T>  fail(String message)
    {
        return of(500, message);
    }

    public static <T> RestResponse<T>  fail(Integer status, String message)
    {
        return of(status, message, 0L , (T) new QueryResult());
    }


}
