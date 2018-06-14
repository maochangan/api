package com.sshy.api.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class ArThemeManagement {

    private Integer id;

    private String arThemeName;

    private String ArThemeDescribe;

    private String arThemeImgUrl;

    private Integer arThemeActive;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp arThemeStartTime;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp arThemeEndTime;

    private String arThemeKeyword;

    private Integer cUserId;

    private Integer deleteNum;

    public Integer getDeleteNum() {
        return deleteNum;
    }

    public void setDeleteNum(Integer deleteNum) {
        this.deleteNum = deleteNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArThemeName() {
        return arThemeName;
    }

    public void setArThemeName(String arThemeName) {
        this.arThemeName = arThemeName;
    }

    public String getArThemeDescribe() {
        return ArThemeDescribe;
    }

    public void setArThemeDescribe(String arThemeDescribe) {
        ArThemeDescribe = arThemeDescribe;
    }

    public String getArThemeImgUrl() {
        return arThemeImgUrl;
    }

    public void setArThemeImgUrl(String arThemeImgUrl) {
        this.arThemeImgUrl = arThemeImgUrl;
    }

    public Integer getArThemeActive() {
        return arThemeActive;
    }

    public void setArThemeActive(Integer arThemeActive) {
        this.arThemeActive = arThemeActive;
    }

    public Timestamp getArThemeStartTime() {
        return arThemeStartTime;
    }

    public void setArThemeStartTime(Timestamp arThemeStartTime) {
        this.arThemeStartTime = arThemeStartTime;
    }

    public Timestamp getArThemeEndTime() {
        return arThemeEndTime;
    }

    public void setArThemeEndTime(Timestamp arThemeEndTime) {
        this.arThemeEndTime = arThemeEndTime;
    }

    public String getArThemeKeyword() {
        return arThemeKeyword;
    }

    public void setArThemeKeyword(String arThemeKeyword) {
        this.arThemeKeyword = arThemeKeyword;
    }

    public Integer getcUserId() {
        return cUserId;
    }

    public void setcUserId(Integer cUserId) {
        this.cUserId = cUserId;
    }
}
