package com.entradas_cine.ffe.cine.rest.auth;

import com.entradas_cine.ffe.cine.config.ApiRoutes;
import com.entradas_cine.ffe.cine.rest.auth.dto.JwtAuthResponse;
import com.entradas_cine.ffe.cine.rest.auth.dto.SignInRequest;
import com.entradas_cine.ffe.cine.rest.auth.dto.SignUpRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.AUTH)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Registro e inicio de sesión (JWT)")
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponse> signup(
            @RequestBody @Valid SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signin(
            @RequestBody @Valid SignInRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }
}
