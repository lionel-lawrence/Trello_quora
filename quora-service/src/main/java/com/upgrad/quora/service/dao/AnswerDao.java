package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerById(final String answerId) {
        try {
            return entityManager
                    .createNamedQuery("getAnswerById", AnswerEntity.class)
                    .setParameter("uuid", answerId)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Transactional
    public void updateAnswer(AnswerEntity answerEntity) {
        entityManager.merge(answerEntity);
    }
}
