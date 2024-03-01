package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.entities.QueryLog;
import com.github.wgzhao.dbquery.repo.QueryLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/admin/api/v1/queryLog")
public class QueryLogController
{
    @Autowired
    private QueryLogRepo queryLogRepo;

    @GetMapping
    public Page<QueryLog> list(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) List<Map<String, String>> sort)
    {
        Pageable pageable = createPageable(page, size, sort);
        return queryLogRepo.findAll(pageable);
    }

    @GetMapping("/by/selectId/{selectId}")
    public Page<QueryLog> listBySelectId(
            @PathVariable("selectId") String selectId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) List<Map<String, String>> sort)
    {
        Pageable pageable = createPageable(page, size, sort);
        return queryLogRepo.findAllBySelectId(selectId, pageable);
    }

    @GetMapping("/by/appId/{appId}")
    public Page<QueryLog> listByAppId(
            @PathVariable("appId") String appId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) List<Map<String, String>> sort)
    {
        Pageable pageable = createPageable(page, size, sort);
        return queryLogRepo.findAllByAppId(appId, pageable);
    }

    private Pageable createPageable(int page, int size, List<Map<String, String>> sort)
    {
        List<Sort.Order> orders = new ArrayList<>();
        Sort sortBy;
        if (size < 0 ) {
            size = Integer.MAX_VALUE;
        }
        if (sort != null && ! sort.isEmpty()) {
            for (Map<String, String> s : sort) {
                String key = s.get("key");
                String value = s.get("order").toLowerCase();
                if (value.equals("asc")) {
                    orders.add(Sort.Order.asc(key));
                } else {
                    orders.add(Sort.Order.desc(key));
                }
            }
        }
        sortBy = Sort.by(orders);
        if (sortBy.isUnsorted()) {
            return Pageable.ofSize(size).withPage(page);
        } else {
            return PageRequest.of(size, page, sortBy);
        }
    }
}
