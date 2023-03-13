package com.vms.auth.service;

import com.vms.auth.dto.AuthDto;
import com.vms.auth.dto.JwtDto;
import com.vms.auth.dto.RegisterDto;
import com.vms.model.Account;
import com.vms.model.Token;
import com.vms.repository.AccountRepository;
import com.vms.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public JwtDto register(RegisterDto request){
        Account account = Account.builder()
                .name(request.getName())
                .email(request.getEmail())
                .company(request.getCompany())
                .password(passwordEncoder.encode(request.getPassword())) // saves encrypted value of password
                .accountType(request.getAccountType())
                .build();
        accountRepository.save(account);

        String jwt = jwtService.generateToken(account);
        saveAccountToken(account, jwt);
        return JwtDto.builder().token(jwt).email(account.getEmail()).accountType(account.getAccountType()).build();
    }

    public JwtDto authenticate(AuthDto request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Account account = accountRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwt = jwtService.generateToken(account);

        // Revoke all existing tokens before saving-- ensures only one valid jwt
        revokeAllAccountTokens(account);
        saveAccountToken(account, jwt);
        return JwtDto.builder().token(jwt).email(account.getEmail()).accountType(account.getAccountType()).build();
    }

    private void saveAccountToken(Account account, String jwt){
        Token token = Token.builder()
                .account(account)
                .token(jwt)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllAccountTokens(Account account) {
        List<Token> validAccountTokens = tokenRepository.findAllValidTokenByUser(account.getId());
        if (validAccountTokens.isEmpty()) {
            return;
        }
        validAccountTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validAccountTokens);
    }
}
