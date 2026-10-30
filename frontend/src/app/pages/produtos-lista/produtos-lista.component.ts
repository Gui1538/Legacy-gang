import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Produto } from '../../models/produto';
import { AuthService } from '../../services/auth.service';
import { ProdutoService } from '../../services/produto.service';
import { mensagemDeErro } from '../../utils/erro';

/**
 * Esta tela atende duas rotas, definidas em app.routes.ts:
 *  - /home           (modo 'todos'): todos os produtos do e-commerce + pesquisa
 *  - /meus-produtos  (modo 'meus'):  só os produtos do usuário, com editar e excluir
 */
@Component({
  selector: 'app-produtos-lista',
  imports: [ReactiveFormsModule, RouterLink, CurrencyPipe],
  templateUrl: './produtos-lista.component.html',
  styleUrl: './produtos-lista.component.scss',
})
export class ProdutosListaComponent implements OnInit {
  private service = inject(ProdutoService);
  private route = inject(ActivatedRoute);
  private auth = inject(AuthService);

  modo: 'todos' | 'meus' = this.route.snapshot.data['modo'] ?? 'todos';

  busca = new FormControl('', { nonNullable: true });
  produtos = signal<Produto[]>([]);
  carregando = signal(true);
  erro = signal('');

  primeiroNome = computed(() => (this.auth.usuario()?.nome ?? '').split(' ')[0]);

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando.set(true);
    this.erro.set('');
    const consulta = this.modo === 'meus' ? this.service.listarMeus(this.busca.value) : this.service.listar(this.busca.value);
    consulta.subscribe({
      next: (lista) => {
        this.produtos.set(lista);
        this.carregando.set(false);
      },
      error: (e) => {
        this.erro.set(mensagemDeErro(e));
        this.carregando.set(false);
      },
    });
  }

  limparBusca(): void {
    this.busca.setValue('');
    this.carregar();
  }

  /** O produto é do usuário logado? (a API também confere isso). */
  ehMeu(produto: Produto): boolean {
    return produto.emailDono === this.auth.usuario()?.email;
  }

  cor(produto: Produto): number {
    return produto.id % 4;
  }

  excluir(produto: Produto): void {
    if (!confirm(`Excluir "${produto.nome}"? Essa ação não pode ser desfeita.`)) {
      return;
    }
    this.service.excluir(produto.id).subscribe({
      next: () => this.carregar(),
      error: (e) => this.erro.set(mensagemDeErro(e)),
    });
  }
}
