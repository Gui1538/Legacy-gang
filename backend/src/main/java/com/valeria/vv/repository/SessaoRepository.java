package com.valeria.vv.repository;

import com.valeria.vv.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

/** CAMADA DE INFRAESTRUTURA - acesso ao banco para Sessao. */
public interface SessaoRepository extends JpaRepository<Sessao, String> {
}
