package com.nathanguisi.login_auth_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.nathanguisi.login_auth_api.domain.domain.user.User;

public interface UserRepository extends JpaRepository<User, String>{
    Optional <User> findByEmail(String email);
}
