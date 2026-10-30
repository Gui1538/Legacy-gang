package com.valeria.vv.controller;

import com.valeria.vv.dto.UsuarioResponse;
import com.valeria.vv.dto.UsuarioUpdateRequest;
import com.valeria.vv.model.Usuario;
import com.valeria.vv.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** CAMADA DE APLICAÇÃO - perfil do usuário logado. */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public UsuarioResponse meuPerfil(@AuthenticationPrincipal Usuario logado) {
        return UsuarioResponse.de(usuarioService.buscarPorId(logado.getId()));
    }

    @PutMapping("/me")
    public UsuarioResponse atualizarPerfil(@AuthenticationPrincipal Usuario logado,
                                           @Valid @RequestBody UsuarioUpdateRequest req) {
        Usuario atualizado = usuarioService.atualizarPerfil(
                logado.getId(), req.nome(), req.telefone(), req.cidade(), req.fotoUrl());
        return UsuarioResponse.de(atualizado);
    }
}
