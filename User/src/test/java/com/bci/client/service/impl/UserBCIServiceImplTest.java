package com.bci.client.service.impl;

import com.bci.client.dto.Request;
import com.bci.client.dto.Response;
import com.bci.client.exception.BusinessException;
import com.bci.client.exception.DatabaseException;
import com.bci.client.model.Phone;
import com.bci.client.model.UserBCI;
import com.bci.client.repository.UserBCIRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserBCIServiceImplTest {

    @Mock
    private UserBCIRepository repository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserBCIServiceImpl service;

    UserBCI user = new UserBCI();

    UserDetails userDetails = null;

    Request request = new Request();

    Response response = new Response();


    @Before
    public void setUp(){
        List<Phone> phones = Arrays.asList(new Phone(UUID.randomUUID(),123L,123,"+549"));
        user.setId(UUID.randomUUID());
        user.setUsername("username");
        user.setEmail("nestor@gmail.com");
        user.setPassword("12345");
        user.setPhones(phones);
        userDetails = User.withUsername("username")
                .password("12345")
                .roles("USER").build();

        request.setEmail("12346@hotmail.com");
        request.setPassword("123");
        request.setPhones(new ArrayList<>());
    }

    @Test
    public void findAll() throws DatabaseException {

        when(repository.findAll()).thenReturn(Arrays.asList(new UserBCI()));
        List<UserBCI> list = service.findAll();
        assertNotNull(list);
    }

    @Test
    public void findAllWithException() throws DatabaseException {

        when(repository.findAll()).thenThrow(DatabaseException.class);
        try{
            service.findAll();
        }catch (DatabaseException e){
            assertEquals("Internal error",e.getMessage());
        }
    }

    @Test
    public void save() throws DatabaseException {
        service.save(user);
        verify(repository, times(1)).save(user);
    }

    @Test
    public void saveWithException() throws DatabaseException {
        when(repository.save(user)).thenThrow(DatabaseException.class);
        try{
            service.save(user);
        }catch (DatabaseException e){
            assertEquals("Internal error",e.getMessage());
        }
    }

    @Test
    public void validateIfUserCBIExistException(){
        when(repository.findByUsername("username")).thenReturn(Optional.of(user));
        try {
            service.validateIfUserCBIExist("username");
        } catch (BusinessException e) {
            assertEquals("User already exist",e.getMessage());
        }
    }

    @Test
    public void findUserBCI()throws BusinessException {
        when(repository.findByUsername("username")).thenReturn(Optional.ofNullable(user));
        when(bCryptPasswordEncoder.matches(user.getPassword(),userDetails.getPassword())).thenReturn(true);

        UserBCI userBCI = service.findUserBCI(user,userDetails);
        assertEquals(userBCI.getUsername(),user.getUsername());


    }

    @Test
    public void findUserBCIBusinessExceptionOne()throws BusinessException {
        when(repository.findByUsername("nestor")).thenThrow(BusinessException.class);
        when(bCryptPasswordEncoder.matches(user.getPassword(),userDetails.getPassword())).thenReturn(true);
        try {
            service.findUserBCI(user,userDetails);
        } catch (BusinessException e) {
            assertEquals("User already exist",e.getMessage());
        }

    }

    @Test
    public void findUserBCIBusinessExceptionTwo()throws BusinessException {
        try {
            service.findUserBCI(user,userDetails);
        } catch (BusinessException e) {
            assertEquals("Bad credentials",e.getMessage());
        }

    }

    @Test
    public void entityToDtoResponse(){
        Response response= this.service.entityToDtoResponse(user,userDetails,"jwt");
        assertNotNull(response);
    }

    @Test
    public void dtoToEntityRequest(){
        UserBCI userBCI= this.service.dtoToEntityRequest(request);
        assertNotNull(userBCI);
    }

    @Test
    public void entityToListDtoResponse(){
        List<Response> list= this.service.entityToListDtoResponse(new ArrayList<>());
        assertNotNull(list);
    }
}