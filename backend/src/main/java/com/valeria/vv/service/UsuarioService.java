package com.valeria.vv.service;

import com.valeria.vv.exception.RecursoNaoEncontradoException;
import com.valeria.vv.model.Usuario;
import com.valeria.vv.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** CAMADA DE DOMÍNIO - regras do perfil do usuário. */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario atualizarPerfil(Long usuarioId, String nome, String telefone, String cidade, String fotoUrl) {
        Usuario usuario = buscarPorId(usuarioId);
        usuario.setNome(nome.trim());
        usuario.setTelefone(vazioParaNulo(telefone));
        usuario.setCidade(vazioParaNulo(cidade));
        usuario.setFotoUrl(vazioParaNulo(fotoUrl));
        return usuarioRepository.save(usuario);
    }

    private String vazioParaNulo(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }
}
