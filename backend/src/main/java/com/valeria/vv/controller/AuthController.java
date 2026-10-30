package com.valeria.vv.controller;

import com.valeria.vv.dto.AuthResponse;
import com.valeria.vv.dto.GoogleLoginRequest;
import com.valeria.vv.dto.LoginRequest;
import com.valeria.vv.dto.RegistroRequest;
import com.valeria.vv.dto.UsuarioResponse;
import com.valeria.vv.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CAMADA DE APLICAÇÃO - cadastro e login. Só recebe a requisição, chama o service e devolve a resposta.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest req) {
        var resultado = authService.registrar(req.nome(), req.email(), req.senha(), req.confirmarSenha());
        return ResponseEntity.status(HttpStatus.CREATED).body(paraResposta(resultado));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return paraResposta(authService.login(req.email(), req.senha()));
    }

    @PostMapping("/google")
    public AuthResponse google(@Valid @RequestBody GoogleLoginRequest req) {
        return paraResposta(authService.loginComGoogle(req.credential()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "Authorization", required = false) String cabecalho) {
        if (cabecalho != null && cabecalho.startsWith("Bearer ")) {
            authService.logout(cabecalho.substring(7));
        }
        return ResponseEntity.noContent().build();
    }

    private AuthResponse paraResposta(AuthService.Resultado resultado) {
        return new AuthResponse(resultado.token(), UsuarioResponse.de(resultado.usuario()));
    }
}
