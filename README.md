# Baozi Store - API REST

Trabalho da disciplina **Desenvolvimento Web Back-End** (UNINTER).

A Baozi Store é uma pequena loja que vende pão chinês (baozi). Essa API faz o controle básico de **clientes**, **produtos** e **pedidos**. Em cada pedido, um cliente compra um único produto em determinada quantidade.

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring Web (endpoints REST com JSON)
- Spring Data JPA
- Banco de dados H2 (em memória)
- Maven
- Postman (para testar)

## Estrutura do projeto

```
src/main/java/br/com/baozistore/
├── BaoziStoreApplication.java   -> classe principal (inicia a aplicação)
├── model/                       -> entidades (viram tabelas no banco)
│   ├── Cliente.java
│   ├── Produto.java
│   └── Pedido.java
├── repository/                  -> acesso ao banco (JpaRepository)
│   ├── ClienteRepository.java
│   ├── ProdutoRepository.java
│   └── PedidoRepository.java
└── controller/                  -> endpoints REST
    ├── ClienteController.java
    ├── ProdutoController.java
    └── PedidoController.java
```

## Como rodar

Precisa ter o **Java 21** instalado. Não precisa instalar o Maven nem banco de dados, o projeto já vem com o Maven Wrapper (`mvnw`) e usa o H2.

No Linux/Mac:

```bash
./mvnw spring-boot:run
```

No Windows (terminal do VS Code ou PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

> No PowerShell é obrigatório usar o `.\` antes do `mvnw.cmd`. Sem ele aparece o erro "O termo 'mvnw.cmd' não é reconhecido".

A API fica disponível em `http://localhost:8080`.

Também dá para abrir o projeto no IntelliJ / Eclipse / VS Code e rodar a classe `BaoziStoreApplication`.

> Como o H2 está em memória, os dados são apagados toda vez que a aplicação é reiniciada.

## Console do banco H2

Com a aplicação rodando, acessar `http://localhost:8080/h2-console` e usar:

- **JDBC URL:** `jdbc:h2:mem:baozidb`
- **User Name:** `sa`
- **Password:** (deixar em branco)

## Testes no Postman

A coleção com todas as requisições está em `postman/baozi-store.postman_collection.json`.

1. No Postman, clicar em **Import** e escolher esse arquivo.
2. Rodar a aplicação (`.\mvnw.cmd spring-boot:run`).
3. Executar as pastas **na ordem** (1, 2, 3, 4 e 5), porque os ids dependem dessa ordem. Se reiniciar a aplicação, começar de novo pela pasta 1.

A URL base fica na variável `baseUrl` da coleção (`http://localhost:8080`).

## Entidades

| Entidade | Campos |
|---|---|
| Cliente | `id` (Long), `nome` (String), `clienteDesde` (LocalDate) |
| Produto | `id` (Long), `nome` (String), `preco` (BigDecimal), `estoque` (Boolean) |
| Pedido | `id` (Long), `clienteId` (Long), `produtoId` (Long), `quantidade` (Integer) |

## Endpoints

### Clientes

| Método | URL | O que faz |
|---|---|---|
| POST | `/clientes` | cadastra um cliente |
| GET | `/clientes` | lista todos os clientes |
| GET | `/clientes/{id}` | busca um cliente pelo id |
| PUT | `/clientes/{id}` | atualiza um cliente |
| DELETE | `/clientes/{id}` | apaga um cliente |

### Produtos

| Método | URL | O que faz |
|---|---|---|
| POST | `/produtos` | cadastra um produto |
| GET | `/produtos` | lista todos os produtos |
| GET | `/produtos/{id}` | busca um produto pelo id |
| PUT | `/produtos/{id}` | atualiza um produto |
| DELETE | `/produtos/{id}` | apaga um produto |

### Pedidos

| Método | URL | O que faz |
|---|---|---|
| POST | `/pedidos` | cadastra um pedido |
| GET | `/pedidos` | lista todos os pedidos |
| GET | `/pedidos/{id}` | busca um pedido pelo id |
| PUT | `/pedidos/{id}` | atualiza um pedido |
| DELETE | `/pedidos/{id}` | apaga um pedido |

### Respostas (status HTTP)

- `201 Created` - cadastro feito com sucesso (POST)
- `200 OK` - consulta ou atualização feita com sucesso (GET e PUT)
- `204 No Content` - registro apagado (DELETE)
- `404 Not Found` - não existe registro com o id informado
- `400 Bad Request` - no pedido, quando o `clienteId` ou o `produtoId` não existem

## Exemplos de JSON

Nos exemplos o header precisa ser `Content-Type: application/json`.

**POST /clientes**

```json
{
  "nome": "Joao Silva",
  "clienteDesde": "2026-10-07"
}
```

**POST /produtos**

```json
{
  "nome": "Baozi de Porco",
  "preco": 8.50,
  "estoque": true
}
```

**POST /pedidos** (o cliente e o produto precisam estar cadastrados antes)

```json
{
  "clienteId": 1,
  "produtoId": 1,
  "quantidade": 6
}
```

Resposta do POST /pedidos:

```json
{
  "clienteId": 1,
  "id": 1,
  "produtoId": 1,
  "quantidade": 6
}
```
