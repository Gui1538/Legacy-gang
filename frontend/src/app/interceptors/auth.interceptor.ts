import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

/** Envia o token em toda requisição e desloga se o servidor disser que a sessão expirou. */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.getToken();

  const requisicao = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

  return next(requisicao).pipe(
    catchError((erro: HttpErrorResponse) => {
      const ehLogin = req.url.includes('/auth/');
      if (erro.status === 401 && !ehLogin && token) {
        auth.sessaoExpirada();
      }
      return throwError(() => erro);
    }),
  );
};
