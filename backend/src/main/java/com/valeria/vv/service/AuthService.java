package com.valeria.vv.service;

import com.valeria.vv.exception.NaoAutorizadoException;
import com.valeria.vv.exception.RegraNegocioException;
import com.valeria.vv.integration.GoogleTokenVerifier;
import com.valeria.vv.integration.GoogleTokenVerifier.GoogleUsuario;
import com.valeria.vv.model.Sessao;
import com.valeria.vv.model.Usuario;
import com.valeria.vv.repository.SessaoRepository;
import com.valeria.vv.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

/**
 * CAMADA DE DOMÍNIO - cadastro, login (plataforma e Google) e sessões.
 */
@Service
public class AuthService {

    private static final int HORAS_DE_SESSAO = 24;

    /** Resultado de um login: o token e o usuário logado. */
    public record Resultado(String token, Usuario usuario) {
    }

    private final UsuarioRepository usuarioRepository;
    private final SessaoRepository sessaoRepository;
    private final PasswordEncoder passwordEncoder;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final SecureRandom random = new SecureRandom();

    public AuthService(UsuarioRepository usuarioRepository,
                       SessaoRepository sessaoRepository,
                       PasswordEncoder passwordEncoder,
                       GoogleTokenVerifier googleTokenVerifier) {
        this.usuarioRepository = usuarioRepository;
        this.sessaoRepository = sessaoRepository;
        this.passwordEncoder = passwordEncoder;
        this.googleTokenVerifier = googleTokenVerifier;
    }

    @Transactional
    public Resultado registrar(String nome, String email, String senha, String confirmarSenha) {
        if (!senha.equals(confirmarSenha)) {
            throw new RegraNegocioException("A confirmação da senha não confere");
        }
        String emailNormalizado = normalizar(email);
        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new RegraNegocioException("Já existe uma conta com este e-mail");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome.trim());
        usuario.setEmail(emailNormalizado);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuarioRepository.save(usuario);

        return new Resultado(criarSessao(usuario), usuario);
    }

    @Transactional
    public Resultado login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(normalizar(email))
                .orElseThrow(() -> new NaoAutorizadoException("E-mail ou senha inválidos"));

        if (usuario.getSenha() == null) {
            throw new NaoAutorizadoException("Esta conta usa o login com Google");
        }
        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new NaoAutorizadoException("E-mail ou senha inválidos");
        }
        return new Resultado(criarSessao(usuario), usuario);
    }

    /**
     * Login com Google: se o e-mail ainda não existe, cria a conta já preenchida
     * com nome e foto do Google; se existe, apenas entra (completando a foto, se faltar).
     */
    @Transactional
    public Resultado loginComGoogle(String credential) {
        GoogleUsuario google = googleTokenVerifier.verificar(credential);
        String email = normalizar(google.email());

        Usuario usuario = usuarioRepository.findByEmail(email).orElseGet(() -> {
            Usuario novo = new Usuario();
            novo.setEmail(email);
            novo.setNome(google.nome());
            novo.setFotoUrl(google.foto());
            return usuarioRepository.save(novo);
        });

        if (usuario.getFotoUrl() == null && google.foto() != null) {
            usuario.setFotoUrl(google.foto());
            usuarioRepository.save(usuario);
        }
        return new Resultado(criarSessao(usuario), usuario);
    }

    @Transactional
    public void logout(String token) {
        sessaoRepository.deleteById(token);
    }

    /** Usado pelo filtro de segurança: devolve o usuário dono do token, se válido. */
    @Transactional
    public Optional<Usuario> autenticar(String token) {
        return sessaoRepository.findById(token).flatMap(sessao -> {
            if (sessao.getExpiraEm().isBefore(LocalDateTime.now())) {
                sessaoRepository.delete(sessao);
                return Optional.empty();
            }
            return Optional.of(sessao.getUsuario());
        });
    }

    private String criarSessao(Usuario usuario) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        Sessao sessao = new Sessao();
        sessao.setToken(token);
        sessao.setUsuario(usuario);
        sessao.setExpiraEm(LocalDateTime.now().plusHours(HORAS_DE_SESSAO));
        sessaoRepository.save(sessao);
        return token;
    }

    private String normalizar(String email) {
        return email.trim().toLowerCase();
    }
}
