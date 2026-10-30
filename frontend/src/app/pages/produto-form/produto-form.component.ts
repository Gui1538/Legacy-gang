import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ProdutoDados } from '../../models/produto';
import { AuthService } from '../../services/auth.service';
import { ProdutoService } from '../../services/produto.service';
import { mensagemDeErro } from '../../utils/erro';

/** Cadastro (/produtos/novo) e edição (/produtos/:id/editar) de produto. */
@Component({
  selector: 'app-produto-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './produto-form.component.html',
})
export class ProdutoFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private service = inject(ProdutoService);
  private auth = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  /** null = cadastrando um produto novo. */
  id: number | null = Number(this.route.snapshot.paramMap.get('id')) || null;

  form = this.fb.group({
    nome: ['', [Validators.required, Validators.maxLength(120)]],
    descricao: ['', Validators.maxLength(1000)],
    preco: [null as number | null, [Validators.required, Validators.min(0.01)]],
    imagemUrl: ['', Validators.maxLength(500)],
  });

  erro = signal('');
  carregando = signal(false);
  bloqueado = signal(false);

  ngOnInit(): void {
    if (this.id === null) {
      return;
    }
    this.carregando.set(true);
    this.service.buscarPorId(this.id).subscribe({
      next: (p) => {
        // Regra de negócio: só edita quem cadastrou o produto (o back-end também garante isso).
        if (p.emailDono !== this.auth.usuario()?.email) {
          this.bloqueado.set(true);
          this.erro.set('Você só pode editar produtos cadastrados por você.');
        } else {
          this.form.patchValue({ nome: p.nome, descricao: p.descricao ?? '', preco: p.preco, imagemUrl: p.imagemUrl ?? '' });
        }
        this.carregando.set(false);
      },
      error: (e) => {
        this.bloqueado.set(true);
        this.erro.set(mensagemDeErro(e));
        this.carregando.set(false);
      },
    });
  }

  salvar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    const dados: ProdutoDados = {
      nome: v.nome ?? '',
      descricao: v.descricao ?? '',
      preco: Number(v.preco),
      imagemUrl: v.imagemUrl ?? '',
    };

    this.erro.set('');
    this.carregando.set(true);
    const requisicao = this.id === null ? this.service.criar(dados) : this.service.atualizar(this.id, dados);
    requisicao.subscribe({
      next: () => this.router.navigate(['/meus-produtos']),
      error: (e) => {
        this.erro.set(mensagemDeErro(e));
        this.carregando.set(false);
      },
    });
  }
}
