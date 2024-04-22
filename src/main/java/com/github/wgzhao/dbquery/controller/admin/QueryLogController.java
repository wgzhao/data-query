package com.github.wgzhao.dbquery.controller.admin;

import com.github.wgzhao.dbquery.entities.QueryLog;
import com.github.wgzhao.dbquery.repo.QueryLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("${app.api.manage-prefix}/queryLog")
public class QueryLogController
{
    @Autowired
    private QueryLogRepo queryLogRepo;

    @GetMapping
    public Page<QueryLog> list(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortKey", required = false) String sortKey,
            @RequestParam(value = "sortOrder", required = false) String sortOrder)
    {
        Pageable pageable = createPageable(page, size, sortKey, sortOrder);
        return queryLogRepo.findAll(pageable);
    }

    @GetMapping("/search")
    public Page<QueryLog> search(
            @RequestParam(value = "q", required = true) String q,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortKey", required = false) String sortKey,
            @RequestParam(value = "sortOrder", required = false) String sortOrder)
    {
        Pageable pageable = createPageable(page, size, sortKey, sortOrder);
        return queryLogRepo.findByQuerySqlContaining(q, pageable);
    }

    @GetMapping("/by/selectId/{selectId}")
    public Page<QueryLog> listBySelectId(
            @PathVariable("selectId") String selectId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortKey", required = false) String sortKey,
            @RequestParam(value = "sortOrder", required = false) String sortOrder)
    {
        Pageable pageable = createPageable(page, size, sortKey, sortOrder);
        return queryLogRepo.findAllBySelectId(selectId, pageable);
    }

    @GetMapping("/by/appId/{appId}")
    public Page<QueryLog> listByAppId(
            @PathVariable("appId") String appId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "sortKey", required = false) String sortKey,
            @RequestParam(value = "sortOrder", required = false) String sortOrder)
    {
        Pageable pageable = createPageable(page, size, sortKey, sortOrder);
        return queryLogRepo.findAllByAppId(appId, pageable);
    }

    private Pageable createPageable(int page, int size, String sortKey, String sortOrder)
    {
        List<Sort.Order> orders = new ArrayList<>();
        Sort sortBy;


        if (size < 0 ) {
            size = Integer.MAX_VALUE;
        }

        if (sortKey.isEmpty() || sortOrder.isEmpty()) {
            return Pageable.ofSize(size).withPage(page);
        }

        String[] k = sortKey.split(",");
        String[] o = sortOrder.split(",");
        if (k.length != o.length) {
            return Pageable.ofSize(size).withPage(page);
        }
        
        if (k.length > 0) {
            for (int i = 0; i < k.length; i++) {
                if (o[i].equalsIgnoreCase("asc")) {
                    orders.add(Sort.Order.asc(k[i]));
                } else {
                    orders.add(Sort.Order.desc(k[i]));
                }
            }
        }
        sortBy = Sort.by(orders);
        if (sortBy.isUnsorted()) {
            return Pageable.ofSize(size).withPage(page);
        } else {
            return PageRequest.of(page, size, sortBy);
        }
    }
}
