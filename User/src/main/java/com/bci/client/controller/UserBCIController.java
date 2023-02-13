package com.bci.client.controller;

import com.bci.client.exception.BusinessException;
import com.bci.client.dto.Request;
import com.bci.client.dto.Response;
import com.bci.client.exception.DatabaseException;
import com.bci.client.model.UserBCI;
import com.bci.client.service.JwtUtilService;
import com.bci.client.service.UserBCIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserBCIController {

    @Autowired
    private UserBCIService userBCIService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> save(@Valid @RequestBody Request request) throws BusinessException,
            DatabaseException {
        this.userBCIService.validateIfUserCBIExist(request.getUsername());

        UserBCI userBCI = this.userBCIService.dtoToEntityRequest(request);
        this.userBCIService.save(userBCI);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserBCI userBCI) throws BusinessException,
            DatabaseException {
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(
                userBCI.getUsername());

        UserBCI userBCI1 = this.userBCIService.findUserBCI(userBCI, userDetails);
        String jwt = jwtUtilService.generateToken(userDetails);

        Response response = this.userBCIService.entityToDtoResponse(userBCI1,userDetails,jwt);

        this.userBCIService.save(userBCI1);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> findAll() throws DatabaseException {
        List<UserBCI> userBCIS= this.userBCIService.findAll();
        List<Response> listResponse = this.userBCIService.entityToListDtoResponse(userBCIS);

        return ResponseEntity.ok(listResponse);
    }
}
