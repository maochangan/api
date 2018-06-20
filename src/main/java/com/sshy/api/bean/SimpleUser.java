package com.sshy.api.bean;

import java.sql.Timestamp;

public class SimpleUser {

    private Integer sId;

    private String sOpenId;

    private String sNickName;

    private Integer sGender;

    private String sProvince;

    private String sCity;

    private String sCountry;

    private String sHeadImgUrl;

    private String sUnionId;

    private Timestamp sCreateTime;

    private String orderCompanyUnionId;

    public String getOrderCompanyUnionId() {
        return orderCompanyUnionId;
    }

    public void setOrderCompanyUnionId(String orderCompanyUnionId) {
        this.orderCompanyUnionId = orderCompanyUnionId;
    }

    public Integer getsId() {
        return sId;
    }

    public void setsId(Integer sId) {
        this.sId = sId;
    }

    public String getsOpenId() {
        return sOpenId;
    }

    public void setsOpenId(String sOpenId) {
        this.sOpenId = sOpenId;
    }

    public String getsNickName() {
        return sNickName;
    }

    public void setsNickName(String sNickName) {
        this.sNickName = sNickName;
    }

    public Integer getsGender() {
        return sGender;
    }

    public void setsGender(Integer sGender) {
        this.sGender = sGender;
    }

    public String getsProvince() {
        return sProvince;
    }

    public void setsProvince(String sProvince) {
        this.sProvince = sProvince;
    }

    public String getsCity() {
        return sCity;
    }

    public void setsCity(String sCity) {
        this.sCity = sCity;
    }

    public String getsCountry() {
        return sCountry;
    }

    public void setsCountry(String sCountry) {
        this.sCountry = sCountry;
    }

    public String getsHeadImgUrl() {
        return sHeadImgUrl;
    }

    public void setsHeadImgUrl(String sHeadImgUrl) {
        this.sHeadImgUrl = sHeadImgUrl;
    }

    public String getsUnionId() {
        return sUnionId;
    }

    public void setsUnionId(String sUnionId) {
        this.sUnionId = sUnionId;
    }

    public Timestamp getsCreateTime() {
        return sCreateTime;
    }

    public void setsCreateTime(Timestamp sCreateTime) {
        this.sCreateTime = sCreateTime;
    }
}
