package com.bci.client.service;

import com.bci.client.exception.BusinessException;
import com.bci.client.model.UserBCI;
import com.bci.client.repository.UserBCIRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class JpaUserDetailsService implements UserDetailsService  {

    @Autowired
    private UserBCIRepository userBCIRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserBCI> userBCI = this.userBCIRepository.findByUsername(username);
        if(!userBCI.isPresent()) throw new BusinessException("Bad credentials");

        if (userBCI.get().getRole() != null) {
            User.UserBuilder userBuilder = User.withUsername(username);
            userBuilder
                    .password(userBCI.get().getPassword())
                    .roles(userBCI.get().getRole());
            return userBuilder.build();
        } else {
            throw new UsernameNotFoundException(username);
        }

    }
}

