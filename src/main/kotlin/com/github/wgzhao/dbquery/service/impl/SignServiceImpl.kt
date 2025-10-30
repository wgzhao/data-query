package com.github.wgzhao.dbquery.service.impl

import com.github.wgzhao.dbquery.entities.Sign
import com.github.wgzhao.dbquery.repo.SignRepo
import com.github.wgzhao.dbquery.service.SignService
import com.github.wgzhao.dbquery.util.SignUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SignServiceImpl

    : SignService {
    @Autowired
    private val signRepo: SignRepo? = null

    override fun querySign(appId: String): Sign? {
        return signRepo!!.findById(appId).orElse(null)
    }

    override fun genSign(applier: String?): Sign {
        while (true) {
            val sign = SignUtil.generateSign()
            if (!signRepo!!.existsById(sign.getAppId())) {
                // no conflict
                // save to db
                sign.setApplier(applier)
                return signRepo.save<Sign>(sign)
            }
        }
    }
}
