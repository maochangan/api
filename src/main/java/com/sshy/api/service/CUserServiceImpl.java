package com.sshy.api.service;

import com.sshy.api.bean.ArThemeManagement;
import com.sshy.api.bean.CompanyUser;
import com.sshy.api.dao.CompanyUserDao;
import com.sshy.api.utils.ConstantInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CUserServiceImpl implements CUserService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CompanyUserDao companyUserDao;

    @Override
    public List<ArThemeManagement> getAllARTheme() {
        List<ArThemeManagement> list = companyUserDao.selectAll();
        if(ConstantInterface.NO_DATA_INSERT_NUM == list.size()){
            return null;
        }else{
            return list;
        }
    }

    @Override
    public boolean checkCunionIdOnly(String cUnionId) {
        Map<String, Object> map = companyUserDao.selectByCunId(cUnionId);
        return map != null;
    }

    @Override
    public void insertUnionId(String cUnionId) {
        Integer state = companyUserDao.addUnionId(cUnionId);
        logger.info("insertUnid:" + state);
    }

    @Override
    public Integer getCunionId(String cUnionId) {
        Map<String, Object> map = companyUserDao.selectByCunId(cUnionId);
        if(map != null){
            return (Integer) map.get("id");
        }else{
            return null;
        }
    }

    @Override
    public boolean insertCompanyUser(CompanyUser companyUser) {
        return companyUserDao.addCompanyUser(companyUser) != ConstantInterface.NO_DATA_INSERT_NUM;
    }

    @Override
    public boolean updateArTheme(ArThemeManagement arThemeManagement) {
        Integer state = companyUserDao.updateArThemeSerByParKey(arThemeManagement);
        return state != 0;
    }

    @Override
    public boolean addARTheme(ArThemeManagement arThemeManagement) {
        Integer state = companyUserDao.addARTheme(arThemeManagement);
        return state != 0;
    }

    @Override
    public ArThemeManagement getARThemeById(Integer id) {
        ArThemeManagement arThemeManagement = companyUserDao.getARThemeById(id);
        if (null == arThemeManagement) {
            return null;
        }else{
            return arThemeManagement;
        }
    }
}
