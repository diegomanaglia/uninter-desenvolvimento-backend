# Baozi Store - API REST

Trabalho de Desenvolvimento Web - Back End (UNINTER).

A Baozi Store é uma lojinha que vende pão chinês. Fiz essa API para cadastrar os clientes, os produtos e os pedidos da loja. Em cada pedido, um cliente compra um produto em uma certa quantidade.

Usei Java 21, Spring Boot, Spring Data JPA e o banco H2, que roda em memória junto com a aplicação.

## Como rodar

Só precisa ter o Java 21 instalado. O Maven já vem no projeto pelo `mvnw`.

```powershell
.\mvnw.cmd spring-boot:run
```

No Linux/Mac é `./mvnw spring-boot:run`. No PowerShell não esqueça do `.\` antes do `mvnw.cmd`, senão dá erro.

A API sobe em `http://localhost:8080`. Os dados ficam só na memória, então somem quando a aplicação é reiniciada.

Para ver as tabelas, acesse `http://localhost:8080/h2-console` com a URL `jdbc:h2:mem:baozidb`, usuário `sa` e senha em branco.

## Endpoints

Os três recursos são `/clientes`, `/produtos` e `/pedidos`, e todos funcionam do mesmo jeito:

| Método | URL | O que faz |
|---|---|---|
| POST | `/clientes` | cadastra |
| GET | `/clientes` | lista todos |
| GET | `/clientes/{id}` | busca pelo id |
| PUT | `/clientes/{id}` | atualiza |
| DELETE | `/clientes/{id}` | apaga |

Se o id não existir, a API devolve 404. No pedido, ela também confere se o cliente e o produto existem e devolve 400 se algum não existir.

Exemplo de pedido (`POST /pedidos`):

```json
{
  "clienteId": 1,
  "produtoId": 1,
  "quantidade": 6
}
```

## Testes

As requisições que usei no Postman estão em `postman/baozi-store.postman_collection.json`. É só importar e rodar as pastas na ordem.

O documento do trabalho com os prints está na pasta `entregas/`.
