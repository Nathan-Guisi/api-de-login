package com.nathanguisi.login_auth_api.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nathanguisi.login_auth_api.repositories.UserRepository;
import com.nathanguisi.login_auth_api.domain.domain.user.User;
import com.nathanguisi.login_auth_api.dto.LoginRequestDTO;
import com.nathanguisi.login_auth_api.dto.RegisterRequestDTO;
import com.nathanguisi.login_auth_api.dto.ResponseDTO;
import com.nathanguisi.login_auth_api.infra.segurity.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AutoController {
    @Autowired
    private  UserRepository repository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  TokenService TokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
    
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(user.getPassword(), body.password())){
            String token = this.TokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        
        Optional<User> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()){
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(body.password()));
        newUser.setEmail(body.email());
        newUser.setName(body.name());
        this.repository.save(newUser);

            String token = this.TokenService.generateToken(newUser);
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));

            }
        return ResponseEntity.badRequest().build();

    }
    
}
