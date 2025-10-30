package com.github.wgzhao.dbquery.param

import org.apache.commons.lang3.ArrayUtils
import java.io.Serializable

class RestResponse<T>
    : Serializable {
    var status: Int? = null

    var message: String? = null

    var total: Long? = null

    var data: T? = null

    constructor()

    constructor(status: Int?, message: String?) {
        this.status = status
        this.message = message
    }

    constructor(status: Int?, message: String?, total: Long?, data: T?) {
        this.status = status
        this.message = message
        this.total = total
        this.data = data
    }

    val isSuccess: Boolean
        get() = (ArrayUtils.indexOf(successStatus, this.status) >= 0)

    companion object {
        private const val serialVersionUID = 691617930352197222L

        private val successStatus: Array<Int?> = arrayOf<Int>(200)
    }
}
