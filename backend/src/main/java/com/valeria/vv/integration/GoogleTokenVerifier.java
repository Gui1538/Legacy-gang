package com.valeria.vv.integration;

import com.valeria.vv.exception.NaoAutorizadoException;
import com.valeria.vv.exception.RegraNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

/**
 * CAMADA DE INFRAESTRUTURA - integração com a API de autenticação do Google.
 * Recebe o ID token gerado no front e pergunta ao Google se ele é válido.
 *
 * Obs.: o endpoint "tokeninfo" é ótimo para projetos acadêmicos/MVP. Em produção, o Google
 * recomenda validar a assinatura do token localmente com a biblioteca oficial.
 */
@Component
public class GoogleTokenVerifier {

    private static final String TOKENINFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token={token}";

    private final RestClient restClient = RestClient.create();

    @Value("${app.google.client-id}")
    private String clientId;

    /** Dados do usuário que o Google devolve. */
    public record GoogleUsuario(String email, String nome, String foto) {
    }

    public GoogleUsuario verificar(String idToken) {
        if (clientId == null || clientId.isBlank() || clientId.startsWith("COLE_")) {
            throw new RegraNegocioException("Login com Google não configurado no servidor (app.google.client-id).");
        }

        Map<String, Object> dados;
        try {
            dados = restClient.get()
                    .uri(TOKENINFO_URL, idToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });
        } catch (RestClientException e) {
            throw new NaoAutorizadoException("Token do Google inválido ou expirado");
        }

        if (dados == null || !clientId.equals(dados.get("aud"))) {
            throw new NaoAutorizadoException("Token do Google não pertence a esta aplicação");
        }
        if (!"true".equals(String.valueOf(dados.get("email_verified")))) {
            throw new NaoAutorizadoException("E-mail do Google não verificado");
        }

        String email = String.valueOf(dados.get("email"));
        String nome = dados.get("name") != null ? String.valueOf(dados.get("name")) : email;
        String foto = dados.get("picture") != null ? String.valueOf(dados.get("picture")) : null;
        return new GoogleUsuario(email, nome, foto);
    }
}
