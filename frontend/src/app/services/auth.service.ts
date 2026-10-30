import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthResponse, RegistroDados, Usuario } from '../models/usuario';

const CHAVE_SESSAO = 'vv_sessao';

/** Cuida de cadastro, login (plataforma e Google), logout e guarda a sessão no navegador. */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private api = `${environment.apiUrl}/auth`;

  private token: string | null = null;
  /** Usuário logado (null = visitante). */
  usuario = signal<Usuario | null>(null);

  constructor() {
    const salvo = localStorage.getItem(CHAVE_SESSAO);
    if (salvo) {
      const sessao: AuthResponse = JSON.parse(salvo);
      this.token = sessao.token;
      this.usuario.set(sessao.usuario);
    }
  }

  estaLogado(): boolean {
    return this.token !== null;
  }

  getToken(): string | null {
    return this.token;
  }

  registrar(dados: RegistroDados): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.api}/registrar`, dados).pipe(tap((r) => this.salvarSessao(r)));
  }

  login(email: string, senha: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.api}/login`, { email, senha }).pipe(tap((r) => this.salvarSessao(r)));
  }

  loginComGoogle(credential: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.api}/google`, { credential }).pipe(tap((r) => this.salvarSessao(r)));
  }

  logout(): void {
    this.http.post(`${this.api}/logout`, {}).subscribe({ error: () => {} });
    this.limparSessao();
    this.router.navigate(['/login']);
  }

  /** Atualiza os dados do usuário guardados (ex.: depois de editar o perfil). */
  atualizarUsuarioLocal(usuario: Usuario): void {
    this.usuario.set(usuario);
    if (this.token) {
      localStorage.setItem(CHAVE_SESSAO, JSON.stringify({ token: this.token, usuario }));
    }
  }

  /** Chamado pelo interceptor quando o servidor responde 401. */
  sessaoExpirada(): void {
    this.limparSessao();
    this.router.navigate(['/login']);
  }

  private salvarSessao(resposta: AuthResponse): void {
    this.token = resposta.token;
    this.usuario.set(resposta.usuario);
    localStorage.setItem(CHAVE_SESSAO, JSON.stringify(resposta));
  }

  private limparSessao(): void {
    this.token = null;
    this.usuario.set(null);
    localStorage.removeItem(CHAVE_SESSAO);
  }
}
