package com.github.wgzhao.dbquery.dto

data class QueryResult (
    val code: Int = 0,
    var message: String? = null,
    var total: Int = 0,
    val data: List<Map<String, Any>>? = null
) {
    companion object {
        // 静态方法构造统一返回结果
        fun  success(data: List<Map<String, Any>>?): QueryResult {
            return QueryResult(0, "success", data.orEmpty().size, data)
        }

        fun error(code: Int, message: String?): QueryResult {
            return QueryResult(code, message, 0, null)
        }
    }
}