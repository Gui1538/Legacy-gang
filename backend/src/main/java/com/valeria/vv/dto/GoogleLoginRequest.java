package com.valeria.vv.dto;

import jakarta.validation.constraints.NotBlank;

/** "credential" é o ID token entregue pelo botão do Google no front-end. */
public record GoogleLoginRequest(
        @NotBlank(message = "Credencial do Google ausente")
        String credential) {
}
