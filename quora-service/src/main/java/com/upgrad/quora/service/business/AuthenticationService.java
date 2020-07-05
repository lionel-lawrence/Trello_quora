package com.upgrad.quora.service.business;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.Transactional;

@Service
public class AuthenticationService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional
}
