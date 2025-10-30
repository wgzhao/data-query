package com.github.wgzhao.dbquery.errors

import java.io.Serial

class ParamException(message: String?) : RuntimeException(message) {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
