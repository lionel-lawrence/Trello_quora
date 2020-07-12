package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.List;


@Service
public class AnswerService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(
            final String accessToken, final String questionId, AnswerEntity answerEntity)
            throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accessToken);
        if (userAuthEntity == null) {
            final String code = "ATHR-001";
            final String comment = "User has not signed in";
            throw new AuthorizationFailedException(code, comment);
        } else if (userAuthEntity.getLogoutAt() != null) {
            final String code = "ATHR-002";
            final String comment = "User is signed out.Sign in first to post an answer";
            throw new AuthorizationFailedException(code, comment);
        }
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            final String code = "QUES-001";
            final String comment = "The question entered is invalid";
            throw new InvalidQuestionException(code, comment);
        }
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setQuestionEntity(questionEntity);
        answerEntity.setUserEntity(userAuthEntity.getUserEntity());
        return answerDao.createAnswer(answerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(
            final String accessToken, final String answerId, final String newAnswer)
            throws AnswerNotFoundException, AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accessToken);
        if (userAuthEntity == null) {
            final String code = "ATHR-001";
            final String comment = "User has not signed in";
            throw new AuthorizationFailedException(code,comment);
        } else if (userAuthEntity.getLogoutAt() != null) {
            final String code = "ATHR-002";
            final String comment = "User is signed out.Sign in first to edit an answer";
            throw new AuthorizationFailedException(code, comment);
        }
        AnswerEntity answerEntity = answerDao.getAnswerById(answerId);
        if (answerEntity == null) {
            final String code = "ANS-001";
            final String comment = "Entered answer uuid does not exist";
            throw new AnswerNotFoundException(code, comment);
        }
        if (!answerEntity.getUserEntity().getUuid().equals(userAuthEntity.getUserEntity().getUuid())) {
            final String code = "ATHR-003";
            final String comment = "Only the answer owner can edit the answer";
            throw new AuthorizationFailedException(code, comment);
        }
        answerEntity.setAnswer(newAnswer);
        answerDao.updateAnswer(answerEntity);
        return answerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(final String answerId, final String accessToken)
            throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accessToken);
        if (userAuthEntity == null) {
            final String code = "ATHR-001";
            final String comment = "User has not signed in";
            throw new AuthorizationFailedException(code, comment);
        } else if (userAuthEntity.getLogoutAt() != null) {
            final String code ="ATHR-002";
            final String comment = "User is signed out.Sign in first to delete an answer";
            throw new AuthorizationFailedException(code,comment);
        }

        AnswerEntity answerEntity = answerDao.getAnswerById(answerId);
        if (answerEntity == null) {
            final String code = "ANS-001";
            final String comment = "Entered answer uuid does not exist";
            throw new AnswerNotFoundException(code ,comment );
        }
        if (userAuthEntity.getUserEntity().getRole().equals("admin")
                || answerEntity
                .getUserEntity()
                .getUuid()
                .equals(userAuthEntity.getUserEntity().getUuid())) {
            return answerDao.deleteAnswer(answerId);
        } else {
            throw new AuthorizationFailedException(
                    "ATHR-003", "Only the answer owner or admin can delete the answer");
        }
    }

    @Transactional( propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAllAnswersToQuestion(
            final String questionId, final String accessToken)
            throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accessToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException(
                    "ATHR-002", "User is signed out.Sign in first to get the answers");
        }
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException(
                    "QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }
        return answerDao.getAllAnswersToQuestion(questionId);
    }

}
