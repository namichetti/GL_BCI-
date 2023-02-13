package com.bci.client.service;

import com.bci.client.dto.Request;
import com.bci.client.dto.Response;
import com.bci.client.exception.BusinessException;
import com.bci.client.exception.DatabaseException;
import com.bci.client.model.UserBCI;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserBCIService {

    List<UserBCI> findAll() throws DatabaseException;
    void save(UserBCI userBCI) throws DatabaseException;
    void validateIfUserCBIExist(String email) throws BusinessException;
    UserBCI findUserBCI(UserBCI userBCI1, UserDetails userDetails) throws BusinessException;
    List<Response> entityToListDtoResponse(List<UserBCI> userBCIS);
    UserBCI dtoToEntityRequest(Request request);
    Response entityToDtoResponse(UserBCI userBCI1, UserDetails userDetails,String jwt);

}
