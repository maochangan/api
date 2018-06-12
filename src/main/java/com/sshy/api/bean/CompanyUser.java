package com.sshy.api.bean;

import java.sql.Timestamp;

public class CompanyUser {

    private Integer cId;

    private String cName;

    private Integer cUnionidId;

    private String cInfo;

    private String cPassword;

    private Timestamp cCreateData;

    public Timestamp getcCreateData() {
        return cCreateData;
    }

    public void setcCreateData(Timestamp cCreateData) {
        this.cCreateData = cCreateData;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public Integer getcUnionidId() {
        return cUnionidId;
    }

    public void setcUnionidId(Integer cUnionidId) {
        this.cUnionidId = cUnionidId;
    }

    public String getcInfo() {
        return cInfo;
    }

    public void setcInfo(String cInfo) {
        this.cInfo = cInfo;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }
}
