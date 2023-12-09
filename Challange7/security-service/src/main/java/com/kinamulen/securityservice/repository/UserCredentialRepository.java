package com.kinamulen.securityservice.repository;

import com.kinamulen.securityservice.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findByUsername(String username);
    Optional<UserCredential> findByBinarfoodId(UUID binarfoodId);

}
