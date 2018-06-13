package com.sshy.api.bean;

import java.sql.Timestamp;

public class ArCharManagement {

    private Integer id;

    private String arChartImageUrl;

    private String arChartImageId;

    private String arChartModelUrl;

    private Integer cUserId;

    private Timestamp arChartCreateTime;

    private Integer deleteNum;

    public Integer getDeleteNum() {
        return deleteNum;
    }

    public void setDeleteNum(Integer deleteNum) {
        this.deleteNum = deleteNum;
    }

    public Timestamp getArChartCreateTime() {
        return arChartCreateTime;
    }

    public void setArChartCreateTime(Timestamp arChartCreateTime) {
        this.arChartCreateTime = arChartCreateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArChartImageUrl() {
        return arChartImageUrl;
    }

    public void setArChartImageUrl(String arChartImageUrl) {
        this.arChartImageUrl = arChartImageUrl;
    }

    public String getArChartImageId() {
        return arChartImageId;
    }

    public void setArChartImageId(String arChartImageId) {
        this.arChartImageId = arChartImageId;
    }

    public String getArChartModelUrl() {
        return arChartModelUrl;
    }

    public void setArChartModelUrl(String arChartModelUrl) {
        this.arChartModelUrl = arChartModelUrl;
    }

    public Integer getcUserId() {
        return cUserId;
    }

    public void setcUserId(Integer cUserId) {
        this.cUserId = cUserId;
    }
}
