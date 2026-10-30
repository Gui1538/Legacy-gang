package com.valeria.vv.dto;

import com.valeria.vv.model.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** O produto carrega seu ID e o e-mail do usuário que o cadastrou. */
public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        String imagemUrl,
        String emailDono,
        String nomeDono,
        LocalDateTime criadoEm) {

    public static ProdutoResponse de(Produto p) {
        return new ProdutoResponse(p.getId(), p.getNome(), p.getDescricao(), p.getPreco(), p.getImagemUrl(),
                p.getDono().getEmail(), p.getDono().getNome(), p.getCriadoEm());
    }
}
