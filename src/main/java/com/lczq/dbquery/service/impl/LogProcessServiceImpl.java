package com.lczq.dbquery.service.impl;

import com.lczq.dbquery.entities.LogEntity;
import com.lczq.dbquery.mapper.LogProcessMapper;
import com.lczq.dbquery.service.LogProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogProcessServiceImpl
        implements LogProcessService
{

    @Autowired
    private LogProcessMapper logProcessMapper;

    @Override
    public int saveLog(LogEntity logEntity)
    {
        return logProcessMapper.insert(logEntity);
    }
}
