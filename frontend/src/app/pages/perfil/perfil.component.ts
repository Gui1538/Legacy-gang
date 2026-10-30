import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { UsuarioService } from '../../services/usuario.service';
import { mensagemDeErro } from '../../utils/erro';

/**
 * Perfil do usuário. Quem entrou com o Google já chega com nome e foto preenchidos
 * (vindos do Google) e pode completar ou alterar qualquer campo.
 */
@Component({
  selector: 'app-perfil',
  imports: [ReactiveFormsModule],
  templateUrl: './perfil.component.html',
})
export class PerfilComponent implements OnInit {
  private fb = inject(FormBuilder);
  private service = inject(UsuarioService);
  private auth = inject(AuthService);

  form = this.fb.nonNullable.group({
    nome: ['', [Validators.required, Validators.maxLength(120)]],
    telefone: ['', Validators.maxLength(20)],
    cidade: ['', Validators.maxLength(80)],
    fotoUrl: ['', Validators.maxLength(500)],
  });

  email = '';
  erro = signal('');
  sucesso = signal('');
  carregando = signal(true);
  salvando = signal(false);

  ngOnInit(): void {
    this.service.meuPerfil().subscribe({
      next: (u) => {
        this.email = u.email;
        this.form.patchValue({
          nome: u.nome,
          telefone: u.telefone ?? '',
          cidade: u.cidade ?? '',
          fotoUrl: u.fotoUrl ?? '',
        });
        this.carregando.set(false);
      },
      error: (e) => {
        this.erro.set(mensagemDeErro(e));
        this.carregando.set(false);
      },
    });
  }

  inicial(): string {
    return (this.form.controls.nome.value || '?').charAt(0).toUpperCase();
  }

  salvar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.erro.set('');
    this.sucesso.set('');
    this.salvando.set(true);
    this.service.atualizarPerfil(this.form.getRawValue()).subscribe({
      next: (u) => {
        this.auth.atualizarUsuarioLocal(u);
        this.sucesso.set('Perfil atualizado.');
        this.salvando.set(false);
      },
      error: (e) => {
        this.erro.set(mensagemDeErro(e));
        this.salvando.set(false);
      },
    });
  }
}
