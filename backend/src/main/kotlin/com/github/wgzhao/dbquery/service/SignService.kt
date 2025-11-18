package com.github.wgzhao.dbquery.service

import com.github.wgzhao.dbquery.entities.Sign
import com.github.wgzhao.dbquery.repo.SignRepo
import com.github.wgzhao.dbquery.util.SignUtil
import org.springframework.stereotype.Service

@Service
class SignService(val signRepo: SignRepo) {

    fun findAll(): List<Sign> {
        return signRepo.findAll()
    }

    fun save(sign: Sign): Sign {
        return signRepo.save(sign)
    }
    fun querySign(appId: String): Sign? {
        return signRepo.findById(appId).orElse(null)
    }

    fun existsById(appId: String): Boolean {
        return signRepo.existsById(appId)
    }

    fun deleteById(appId: String) {
        signRepo.deleteById(appId)
    }

    fun genSign(applier: String?): Sign {
        while (true) {
            val sign = SignUtil.generateSign()
            if (!signRepo.existsById(sign.appId)) {
                // no conflict
                // save to db
                sign.applier = applier
                return signRepo.save<Sign>(sign)
            }
        }
    }
}
