package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
	@Autowired
	UserDao userDao;

	//This Admin service class holds the logic for the delete functioning of the Admin controller.
	public UserEntity deleteUser(final String accessToken, final String uuid)
			throws AuthorizationFailedException, UserNotFoundException {

		UserAuthEntity userAuthEntity = this.userDao.getUserAuthByToken(accessToken);

		if (userAuthEntity == null) {
			final String code = "ATHR-001";
			final String comment = "User has not signed in";
			throw new AuthorizationFailedException(code, comment);
		}
		if (userAuthEntity.getLogoutAt() != null) {
			final String code = "ATHR-002";
			final String comment = "User is signed out";
			throw new AuthorizationFailedException(code, comment);
		}
		if (!userAuthEntity.getUserEntity().getRole().equals("admin")) {
			final String code = "ATHR-003";
			final String comment = "Unauthorized Access, Entered user is not an admin";
			throw new AuthorizationFailedException(code, comment);
		}

		UserEntity user = this.userDao.getUserById(uuid);

		if (user == null) {
			final String code = "USR-001";
			final String comment = "User with entered uuid to be deleted does not exist";
			throw new UserNotFoundException(code, comment);
		}

		return this.userDao.deleteUser(uuid);

	}

}
