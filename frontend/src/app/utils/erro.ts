import { HttpErrorResponse } from '@angular/common/http';

/** Transforma um erro HTTP em uma frase simples para mostrar ao usuário. */
export function mensagemDeErro(erro: HttpErrorResponse): string {
  if (erro.status === 0) {
    return 'Não foi possível conectar ao servidor. Verifique se o back-end está rodando.';
  }
  return erro.error?.mensagem ?? 'Algo deu errado. Tente novamente.';
}
