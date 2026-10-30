package com.valeria.vv.exception;

/** Usuário autenticado, mas sem permissão para a operação (vira HTTP 403). */
public class AcessoNegadoException extends RuntimeException {
    public AcessoNegadoException(String mensagem) {
        super(mensagem);
    }
}
