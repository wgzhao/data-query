package com.lczq.dbquery.entities;

public class LogEntity
{

    public final static String PROCESS_RESULT_SUCCESS = "成功";
    public final static String PROCESS_RESULT_FAIL = "失败";

    private Integer logid;
    private String flowKey;
    private String flowSequence;
    private Integer qid;
    private String queryKey;
    private String processResult;
    private String message;

    private LogEntity() {}

    public LogEntity(String flowKey, String flowSequence, Integer qid, String queryKey, String processResult, String message)
    {
        this.flowKey = flowKey;
        this.flowSequence = flowSequence;
        this.qid = qid;
        this.queryKey = queryKey;
        this.processResult = processResult;
        this.message = message;
    }
}
