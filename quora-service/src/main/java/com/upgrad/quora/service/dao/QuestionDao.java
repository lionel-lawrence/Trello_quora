package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public QuestionEntity createQuestion(QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions() {
        return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
    }

}
