# Planejamento – API REST Baozi Store

Trabalho de **Desenvolvimento Web Back-End** (UNINTER – Profa. Luciane Kanashiro).
Baseado no arquivo `instrucoes_trabalho/TRABALHO DE DESENVOLVIMENTO WEB BACK_2026.pdf`.

---

## 1. Regras de trabalho (valem para TODAS as etapas)

### 1.1 Git / GitHub
- **Todos** os commits e pushes são feitos com o usuário **diegomanaglia** (autor e committer: `Diego Managlia <diego.managlia@gmail.com>`).
  - Configuração local do repositório (antes do primeiro commit):
    ```bash
    git config user.name "Diego Managlia"
    git config user.email "diego.managlia@gmail.com"
    git config commit.gpgsign false
    ```
- **Nunca** pode existir rastro do Claude Code (nem de qualquer ferramenta de IA) em lugar nenhum:
  - nada de `Co-Authored-By`, "Generated with...", links de sessão ou assinaturas nas mensagens de commit;
  - nada em comentários, README, nomes de arquivos, `pom.xml`, coleção do Postman ou no PDF;
  - não versionar pastas/arquivos como `.claude/` e `CLAUDE.md`;
  - **exceção:** este `PLANEJAMENTO.md` é o **único** arquivo do repositório que pode mencionar o Claude.
- Este repositório é a **primeira versão** do trabalho; depois o trabalho será refeito em outro repositório.
- **Nunca** criar branches extras nem branches com nomes estranhos. **Não** abrir Pull Requests.
- Commit e push **sempre direto na branch `main`**:
  ```bash
  git checkout main
  git add <arquivos>
  git commit -m "mensagem"
  git push origin main
  ```
- Checagem antes de cada push:
  ```bash
  git log -3 --format='%an <%ae> | %cn <%ce> | %s%n%b'   # tem que aparecer só Diego Managlia
  git grep -i -n "claude\|anthropic" -- . ':!PLANEJAMENTO.md'  # não pode retornar nada
  ```
- Mensagens de commit curtas, em pt-br, no estilo de quem está aprendendo (ex.: `cria entidade Cliente`, `adiciona controller de pedidos`, `ajusta application.properties`).
- Commits pequenos, um por etapa, para o histórico ficar natural.

### 1.2 Estilo do código
- **Simplicidade máxima.** Só o que o trabalho pede, sem nada complexo:
  - sem camada de service, sem DTO, sem Lombok, sem MapStruct, sem tratamento global de exceções, sem segurança, sem Swagger, sem Docker;
  - getters e setters escritos "na mão" nas entidades.
- Escrever como um **programador júnior humano** em fase de aprendizado: código direto, nomes em português, sem "truques".
- **Comentários em pt-br** só em pontos específicos que ajudam a entender (para mim e para quem for corrigir), por exemplo:
  - o que faz cada anotação importante (`@Entity`, `@Id`, `@GeneratedValue`, `@RestController`, `@RequestMapping`, `@PathVariable`, `@RequestBody`);
  - por que o `PedidoController` confere se o cliente e o produto existem antes de salvar;
  - por que o GET por ID retorna 404 quando não acha;
  - o que significa cada linha do `application.properties`.
- Não comentar o óbvio (não comentar cada getter/setter).

---

## 2. O que o trabalho pede (resumo do PDF)

**Tecnologias obrigatórias:** Java, Spring Boot, Spring Data JPA, banco relacional (MySQL, MariaDB **ou H2**), JSON nos endpoints, padrão MVC do Spring.
**Arquitetura mínima:** packages `model`, `repository` e `controller`.
> Trabalho sem os requisitos técnicos obrigatórios é **zerado**.

### Entidades (conforme o DER do PDF)
| Entidade | Campos |
|---|---|
| **Produto** | `id` (Long), `nome` (String), `preco` (BigDecimal), `estoque` (Boolean) |
| **Cliente** | `id` (Long), `nome` (String), `clienteDesde` (LocalDate) |
| **Pedido** | `id` (Long), `clienteId` (Long), `produtoId` (Long), `quantidade` (Integer) |

Relacionamentos no DER: um Cliente **faz** vários Pedidos; um Produto é **vendido em** vários Pedidos.
Para manter simples, o Pedido guarda só `clienteId` e `produtoId` (exatamente como no DER), sem `@ManyToOne`.

### Endpoints obrigatórios (para cada entidade)
| Método | Rota | Ação |
|---|---|---|
| POST | `/clientes`, `/produtos`, `/pedidos` | criar |
| GET | `/clientes`, `/produtos`, `/pedidos` | listar todos |
| GET | `/clientes/{id}`, `/produtos/{id}`, `/pedidos/{id}` | consultar por ID |
| DELETE | `/clientes/{id}`, `/produtos/{id}`, `/pedidos/{id}` | apagar |
| PUT *(opcional)* | `/{entidade}/{id}` | atualizar |

