package com.valeria.vv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Campos do perfil que o usuário pode alterar (o e-mail não muda). */
public record UsuarioUpdateRequest(
        @NotBlank(message = "Informe o nome completo")
        @Size(max = 120, message = "O nome pode ter no máximo 120 caracteres")
        String nome,

        @Size(max = 20, message = "O telefone pode ter no máximo 20 caracteres")
        String telefone,

        @Size(max = 80, message = "A cidade pode ter no máximo 80 caracteres")
        String cidade,

        @Size(max = 500, message = "A URL da foto pode ter no máximo 500 caracteres")
        String fotoUrl) {
}
