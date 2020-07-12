package com.upgrad.quora.service.dao;

import org.springframework.stereotype.Repository;
import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
}
