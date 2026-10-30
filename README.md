# VV Market — MVP de E-commerce em Arquitetura em Camadas

Projeto acadêmico: **Spring Boot (Java 21) + Angular 19 + MySQL**, com CRUD de produtos,
login com e-mail/senha e login com Google.

## 1. Arquitetura em camadas

Segundo Mark Richards (*Software Architecture Patterns*), a arquitetura em camadas organiza os componentes
em camadas horizontais, cada uma com um papel específico, o que gera **separação de responsabilidades**.
Em geral são quatro camadas: apresentação, negócio, persistência e banco de dados. Martin Fowler descreve o mesmo
princípio em três camadas lógicas (*apresentação, domínio e dados*), com dependências indo de cima para baixo.
Ian Sommerville (citado no relatório técnico) reforça que cada camada só oferece serviços à camada superior e depende
apenas da inferior.

Este projeto segue exatamente as 4 camadas do **diagrama de implantação do relatório**:

| Camada (relatório)      | Onde está                                                                 | Responsabilidade |
|-------------------------|---------------------------------------------------------------------------|------------------|
| 4 - Apresentação        | `frontend/` (Angular): `pages/`, `components/`                            | Telas e interação com o usuário |
| 3 - Aplicação           | `backend/.../controller` e `dto`                                          | Recebe HTTP, valida o formato dos dados, chama o service |
| 2 - Domínio             | `backend/.../service` e `model`                                           | **Regras de negócio** (ex.: só o dono altera o produto) e entidades |
| 1 - Infraestrutura      | `backend/.../repository`, `integration`, `security` + MySQL               | Banco de dados, API do Google, segurança |

Regra das camadas **fechadas**: a requisição sempre desce `controller → service → repository → banco`.
O controller nunca acessa o repository, e o service não conhece DTOs nem HTTP.

```
Angular (pages) → services Angular → HTTP/JSON → Controller → Service → Repository → MySQL
                                                                  └→ GoogleTokenVerifier (API do Google)
```

### Estrutura de pastas

```
E-commerce/
├── backend/src/main/java/com/valeria/vv/
│   ├── controller/   AuthController, ProdutoController, UsuarioController
│   ├── dto/          objetos de entrada/saída da API (com validações)
│   ├── service/      AuthService, ProdutoService, UsuarioService   ← regras de negócio
│   ├── model/        Usuario, Produto, Sessao                      ← entidades JPA
│   ├── repository/   interfaces Spring Data JPA
│   ├── integration/  GoogleTokenVerifier
│   ├── security/     SecurityConfig, TokenAuthFilter
│   └── exception/    exceções de negócio + tratamento global
└── frontend/src/app/
    ├── pages/        boas-vindas, login, cadastro, produtos-lista (home e meus produtos), produto-form, perfil
    ├── components/   navbar, google-button
    ├── services/     auth, produto, usuario (comunicação com a API)
    ├── models/       interfaces TypeScript
    ├── guards/       authGuard, visitanteGuard
    └── interceptors/ authInterceptor (envia o token)
```

## 2. Regra de negócio principal

> Um usuário só pode **editar ou excluir** um produto se ele constar na sua página **Meus produtos**
> (ou seja, se ele mesmo o cadastrou).

- Cada produto guarda seu **ID** e o **usuário dono** (a API devolve `emailDono`).
- A regra está em `ProdutoService.buscarProdutoDoUsuario()`; se o usuário não é o dono, a API responde **403**.
- O front também esconde os botões Editar/Excluir fora de "Meus produtos", mas a proteção real é a do back-end.

## 3. Funcionalidades

- Tela de boas-vindas, login, cadastro (nome completo, e-mail, senha e confirmação) e **login com Google**
- Home com **barra de pesquisa** (nome/descrição) e todos os produtos cadastrados
- **Meus produtos**: cadastrar, editar e excluir (CRUD)
- **Perfil**: altera nome, telefone, cidade e foto. No login com Google, nome e foto já vêm preenchidos
  automaticamente e o usuário pode alterá-los ou completar os demais campos

## 4. Como rodar

Pré-requisitos: Java 21, Node 20+ e MySQL em execução.

### Back-end
1. Ajuste usuário/senha do MySQL em `backend/src/main/resources/application.properties`
   (ou use as variáveis `DB_USER` e `DB_PASSWORD`). O banco `ecommerce_vv` e as tabelas são criados sozinhos.
2. Rode:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
   API em `http://localhost:8080`.

### Front-end
```bash
cd frontend
npm install
npm start
```
Acesse `http://localhost:4200`.

### Configurando o login com Google
1. Acesse o [Google Cloud Console](https://console.cloud.google.com/) → *APIs e serviços* → *Credenciais* →
   **Criar credenciais → ID do cliente OAuth** → tipo **Aplicativo da Web**.
2. Em **Origens JavaScript autorizadas** adicione `http://localhost:4200`.
3. Copie o **Client ID** e cole em **dois** lugares (o mesmo valor):
   - `frontend/src/environments/environment.ts` → `googleClientId`
   - `backend/src/main/resources/application.properties` → `app.google.client-id` (ou variável `GOOGLE_CLIENT_ID`)

Fluxo: o botão do Google entrega um *ID token* ao Angular → o Angular envia ao `POST /api/auth/google` →
o back-end valida o token com o Google, cria (ou encontra) o usuário pelo e-mail e devolve o token de sessão.

## 5. Endpoints

| Método | Rota                    | Descrição                              | Login? |
|--------|-------------------------|----------------------------------------|--------|
| POST   | `/api/auth/registrar`   | Cadastro                               | não |
| POST   | `/api/auth/login`       | Login com e-mail e senha               | não |
| POST   | `/api/auth/google`      | Login com Google                       | não |
| POST   | `/api/auth/logout`      | Encerra a sessão                       | — |
| GET    | `/api/produtos?busca=`  | Todos os produtos (com pesquisa)       | sim |
| GET    | `/api/produtos/meus`    | Produtos do usuário logado             | sim |
| GET    | `/api/produtos/{id}`    | Detalhe de um produto                  | sim |
| POST   | `/api/produtos`         | Cadastrar produto                      | sim |
| PUT    | `/api/produtos/{id}`    | Editar (só o dono)                     | sim |
| DELETE | `/api/produtos/{id}`    | Excluir (só o dono)                    | sim |
| GET/PUT| `/api/usuarios/me`      | Ver / alterar perfil                   | sim |

## 6. Simplificações do MVP (e como evoluir)

- Sessão por token aleatório guardado na tabela `sessoes` (mais simples que JWT). 
- O token do Google é validado pelo endpoint `tokeninfo`; em produção, use a biblioteca oficial do Google para validar localmente.
- `ddl-auto=update` cria as tabelas automaticamente; em produção, use migrações (Flyway).
- Imagem do produto é um link (sem upload de arquivo).
- O teste `VvApplicationTests` (gerado pelo Spring Initializr) precisa do MySQL ligado; rode a aplicação com `spring-boot:run`.
