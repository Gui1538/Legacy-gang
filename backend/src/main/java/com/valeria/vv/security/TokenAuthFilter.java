package com.valeria.vv.security;

import com.valeria.vv.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Lê o cabeçalho "Authorization: Bearer <token>" e, se o token for válido,
 * coloca o usuário como autenticado na requisição.
 */
public class TokenAuthFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public TokenAuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String cabecalho = request.getHeader("Authorization");
        if (cabecalho != null && cabecalho.startsWith("Bearer ")) {
            String token = cabecalho.substring(7);
            authService.autenticar(token).ifPresent(usuario -> {
                var autenticacao = new UsernamePasswordAuthenticationToken(usuario, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            });
        }
        chain.doFilter(request, response);
    }
}
