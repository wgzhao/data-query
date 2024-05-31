package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.dto.QueryStatDto;
import com.github.wgzhao.dbquery.repo.QueryLogRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${app.api.manage-prefix}/statistic")
@RequiredArgsConstructor
public class StatisticController
{
    private final QueryLogRepo queryLogRepo;

    @GetMapping("/byDate")
    public List<Map<String, Object>> statistic()
    {
        // get the date of  a week ago
        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
        Date date = Date.from(oneWeekAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println(date);
        return queryLogRepo.statisticByDate(date);
    }
}
