package com.github.wgzhao.dbquery.dto

data class QueryResult (
    val code: Int = 0,
    var message: String? = null,
    var total: Int = 0,
    val data: List<Map<String, Any>>? = null
) {
    companion object {
        // 静态方法构造统一返回结果
        fun <T> success(data: List<Map<String, Any>>?): QueryResult {
            return QueryResult(0, "success", data?.size, data)
        }

        fun <T> error(code: Int, message: String?): ApiResponse<T?> {
            return ApiResponse<T?>(code, message, null)
        }
    }
}