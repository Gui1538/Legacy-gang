package com.valeria.vv.repository;

import com.valeria.vv.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/** CAMADA DE INFRAESTRUTURA - acesso ao banco para Produto. */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    /** Pesquisa por nome ou descrição entre TODOS os produtos (busca vazia traz tudo). */
    @Query("""
            select p from Produto p
            where lower(p.nome) like lower(concat('%', :busca, '%'))
               or lower(p.descricao) like lower(concat('%', :busca, '%'))
            order by p.criadoEm desc
            """)
    List<Produto> buscar(@Param("busca") String busca);

    /** Pesquisa por nome ou descrição apenas entre os produtos de um usuário. */
    @Query("""
            select p from Produto p
            where p.dono.id = :donoId
              and (lower(p.nome) like lower(concat('%', :busca, '%'))
                or lower(p.descricao) like lower(concat('%', :busca, '%')))
            order by p.criadoEm desc
            """)
    List<Produto> buscarDoUsuario(@Param("donoId") Long donoId, @Param("busca") String busca);
}
