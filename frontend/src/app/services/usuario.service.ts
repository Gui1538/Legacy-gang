import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Usuario } from '../models/usuario';

/** Comunicação do front com o perfil do usuário na API. */
@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private http = inject(HttpClient);
  private api = `${environment.apiUrl}/usuarios`;

  meuPerfil(): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.api}/me`);
  }

  atualizarPerfil(dados: Partial<Usuario>): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.api}/me`, dados);
  }
}
