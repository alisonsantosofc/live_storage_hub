# Live Storage Hub

Backend compartilhado para armazenar dados JSON e arquivos de múltiplos aplicativos. O ambiente local usa PostgreSQL e um volume Docker para os arquivos.

## Executar localmente

1. Copie `.env.example` para `.env` e defina chaves locais seguras.
2. Inicie os serviços:

   ```bash
   docker compose up -d --build
   ```

3. Acesse o Swagger em <http://localhost:9090/swagger-ui/index.html>.

Para acompanhar a inicialização:

```bash
docker compose logs -f api
```

Para parar sem apagar banco ou arquivos:

```bash
docker compose down
```

Não use `docker compose down -v` se quiser preservar os dados locais.

## Fluxo básico

1. `POST /apps` com o header `X-Admin-Key` cria um aplicativo e retorna sua API key.
2. `POST /users` com `X-Api-Key` cadastra um usuário.
3. `POST /auth` com `X-Api-Key` autentica e retorna um JWT.
4. As rotas `/users/data` e `/users/files` usam `Authorization: Bearer <token>`. O usuário e o app são obtidos exclusivamente do JWT.

## Arquivos locais

Os arquivos são persistidos no volume Docker `file_storage` e organizados internamente assim:

```text
apps/{appId}/users/{userId}/{uuid}
```

Essa mesma chave será usada no bucket S3 único em produção. O caminho interno nunca é usado como autorização; download e exclusão passam pela API autenticada.

Rotas disponíveis:

```text
POST   /users/files
GET    /users/files
GET    /users/files/{fileId}/download
DELETE /users/files/{fileId}
```

O limite padrão de upload é 10 MB e pode ser ajustado no `.env`.

## Respostas e testes

As respostas de erro seguem códigos estáveis documentados em [`ERRORS.md`](ERRORS.md).

Execute a suíte automatizada com:

```bash
./mvnw clean test
```
