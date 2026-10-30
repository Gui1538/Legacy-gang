package com.valeria.vv.repository;

import com.valeria.vv.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** CAMADA DE INFRAESTRUTURA - acesso ao banco para Usuario. */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
