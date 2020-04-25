package com.tongji.KnowledgeReasoning.entity;

/**
 * @program: 2020-XLab-KnowledgeReasoning
 * @description: 动态数据
 * @author: 1754060 Zhe Zhang
 * @create: 2020/04/25
 **/
public class DynamicDataEntity {
    private String type;
    private String name;
    private String status;
    private String startTime;
    private String endTime;
    private String deploy_inInfo;
    private static String containsInfo = "container:front-end";
    private static String provideInfo = "service:front-end";

    public DynamicDataEntity(String type, String name, String status,
                             String startTime, String endTime, String deploy_inInfo) {
        this.type = type;
        this.name = name;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.deploy_inInfo = deploy_inInfo;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDeploy_inInfo() {
        return deploy_inInfo;
    }

    public static String getContainsInfo() {
        return containsInfo;
    }

    public static String getProvideInfo() {
        return provideInfo;
    }

    public static DynamicDataEntity getSample(){
        return new DynamicDataEntity("Pod", "hook-demo1", "running",
                "9999-99-01T10:12:18Z","9999-99-01T10:12:18Z",  "server:192.168.199.32");
    }
}
