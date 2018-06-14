package com.sshy.api.service;

import com.sshy.api.bean.ArCharManagement;
import com.sshy.api.bean.ArModelManagement;
import com.sshy.api.bean.ArThemeManagement;
import com.sshy.api.bean.CompanyUser;

import java.util.List;
import java.util.Map;

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

    boolean updateARTarget(String serverPath, String targetId);

    void removeTarget(String targetId);

    boolean addARModel(ArModelManagement arModelManagement);
}