### Critérios de avaliação
| Critério | Pontos |
|---|---|
| Entidades criadas corretamente | 2,0 |
| Endpoints funcionando | 2,0 |
| Banco configurado corretamente | 1,0 |
| Testes no Postman com prints | 2,0 |
| Documento PDF bem organizado | 1,0 |
| Clareza do estudo de caso | 1,0 |
| Diagrama de Caso de Uso | 1,0 |

---

## 3. Decisões técnicas (simples)

| Item | Escolha | Motivo |
|---|---|---|
| Java | 21 | já instalado e suportado pelo Spring Boot 4 |
| Build | Maven (com `mvnw`) | padrão do Spring Initializr; o professor roda sem instalar nada |
| Spring Boot | 4.1.1 (versão estável padrão do Initializr; a 3.x não é mais oferecida lá) | |
| Dependências | Spring Web (`spring-boot-starter-webmvc`), Spring Data JPA (`spring-boot-starter-data-jpa`), H2 (`h2` + `spring-boot-h2console`) | só o necessário |
| Banco | **H2 em memória** + console H2 habilitado | permitido pelo PDF, não precisa instalar nada, fácil de corrigir |
| Pacote base | `br.com.baozistore` | |

> Se preferir MySQL depois, basta trocar a dependência `h2` por `mysql-connector-j` e as linhas do `application.properties` (deixar isso comentado como exemplo no próprio arquivo).
> Atenção: com H2 em memória os dados somem quando a aplicação reinicia — fazer todos os prints do Postman na mesma execução.

### Estrutura de pastas
```
uninter-desenvolvimento-backend/
├── pom.xml
├── mvnw / mvnw.cmd / .mvn/
├── README.md
├── .gitignore
├── instrucoes_trabalho/            (já existe)
└── src/main/
    ├── java/br/com/baozistore/
    │   ├── BaoziStoreApplication.java
    │   ├── model/
    │   │   ├── Cliente.java
    │   │   ├── Produto.java
    │   │   └── Pedido.java
    │   ├── repository/
    │   │   ├── ClienteRepository.java
    │   │   ├── ProdutoRepository.java
    │   │   └── PedidoRepository.java
    │   └── controller/
    │       ├── ClienteController.java
    │       ├── ProdutoController.java
    │       └── PedidoController.java
    └── resources/
        └── application.properties
```

---

## 4. Etapas de desenvolvimento (cada etapa = 1 commit direto na `main`)

### Etapa 1 – Criar o projeto
- Gerar pelo Spring Initializr (Maven, Spring Boot 4.1.1, Java 21, Jar) com as dependências **Spring Web**, **Spring Data JPA** e **H2 Database**.
- Group `br.com.baozistore`, Artifact `baozi-store`.
- Remover o teste gerado automaticamente se não for usado (ou manter o `contextLoads` simples).
- Criar `.gitignore` (target/, .idea/, *.iml, .vscode/). As pastas de ferramentas de IA ficam só no `.git/info/exclude` (local), para o `.gitignore` não mencionar nada disso.
- Commit: `cria projeto spring boot da baozi store`

### Etapa 2 – Configurar o banco (`application.properties`)
```properties
spring.application.name=baozi-store
spring.datasource.url=jdbc:h2:mem:baozidb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
```
- Cada linha com um comentário curto em pt-br explicando.
- Testar: subir com `./mvnw spring-boot:run` e abrir `http://localhost:8080/h2-console`.
- Commit: `configura banco h2`

