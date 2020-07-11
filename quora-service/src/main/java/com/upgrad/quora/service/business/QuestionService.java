package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class QuestionService {
    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final String accessToken, QuestionEntity questionEntity) throws AuthorizationFailedException {
        UserAuthEntity authEntity = userDao.getUserAuthByToken(accessToken);
        if (authEntity == null){
            final String code = "ATHR-001";
            final String comment = "User has not signed in";
            throw new AuthorizationFailedException(code, comment);
        }
        if (authEntity.getLogoutAt() != null){
            final String code = "ATHR-002";
            final String comment = "User is signed out.Sign in first to post a question";
            throw new AuthorizationFailedException(code, comment);
        }
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setUserEntity(authEntity.getUserEntity());
        return questionDao.createQuestion(questionEntity);
    }

}
