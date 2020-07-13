package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerDeleteResponse;
import com.upgrad.quora.api.model.AnswerDetailsResponse;

import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/")
public class AnswerController {
    @Autowired
    private AnswerService answerService;
    
    //The purpose of this endpoint is to create an answer to a particular question. It has  Any user can access this endpoint.

    @RequestMapping(method = RequestMethod.POST,path = "/question/{qnId}/answer/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("authorization") final String accessToken, @PathVariable("qnId") final String questionId, com.upgrad.quora.api.model.AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswer(answerRequest.getAnswer());
        answerEntity = answerService.createAnswer(accessToken, questionId, answerEntity);
        AnswerResponse answerResponse = new AnswerResponse();
        answerResponse.setId(answerEntity.getUuid());
        answerResponse.setStatus("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }
    
    
    //This endpoint is used to edit an answer. Only the owner of the answer can edit the answer.
    
    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/answer/edit/{answerId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswer(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("answerId") final String answerId,
            AnswerEditRequest answerEditRequest)
            throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEditResponse answerEditResponse = new AnswerEditResponse();
        AnswerEntity answerEntity =
                answerService.editAnswer(accessToken, answerId, answerEditRequest.getContent());
        answerEditResponse.setId(answerEntity.getUuid());
        answerEditResponse.setStatus("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);
    }
    
    //This endpoint is used to delete an answer. Only the owner of the answer or admin can delete an answer.

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/answer/delete/{answerId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("answerId") String answerId)
            throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = answerService.deleteAnswer(answerId, accessToken);
        AnswerDeleteResponse answerDeleteResponse =
                new AnswerDeleteResponse().id(answerEntity.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }
    
    //This endpoint gets all answers to a particular question. Any user can access this endpoint.

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/answer/all/{questionId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(
            @RequestHeader("authorization") final String accessToken,
            @PathVariable("questionId") String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {
        List<AnswerEntity> answers = answerService.getAllAnswersToQuestion(questionId, accessToken);
        List<AnswerDetailsResponse> answerDetailsResponses = new ArrayList<>();
        for (AnswerEntity answerEntity : answers) {
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();
            answerDetailsResponse.setId(answerEntity.getUuid());
            answerDetailsResponse.setQuestionContent(answerEntity.getQuestionEntity().getContent());
            answerDetailsResponse.setAnswerContent(answerEntity.getAnswer());
            answerDetailsResponses.add(answerDetailsResponse);
        }
        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponses, HttpStatus.OK);
    }

}
