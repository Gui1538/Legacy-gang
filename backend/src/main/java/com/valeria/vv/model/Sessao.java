package com.valeria.vv.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * CAMADA DE DOMÍNIO - Sessão de login.
 * O token é uma string aleatória enviada pelo front no cabeçalho Authorization.
 */
@Entity
@Table(name = "sessoes")
@Getter
@Setter
@NoArgsConstructor
public class Sessao {

    @Id
    @Column(length = 64)
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime expiraEm;
}
