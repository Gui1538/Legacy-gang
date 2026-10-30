export interface Produto {
  id: number;
  nome: string;
  descricao?: string | null;
  preco: number;
  imagemUrl?: string | null;
  emailDono: string;
  nomeDono: string;
  criadoEm: string;
}

/** Dados enviados ao cadastrar ou editar um produto. */
export interface ProdutoDados {
  nome: string;
  descricao: string;
  preco: number;
  imagemUrl: string;
}