### Etapa 3 – Entidades (package `model`)
- `Cliente`, `Produto` e `Pedido` com `@Entity`, `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
- Construtor vazio + getters e setters.
- Conferir no console H2 que as tabelas `CLIENTE`, `PRODUTO` e `PEDIDO` foram criadas.
- Commit: `cria entidades cliente, produto e pedido`

### Etapa 4 – Repositórios (package `repository`)
- Interfaces que estendem `JpaRepository<Entidade, Long>` (sem métodos extras).
- Commit: `cria repositories`

### Etapa 5 – Controllers (package `controller`)
- Um `@RestController` por entidade, com `@RequestMapping("/clientes")` etc.
- Métodos (mesmo padrão nos três):
  - `@PostMapping` → `repository.save(...)` → **201 Created** com o objeto salvo;
  - `@GetMapping` → `repository.findAll()` → **200 OK**;
  - `@GetMapping("/{id}")` → `findById` → **200** ou **404 Not Found**;
  - `@DeleteMapping("/{id}")` → se existe, `deleteById` → **204 No Content**, senão **404**.
- `PedidoController` no POST: conferir com `existsById` se o cliente e o produto existem; se não existirem, retornar **400 Bad Request** (com comentário explicando).
- Commits separados:
  - `adiciona controller de clientes`
  - `adiciona controller de produtos`
  - `adiciona controller de pedidos`

### Etapa 6 (opcional) – PUT
- `@PutMapping("/{id}")` em cada controller: busca, altera os campos, salva; 404 se não achar.
- Só fazer se sobrar tempo. Commit: `adiciona put para atualizar`

### Etapa 7 – README
- Como rodar (`./mvnw spring-boot:run`), URL do console H2, lista de endpoints e exemplos de JSON.
- Commit: `adiciona readme`

### Etapa 8 – Testes no Postman (prints obrigatórios)
Rodar a aplicação **uma vez** e, na sequência, tirar print de cada requisição (mostrando URL, método, body e resposta):

1. **POST** `/clientes`
   ```json
   { "nome": "DiegoManaglia<RU>", "clienteDesde": "2026-10-07" }
   ```
2. **POST** `/produtos`
   ```json
   { "nome": "Baozi de Porco", "preco": 8.50, "estoque": true }
   ```
3. **POST** `/pedidos`
   ```json
   { "clienteId": 1, "produtoId": 1, "quantidade": 6 }
   ```
4. **GET** `/clientes`, `/produtos`, `/pedidos` (listagem geral)
5. **GET** `/clientes/1`, `/produtos/1`, `/pedidos/1` (consulta por ID)
6. **DELETE** de cada entidade (criar um registro extra só para apagar, assim os registros da história continuam aparecendo nos outros prints). Depois mostrar o GET por ID retornando 404.

> Os dados dos prints têm que ser **os mesmos** da descrição fictícia do PDF (nome + RU, nome do produto, quantidade).
> Opcional: exportar a coleção do Postman (`postman/baozi-store.postman_collection.json`) e versionar. Conferir que o JSON exportado não tem nada além das requisições.

### Etapa 9 – Documento PDF de entrega
Usar o modelo `instrucoes_trabalho/ATIVIDADE PRATICA - MODELO.docx` (capa com nome, RU, cidade/estado, ano 2026) e preencher as seções:

1. **Descrição da situação fictícia** – seguir o texto modelo do PDF com: cliente `DiegoManaglia<RU>`, produto `Baozi de Porco` vendido por unidade, pedido de 6 unidades.
2. **Diagrama de Caso de Uso (UML)** – fazer no draw.io (ou similar):
   - ator: **Usuário da API**;
   - casos de uso: *Cadastrar / Listar / Consultar por ID / Excluir* **Cliente**, **Produto** e **Pedido** (12 casos de uso; +3 de *Atualizar* se a Etapa 6 for feita);
   - opcional: `<<include>>` de "Cadastrar Pedido" para "Consultar Cliente" e "Consultar Produto" (validação feita no controller).
3. **Especificação da API** – tabela com entidades, campos/tipos e endpoints (pode reaproveitar as tabelas da seção 2 deste planejamento).
4. **Prints do Postman** – todos os da Etapa 8, com legenda curta em cada um.
5. **Link do repositório** – `https://github.com/diegomanaglia/uninter-desenvolvimento-backend`

Exportar como PDF e revisar.

---

## 5. Checklist final antes de entregar
- [ ] Projeto compila e sobe com `./mvnw spring-boot:run`
- [ ] Packages `model`, `repository` e `controller` existem
- [ ] As 3 entidades com os campos certos
- [ ] POST, GET, GET /{id} e DELETE /{id} funcionando nas 3 entidades
- [ ] Banco H2 configurado e tabelas criadas
- [ ] Prints do Postman com os mesmos dados da descrição fictícia
- [ ] Diagrama de caso de uso com ator "Usuário da API" e todos os casos de uso do CRUD
- [ ] PDF com as 5 seções na ordem do modelo
- [ ] Repositório público (ou acessível para a professora) com tudo na `main`
- [ ] `git log` mostra só **Diego Managlia** como autor/committer
- [ ] Nenhuma menção a Claude/IA em código, commits, README ou PDF
- [ ] Nenhuma branch além da `main` no GitHub

---

## 6. Pendências comigo (Diego)
- Informar o **RU** para usar no nome do cliente (`DiegoManaglia<RU>`).
- Confirmar o nome do produto (sugestão: "Baozi de Porco") e a quantidade do pedido (sugestão: 6).
- Informar cidade/estado para a capa do PDF.
