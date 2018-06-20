package com.sshy.api.bean;

import java.sql.Timestamp;

public class ArCharManagement {

    private Integer id;

    private String arChartImageId;

    private Integer cUserId;

    private Integer arModelId;

    private Timestamp arChartCreateTime;

    private Integer deleteNum;

    private Integer arThemeId;

    private String outUrl;

    private String arChartImageUrl;

    public String getArChartImageUrl() {
        return arChartImageUrl;
    }

    public void setArChartImageUrl(String arChartImageUrl) {
        this.arChartImageUrl = arChartImageUrl;
    }

    public Integer getArThemeId() {
        return arThemeId;
    }

    public void setArThemeId(Integer arThemeId) {
        this.arThemeId = arThemeId;
    }

    public String getOutUrl() {
        return outUrl;
    }

    public void setOutUrl(String outUrl) {
        this.outUrl = outUrl;
    }

    public Integer getArModelId() {
        return arModelId;
    }

    public void setArModelId(Integer arModelId) {
        this.arModelId = arModelId;
    }

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

    public String getArChartImageId() {
        return arChartImageId;
    }

    public void setArChartImageId(String arChartImageId) {
        this.arChartImageId = arChartImageId;
    }

    public Integer getcUserId() {
        return cUserId;
    }

    public void setcUserId(Integer cUserId) {
        this.cUserId = cUserId;
    }
}
