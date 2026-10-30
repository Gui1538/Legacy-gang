import { AfterViewInit, Component, ElementRef, EventEmitter, NgZone, Output, ViewChild, inject } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../services/auth.service';
import { mensagemDeErro } from '../../utils/erro';

/**
 * Botão "Continuar com o Google".
 * O Google devolve um ID token (credential); enviamos ao back-end, que valida com o Google
 * e devolve o nosso token de sessão.
 */
@Component({
  selector: 'app-google-button',
  template: `
    <div #alvo class="google-alvo"></div>
    @if (naoConfigurado) {
      <p class="aviso">Login com Google ainda não configurado: informe o Client ID em <code>environment.ts</code>.</p>
    }
  `,
})
export class GoogleButtonComponent implements AfterViewInit {
  @ViewChild('alvo', { static: true }) alvo!: ElementRef<HTMLDivElement>;
  @Output() erro = new EventEmitter<string>();

  private auth = inject(AuthService);
  private router = inject(Router);
  private zone = inject(NgZone);

  naoConfigurado = environment.googleClientId.startsWith('COLE_');

  ngAfterViewInit(): void {
    if (!this.naoConfigurado) {
      this.iniciar(0);
    }
  }

  /** A biblioteca do Google carrega de forma assíncrona; tentamos até ela estar pronta. */
  private iniciar(tentativa: number): void {
    const google = (window as any).google;
    if (!google?.accounts?.id) {
      if (tentativa < 20) {
        setTimeout(() => this.iniciar(tentativa + 1), 250);
      } else {
        this.erro.emit('Não foi possível carregar o login do Google.');
      }
      return;
    }

    google.accounts.id.initialize({
      client_id: environment.googleClientId,
      callback: (resposta: { credential: string }) => this.zone.run(() => this.entrar(resposta.credential)),
    });
    google.accounts.id.renderButton(this.alvo.nativeElement, {
      theme: 'outline',
      size: 'large',
      shape: 'pill',
      text: 'continue_with',
      width: 320,
      locale: 'pt-BR',
    });
  }

  private entrar(credential: string): void {
    this.auth.loginComGoogle(credential).subscribe({
      next: () => this.router.navigate(['/home']),
      error: (e) => this.erro.emit(mensagemDeErro(e)),
    });
  }
}
