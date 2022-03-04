package com.lczq.dbquery.mapper;

import com.lczq.dbquery.entities.DataSource;
import com.lczq.dbquery.entities.QueryConfig;
import com.lczq.dbquery.entities.QueryParams;
import com.lczq.dbquery.entities.SignEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface QueryMapper
{

    QueryConfig queryConfigBySelectId(String selectId);

    List<QueryParams> queryParamsBySelectId(String selectId);

    DataSource queryDataSourceBySourceName(String dataSource);

    SignEntity querySignByAppId(String appId);

    void saveSign(SignEntity signEntity);
}
