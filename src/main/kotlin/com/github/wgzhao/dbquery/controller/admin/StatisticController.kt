package com.github.wgzhao.dbquery.controller.admin

import com.github.wgzhao.dbquery.repo.QueryLogRepo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@RestController
@RequestMapping("\${app.api.manage-prefix}/statistics")
class StatisticController(private val queryLogRepo: QueryLogRepo) {

    @GetMapping("/by-date")
    fun statistic(): MutableList<MutableMap<String?, Any?>?>? {
        // get the date of  a week ago
        val oneWeekAgo = LocalDate.now().minusWeeks(1)
        val date = Date.from(oneWeekAgo.atStartOfDay(ZoneId.systemDefault()).toInstant())
        println(date)
        return queryLogRepo.statisticByDate(date)
    }
}
