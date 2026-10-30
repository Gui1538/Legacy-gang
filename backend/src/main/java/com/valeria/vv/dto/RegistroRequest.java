package com.valeria.vv.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Cadastro na plataforma: apenas nome completo, e-mail, senha e confirmação. */
public record RegistroRequest(
        @NotBlank(message = "Informe o nome completo")
        @Size(max = 120, message = "O nome pode ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "Informe o e-mail")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Informe a senha")
        @Size(min = 6, max = 72, message = "A senha deve ter entre 6 e 72 caracteres")
        String senha,

        @NotBlank(message = "Confirme a senha")
        String confirmarSenha) {
}
