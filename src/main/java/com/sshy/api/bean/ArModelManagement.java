package com.sshy.api.bean;

import java.sql.Timestamp;

public class ArModelManagement {

    private Integer id;

    private Integer cUserId;

    private String arModelUrl;

    private String arModelName;

    private Timestamp arModelCreateTime;

    private Integer deleteNum;

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
