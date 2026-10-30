package com.valeria.vv.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CAMADA DE DOMÍNIO - Entidade Produto.
 * Cada produto tem um ID e pertence a um usuário (o "dono"), cujo e-mail é exposto na API.
 */
@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(length = 500)
    private String imagemUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario dono;

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();
}
