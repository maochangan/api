package com.sshy.api.service;

import com.sshy.api.bean.ArCharManagement;
import com.sshy.api.bean.ArThemeManagement;
import com.sshy.api.bean.CompanyUser;

import java.util.List;

public interface CUserService {

    List<ArThemeManagement> getAllARTheme();

    boolean checkCunionIdOnly(String cUnionId);

    void insertUnionId(String cUnionId);

    Integer getCunionId(String cUnionId);

    boolean insertCompanyUser(CompanyUser companyUser);

    boolean updateArTheme(ArThemeManagement arThemeManagement);

    boolean addARTheme(ArThemeManagement arThemeManagement);

    ArThemeManagement getARThemeById(Integer id);

    boolean addARChart(ArCharManagement arCharManagement);

    List<ArCharManagement> getAllARChart();
}
