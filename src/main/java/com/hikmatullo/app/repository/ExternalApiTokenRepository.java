package com.hikmatullo.app.repository;

import com.hikmatullo.app.entity.ExternalApiToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExternalApiTokenRepository extends JpaRepository<ExternalApiToken, Long> {

    @Query("select e from ExternalApiToken e where e.username = :username and e.isExpired = false")
    Optional<ExternalApiToken> findAllValidToken(@Param("username") String username);

    @Modifying
    @Query("update ExternalApiToken e set e.isExpired = true where e.username = :username")
    void makeTokenExpired(@Param("username") String username);


    @Query("select e from ExternalApiToken e where e.refreshToken = :refreshToken and e.isExpired = false")
    Optional<ExternalApiToken> findExternalApiTokenByRefreshToken(String refreshToken);
}
