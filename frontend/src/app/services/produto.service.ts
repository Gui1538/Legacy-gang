import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Produto, ProdutoDados } from '../models/produto';

/** Comunicação do front com o CRUD de produtos da API. */
@Injectable({ providedIn: 'root' })
export class ProdutoService {
  private http = inject(HttpClient);
  private api = `${environment.apiUrl}/produtos`;

  listar(busca = ''): Observable<Produto[]> {
    return this.http.get<Produto[]>(this.api, { params: this.params(busca) });
  }

  listarMeus(busca = ''): Observable<Produto[]> {
    return this.http.get<Produto[]>(`${this.api}/meus`, { params: this.params(busca) });
  }

  buscarPorId(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${this.api}/${id}`);
  }

  criar(dados: ProdutoDados): Observable<Produto> {
    return this.http.post<Produto>(this.api, dados);
  }

  atualizar(id: number, dados: ProdutoDados): Observable<Produto> {
    return this.http.put<Produto>(`${this.api}/${id}`, dados);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  private params(busca: string): HttpParams {
    return busca.trim() ? new HttpParams().set('busca', busca.trim()) : new HttpParams();
  }
}
