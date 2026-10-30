import { Routes } from '@angular/router';
import { authGuard, visitanteGuard } from './guards/auth.guard';
import { BoasVindasComponent } from './pages/boas-vindas/boas-vindas.component';
import { CadastroComponent } from './pages/cadastro/cadastro.component';
import { LoginComponent } from './pages/login/login.component';
import { PerfilComponent } from './pages/perfil/perfil.component';
import { ProdutoFormComponent } from './pages/produto-form/produto-form.component';
import { ProdutosListaComponent } from './pages/produtos-lista/produtos-lista.component';

export const routes: Routes = [
  // Telas públicas
  { path: '', component: BoasVindasComponent, canActivate: [visitanteGuard] },
  { path: 'login', component: LoginComponent, canActivate: [visitanteGuard] },
  { path: 'cadastro', component: CadastroComponent, canActivate: [visitanteGuard] },

  // Telas que exigem login
  { path: 'home', component: ProdutosListaComponent, data: { modo: 'todos' }, canActivate: [authGuard] },
  { path: 'meus-produtos', component: ProdutosListaComponent, data: { modo: 'meus' }, canActivate: [authGuard] },
  { path: 'produtos/novo', component: ProdutoFormComponent, canActivate: [authGuard] },
  { path: 'produtos/:id/editar', component: ProdutoFormComponent, canActivate: [authGuard] },
  { path: 'perfil', component: PerfilComponent, canActivate: [authGuard] },

  { path: '**', redirectTo: '' },
];
