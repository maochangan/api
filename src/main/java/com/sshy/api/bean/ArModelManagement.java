package com.sshy.api.bean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

public class ArModelManagement {

    private Integer id;

    private Integer cUserId;

    @NotBlank(message = "不可为空")
    private String arModelUrl;

    @NotBlank(message = "不可为空")
    private String arModelName;

    private Timestamp arModelCreateTime;

    private Integer deleteNum;

    @NotBlank(message = "不可为空")
    private String arModelType;

    private Integer arModelActive;

    private String outUrl;

    private Integer testSetting;

    private String pageSetting;

    public String getPageSetting() {
        return pageSetting;
    }

    public void setPageSetting(String pageSetting) {
        this.pageSetting = pageSetting;
    }

    public String getArModelType() {
        return arModelType;
    }

    public void setArModelType(String arModelType) {
        this.arModelType = arModelType;
    }

    public Integer getArModelActive() {
        return arModelActive;
    }

    public void setArModelActive(Integer arModelActive) {
        this.arModelActive = arModelActive;
    }

    public String getOutUrl() {
        return outUrl;
    }

    public void setOutUrl(String outUrl) {
        this.outUrl = outUrl;
    }

    public Integer getTestSetting() {
        return testSetting;
    }

    public void setTestSetting(Integer testSetting) {
        this.testSetting = testSetting;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getcUserId() {
        return cUserId;
    }

    public void setcUserId(Integer cUserId) {
        this.cUserId = cUserId;
    }

    public String getArModelUrl() {
        return arModelUrl;
    }

    public void setArModelUrl(String arModelUrl) {
        this.arModelUrl = arModelUrl;
    }

    public String getArModelName() {
        return arModelName;
    }

    public void setArModelName(String arModelName) {
        this.arModelName = arModelName;
    }

    public Timestamp getArModelCreateTime() {
        return arModelCreateTime;
    }

    public void setArModelCreateTime(Timestamp arModelCreateTime) {
        this.arModelCreateTime = arModelCreateTime;
    }

    public Integer getDeleteNum() {
        return deleteNum;
    }

    public void setDeleteNum(Integer deleteNum) {
        this.deleteNum = deleteNum;
    }
}
