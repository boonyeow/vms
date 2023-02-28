package com.vms.service;

import com.vms.auth.AuthRequest;
import com.vms.auth.AuthResponse;
import com.vms.auth.RegisterRequest;
import com.vms.config.JwtService;
import com.vms.model.Account;
import com.vms.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthResponse register(RegisterRequest request){
        Account account = Account.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // saves encrypted value of password
                .accountType(request.getAccountType())
                .build();
        accountRepository.save(account);
        String jwt = jwtService.generateToken(account);
        return AuthResponse.builder().token(jwt).build();
    }

    public AuthResponse authenticate(AuthRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        Account account = accountRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwt = jwtService.generateToken(account);
        return AuthResponse.builder().token(jwt).build();
    }
}
