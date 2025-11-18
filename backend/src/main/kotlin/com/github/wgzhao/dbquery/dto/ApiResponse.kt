package com.github.wgzhao.dbquery.dto

data class ApiResponse<T>(val code: Int, val message: String?, val result: T?) {
    companion object {
        // 静态方法构造统一返回结果
        fun <T> success(data: T?): ApiResponse<T?> {
            return ApiResponse<T?>(0, "success", data)
        }

        fun <T> error(code: Int, message: String): ApiResponse<T?> {
            return ApiResponse(code, message, null)
        }
    }
}
