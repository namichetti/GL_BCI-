package com.bci.client.repository;

import com.bci.client.model.UserBCI;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserBCIRepository extends JpaRepository<UserBCI, UUID> {

    Optional<UserBCI> findByUsername(String email);
}
