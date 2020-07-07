package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonUserService {
    @Autowired
    UserDao userDao;

    public void tokenCheckIsValid(final String accessToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accessToken);
        if(userAuthEntity==null){
            final String code = "ATHR-001";
            final String comment = "User has not signed in";
            throw new AuthorizationFailedException(code, comment);
        }
        if (userAuthEntity.getLogoutAt() != null){
            final String code = "ATHR-002";
            final String comment = "User is signed out. Sign in first to get user details";
            throw new AuthorizationFailedException(code, comment);
        }
    }

    public UserEntity getUser_Id(final String id) throws UserNotFoundException {
        UserEntity userEntity = userDao.getUserById(id);
        if(userEntity == null){
            final String code = "USR-001";
            final String comment = "User with entered uuid does not exist";
            throw new UserNotFoundException(code, comment);
        }
        return userEntity;
    }

}
