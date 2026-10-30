package com.valeria.vv.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * CAMADA DE DOMÍNIO - Entidade Usuario.
 * A senha fica nula quando a conta foi criada apenas pelo login com Google.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /** Hash BCrypt da senha (nunca a senha pura). */
    @Column(length = 100)
    private String senha;

    @Column(length = 20)
    private String telefone;

    @Column(length = 80)
    private String cidade;

    @Column(length = 500)
    private String fotoUrl;

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();
}
