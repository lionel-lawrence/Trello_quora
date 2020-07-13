package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
		if (userEntity == null) {
			final String code = "ATH-001";
			final String comment = "This username does not exist";
			throw new AuthenticationFailedException(code, comment);
		}
		final String encryptedPassword = passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
		boolean passwordSuccessfulFlag = encryptedPassword.equals(userEntity.getPassword());
		final String code = "ATH-002";
		final String comment = "Password failed";
		if (passwordSuccessfulFlag == false) {
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
	public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
		if (userNameInUse(userEntity.getUserName())) {
			System.out.println(userNameInUse(userEntity.getUserName()));
			throw new SignUpRestrictedException("SGR-001",
					"Try any other Username, this Username has already been taken");
		}
		if (emailInUse(userEntity.getEmail())) {
			throw new SignUpRestrictedException("SGR-002",
					"This user has already been registered, try with any other emailId");
		}

		userEntity.setUuid(UUID.randomUUID().toString());
		String[] textEncryption = passwordCryptographyProvider.encrypt(userEntity.getPassword());
		userEntity.setSalt(textEncryption[0]);
		userEntity.setPassword(textEncryption[1]);
		return userDao.createUser(userEntity);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UserEntity signout(final String accessToken) throws SignOutRestrictedException {
		UserAuthEntity userAuthEntity = userDao.getUserAuthByToken(accessToken);
		if (userAuthEntity == null) {
			final String code = "SGR-001";
			final String comment = "User is not Signed in";
			throw new SignOutRestrictedException(code, comment);
		}
			final ZonedDateTime logoutAt = ZonedDateTime.now();
			userAuthEntity.setLogoutAt(logoutAt);
			userDao.updateUserAuth(userAuthEntity);
			return userAuthEntity.getUserEntity();
	}

	private boolean userNameInUse(final String userName) {
		return userDao.getUser_UserName(userName) != null;
	}

	private boolean emailInUse(final String email) {
		return userDao.getUser_EmailId(email) != null;
	}

}
