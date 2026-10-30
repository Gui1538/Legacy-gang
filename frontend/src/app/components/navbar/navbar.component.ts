import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  template: `
    <header class="navbar">
      <div class="container navbar-conteudo">
        <a routerLink="/home" class="marca"><span class="marca-logo"></span>VV Market</a>

        <nav class="navbar-links">
          <a routerLink="/home" routerLinkActive="ativo">Início</a>
          <a routerLink="/meus-produtos" routerLinkActive="ativo">Meus produtos</a>
          <a routerLink="/perfil" routerLinkActive="ativo">Perfil</a>
        </nav>

        <div class="navbar-usuario">
          <a routerLink="/perfil" class="avatar" [title]="auth.usuario()?.nome ?? ''">
            @if (auth.usuario()?.fotoUrl) {
              <img [src]="auth.usuario()?.fotoUrl" alt="Sua foto" referrerpolicy="no-referrer" />
            } @else {
              {{ inicial() }}
            }
          </a>
          <button type="button" class="btn btn-claro" (click)="auth.logout()">Sair</button>
        </div>
      </div>
    </header>
  `,
})
export class NavbarComponent {
  auth = inject(AuthService);

  inicial(): string {
    return (this.auth.usuario()?.nome ?? '?').charAt(0).toUpperCase();
  }
}
