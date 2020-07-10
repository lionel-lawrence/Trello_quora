package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.*;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserById(final String id){
    try{
        return entityManager.createNamedQuery("userByUserId", UserEntity.class).setParameter("userId", id).getSingleResult();
        }
    catch(NoResultException exception){
        return null;
        }
    }

    public UserAuthEntity createToken(final UserAuthEntity authEntity){
        entityManager.persist(authEntity);
        return authEntity;
    }

    public UserAuthEntity getUserAuthByToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUser_UserName(final String userName){
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("userName", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUser_EmailId(final String email){
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void updateUserEntity(final UserEntity updateUserEntity){
        entityManager.merge(updateUserEntity);
    }

    @Transactional
    public UserEntity deleteUser(final String uuid){
        UserEntity user = getUserById(uuid);
        if (user != null){
            this.entityManager.remove(user);
        }
        return user;
    }
}
