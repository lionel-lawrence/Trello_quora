package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonUserService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    CommonUserService commonUserService;
    
    //This endpoint of the common controller gets the details of any user in the Quora Application. This endpoint can be accessed by any user in the application.

    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserProfile(@RequestHeader("authorization") final String accessToken, @PathVariable("userId") final String userId) throws UserNotFoundException, AuthenticationFailedException, AuthorizationFailedException {
        commonUserService.tokenCheckIsValid(accessToken);
        UserEntity userEntity = commonUserService.getUser_Id(userId);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
        userDetailsResponse.setFirstName(userEntity.getFirstName());
        userDetailsResponse.setLastName(userEntity.getLastName());
        userDetailsResponse.setUserName(userEntity.getUserName());
        userDetailsResponse.setEmailAddress(userEntity.getEmail());
        userDetailsResponse.setDob(userEntity.getDob());
        userDetailsResponse.setAboutMe(userEntity.getAboutMe());
        userDetailsResponse.setContactNumber(userEntity.getContactNumber());
        userDetailsResponse.setCountry(userEntity.getCountry());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }

}
