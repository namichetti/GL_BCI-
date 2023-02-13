package com.bci.client.service.impl;

import com.bci.client.dto.PhoneDTO;
import com.bci.client.dto.Request;
import com.bci.client.dto.Response;
import com.bci.client.exception.BusinessException;
import com.bci.client.exception.DatabaseException;
import com.bci.client.model.Phone;
import com.bci.client.model.UserBCI;
import com.bci.client.repository.UserBCIRepository;
import com.bci.client.service.UserBCIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserBCIServiceImpl implements UserBCIService {

    @Autowired
    private UserBCIRepository userBCIRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserBCI> findAll() throws DatabaseException {
        try {
            return this.userBCIRepository.findAll();
        } catch (Exception e) {
            throw new DatabaseException("Internal error");
        }
    }

    @Override
    public void save(UserBCI userBCI) throws DatabaseException {
        try {
            this.userBCIRepository.save(userBCI);
        } catch (Exception e) {
            throw new DatabaseException("Internal error");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void validateIfUserCBIExist(String username) throws BusinessException {

        Optional<UserBCI> userBCI = this.userBCIRepository.findByUsername(username);
        if (userBCI.isPresent()) throw new BusinessException("User already exist");
    }

    @Override
    @Transactional(readOnly = true)
    public UserBCI findUserBCI(UserBCI userBCI1, UserDetails userDetails) throws BusinessException {
        this.validatePassword(userBCI1.getPassword(), userDetails.getPassword());

        Optional<UserBCI> userBCI = this.userBCIRepository.findByUsername(userBCI1.getUsername());
        if (!userBCI.isPresent()) throw new BusinessException("User doesn't exist");
        return userBCI.get();

    }

    private void validatePassword(String passwordOne, String passwordTwo) throws BusinessException {
        if (!bCryptPasswordEncoder.matches(passwordOne, passwordTwo))
            throw new BusinessException("Bad credentials");
    }


    @Override
    public Response entityToDtoResponse(UserBCI userBCI1, UserDetails userDetails,
                                        String jwt) {
        Response response = new Response();
        response.setToken(jwt);
        response.setId(userBCI1.getId());
        response.setCreated(userBCI1.getCreated().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss a")));
        response.setLastLogin(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss a")));
        response.setIsActive(userDetails.isAccountNonExpired());
        response.setUsername(userBCI1.getUsername());
        response.setPassword(userBCI1.getPassword());
        response.setEmail(userBCI1.getEmail());
        List<PhoneDTO> phonesDTO = new ArrayList<>();
        userBCI1.getPhones().forEach(p -> {
            PhoneDTO phoneDTO = new PhoneDTO();
            phoneDTO.setCitycode(p.getCitycode());
            phoneDTO.setContrycode(p.getContrycode());
            phoneDTO.setNumber(p.getNumber());
            phonesDTO.add(phoneDTO);
        });
        response.setPhones(phonesDTO);
        userBCI1.setIsActive(userDetails.isAccountNonExpired());
        userBCI1.setLastLogin(LocalDateTime.now());

        return response;
    }

    @Override
    public UserBCI dtoToEntityRequest(Request request) {
        UserBCI userBCI = new UserBCI();
        List<Phone> phones = new ArrayList<>();
        userBCI.setUsername(request.getUsername());
        userBCI.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        userBCI.setEmail(request.getEmail());
        request.getPhones().forEach(p -> {
            Phone phone = new Phone();
            phone.setCitycode(p.getCitycode());
            phone.setContrycode(p.getContrycode());
            phone.setNumber(p.getNumber());
            phones.add(phone);
        });
        userBCI.setPhones(phones);
        return userBCI;
    }

    @Override
    public List<Response> entityToListDtoResponse(List<UserBCI> userBCIS) {
        List<Response> listResponse = new ArrayList<>();
        userBCIS.forEach(u -> {
            List<PhoneDTO> phoneDTOS = new ArrayList<>();
            Response response = new Response();
            response.setUsername(u.getUsername());
            u.getPhones().forEach(p -> {
                PhoneDTO phoneDTO = new PhoneDTO();
                phoneDTO.setCitycode(p.getCitycode());
                phoneDTO.setNumber(p.getNumber());
                phoneDTO.setContrycode(p.getContrycode());
                phoneDTOS.add(phoneDTO);
            });
            response.setPhones(phoneDTOS);
            response.setEmail(u.getEmail());
            response.setPassword(u.getPassword());
            response.setCreated(u.getCreated().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss a")));
            response.setId(u.getId());
            response.setIsActive(u.getIsActive());
            response.setLastLogin(u.getLastLogin().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss a")));
            listResponse.add(response);
        });

        return listResponse;
    }
}
