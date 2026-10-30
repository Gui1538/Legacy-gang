package com.valeria.vv.dto;

import com.valeria.vv.model.Produto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank(message = "Informe o nome do produto")
        @Size(max = 120, message = "O nome pode ter no máximo 120 caracteres")
        String nome,

        @Size(max = 1000, message = "A descrição pode ter no máximo 1000 caracteres")
        String descricao,

        @NotNull(message = "Informe o preço")
        @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
        BigDecimal preco,

        @Size(max = 500, message = "A URL da imagem pode ter no máximo 500 caracteres")
        String imagemUrl) {

    /** Converte o DTO em entidade, para que a camada de domínio não conheça DTOs. */
    public Produto paraEntidade() {
        Produto p = new Produto();
        p.setNome(nome);
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setImagemUrl(imagemUrl);
        return p;
    }
}
