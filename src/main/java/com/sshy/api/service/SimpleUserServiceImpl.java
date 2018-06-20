package com.sshy.api.service;

import com.sshy.api.bean.SimpleUser;
import com.sshy.api.dao.SimpleUserDao;
import com.sshy.api.utils.ConstantInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SimpleUserServiceImpl implements SimpleUserService {

    @Autowired
    private SimpleUserDao simpleUserDao;

    @Override
    public boolean insertUser(SimpleUser user) {
        Integer state = simpleUserDao.insertUser(user);
        if (ConstantInterface.NO_DATA_INSERT_NUM == state) {
            return false;
        }else{
            return true;
        }
    }
}
