package com.vms.auth;

import com.vms.auth.dto.AuthDto;
import com.vms.auth.dto.JwtDto;
import com.vms.auth.dto.RegisterDto;
import com.vms.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<JwtDto> register(@RequestBody RegisterDto request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtDto> authenticate(@RequestBody AuthDto request){
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
