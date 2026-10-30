import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/** Só deixa entrar quem está logado. */
export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  return auth.estaLogado() ? true : router.createUrlTree(['/login']);
};

/** Telas de visitante (boas-vindas, login, cadastro): quem já está logado vai direto para a home. */
export const visitanteGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  return auth.estaLogado() ? router.createUrlTree(['/home']) : true;
};
