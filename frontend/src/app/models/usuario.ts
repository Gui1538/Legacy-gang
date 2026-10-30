export interface Usuario {
  id: number;
  nome: string;
  email: string;
  telefone?: string | null;
  cidade?: string | null;
  fotoUrl?: string | null;
}

export interface AuthResponse {
  token: string;
  usuario: Usuario;
}

export interface RegistroDados {
  nome: string;
  email: string;
  senha: string;
  confirmarSenha: string;
}
