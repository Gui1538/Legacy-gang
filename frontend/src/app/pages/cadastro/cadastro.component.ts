import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { GoogleButtonComponent } from '../../components/google-button/google-button.component';
import { AuthService } from '../../services/auth.service';
import { mensagemDeErro } from '../../utils/erro';

/** Validador do formulário: senha e confirmação precisam ser iguais. */
function senhasIguais(grupo: AbstractControl): ValidationErrors | null {
  return grupo.get('senha')?.value === grupo.get('confirmarSenha')?.value ? null : { senhasDiferentes: true };
}

@Component({
  selector: 'app-cadastro',
  imports: [ReactiveFormsModule, RouterLink, GoogleButtonComponent],
  templateUrl: './cadastro.component.html',
})
export class CadastroComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);

  form = this.fb.nonNullable.group(
    {
      nome: ['', [Validators.required, Validators.maxLength(120)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required],
    },
    { validators: senhasIguais },
  );
  erro = signal('');
  carregando = signal(false);

  cadastrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.erro.set('');
    this.carregando.set(true);
    this.auth.registrar(this.form.getRawValue()).subscribe({
      next: () => this.router.navigate(['/home']),
      error: (e) => {
        this.erro.set(mensagemDeErro(e));
        this.carregando.set(false);
      },
    });
  }
}
