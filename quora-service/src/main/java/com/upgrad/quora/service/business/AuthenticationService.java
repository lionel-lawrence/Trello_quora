package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signin(final String name, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUser_UserName(name);
        if (userEntity == null){
            final String code = "ATH-001";
            final String comment = "This username does not exist";
            throw new AuthenticationFailedException(code, comment);
        }
        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
        boolean passwordSuccessfulFlag = encryptedPassword.equals(userEntity.getPassword());
        final String code = "ATH-002";
        final String comment = "Password failed";
        if(passwordSuccessfulFlag == false){
            throw new AuthenticationFailedException(code, comment);
        }
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(password);
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        userAuthEntity.setUuid(UUID.randomUUID().toString());
        userAuthEntity.setUserEntity(userEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
        userAuthEntity.setLoginAt(now);
        userAuthEntity.setExpiresAt(expiresAt);

        userDao.createToken(userAuthEntity);
        userDao.updateUserEntity(userEntity);

        return userAuthEntity;

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException{
        if (userDao.getUser_UserName(userEntity.getUserName()) != null) {
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken ");
        }
        if (userDao.getUser_EmailId(userEntity.getEmail()) != null){
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }

        userEntity.setUuid(UUID.randomUUID().toString());
        String[] textEncryption = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(textEncryption[0]);
        userEntity.setPassword(textEncryption[1]);
        return userDao.createUser(userEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signout(final String accessToken) throws AuthenticationFailedException{
        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accessToken);
        if (userAuthEntity == null){
            final String code = "SGR-001";
            final String comment = "User is not Signed in";
            throw new AuthenticationFailedException(code, comment);
        }
        else {
            final ZonedDateTime logoutAt = ZonedDateTime.now();
            userAuthEntity.setLogoutAt(logoutAt);
//            userDao.updateUserEntity(userAuthEntity);
        }
        return userAuthEntity;
    }

}
