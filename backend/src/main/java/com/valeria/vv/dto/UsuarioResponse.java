package com.valeria.vv.dto;

import com.valeria.vv.model.Usuario;

public record UsuarioResponse(Long id, String nome, String email, String telefone, String cidade, String fotoUrl) {

    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getTelefone(), u.getCidade(), u.getFotoUrl());
    }
}
