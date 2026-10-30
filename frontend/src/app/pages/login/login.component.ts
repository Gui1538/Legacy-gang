import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { GoogleButtonComponent } from '../../components/google-button/google-button.component';
import { AuthService } from '../../services/auth.service';
import { mensagemDeErro } from '../../utils/erro';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink, GoogleButtonComponent],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);

  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    senha: ['', Validators.required],
  });
  erro = signal('');
  carregando = signal(false);

  entrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.erro.set('');
    this.carregando.set(true);
    const { email, senha } = this.form.getRawValue();
    this.auth.login(email, senha).subscribe({
      next: () => this.router.navigate(['/home']),
      error: (e) => {
        this.erro.set(mensagemDeErro(e));
        this.carregando.set(false);
      },
    });
  }
}
