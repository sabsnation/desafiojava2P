# Desafio Extra — CRUD com Integração

Aplicação Java com CRUD completo para **Cliente** e **Pedido** (entidades relacionadas), integração com **MySQL** (JDBC + HikariCP), menu interativo e consumo da API **ViaCEP**.

Repositório: [https://github.com/sabsnation/desafiojava2P](https://github.com/sabsnation/desafiojava2P)

## Requisitos atendidos

| Requisito | Implementação |
|-----------|---------------|
| CRUD 2 entidades relacionadas | `Cliente` ↔ `Pedido` (FK `cliente_id`) |
| Banco de dados | MySQL com JDBC e pool HikariCP |
| Entrada pelo usuário | Menu interativo com `Scanner` |
| API externa | [ViaCEP](https://viacep.com.br/) ao cadastrar/atualizar cliente |
| Arquivo `.env` | Credenciais do banco via `dotenv-java` |
| Maven | `pom.xml` com dependências |
| README | Este arquivo |

## Tecnologias

- Java 17
- Maven
- MySQL 8+
- JDBC + HikariCP
- Gson (JSON da ViaCEP)
- dotenv-java

## Pré-requisitos

- JDK 17+
- Maven 3.8+
- MySQL instalado e em execução

## Configuração do MySQL

1. Inicie o MySQL.
2. Crie o banco (ou use o script):

```sql
CREATE DATABASE IF NOT EXISTS desafio_crud;
```

3. O programa cria as tabelas automaticamente na primeira execução.

## Configuração do `.env`

```bash
cp .env.example .env
```

Edite o `.env` com seus dados:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=desafio_crud
DB_USER=root
DB_PASSWORD=sua_senha
```

> O arquivo `.env` **não** vai para o Git (está no `.gitignore`).

## Como executar

```bash
cd desafo
mvn clean compile
mvn exec:java
```

Ou em um comando:

```bash
mvn clean compile exec:java
```

## Menu do sistema

```
======== SISTEMA CRUD - CLIENTE & PEDIDO ========
1 - Gerenciar Clientes   (CRUD completo)
2 - Gerenciar Pedidos    (CRUD completo)
3 - Consultar CEP        (ViaCEP)
0 - Sair
```

### Fluxo sugerido para testar

1. Cadastre um **cliente** informando CEP → endereço preenchido pela ViaCEP.
2. Cadastre **pedidos** vinculados ao ID do cliente.
3. Liste, atualize e exclua registros pelos submenus.
4. Use a opção 3 para consultar um CEP sem cadastrar cliente.

## Estrutura do projeto

```
src/main/java/com/desafio/
├── Main.java
├── api/          ViaCEP (API externa)
├── config/       .env e conexão MySQL
├── dao/          CRUD JDBC
├── model/        Cliente e Pedido
├── ui/           Menu interativo
└── util/         Validações
```

## Relacionamento

- Um **Cliente** pode ter vários **Pedidos**.
- Ao excluir um cliente, os pedidos são removidos (`ON DELETE CASCADE`).

## Entrega

- Projeto **individual**
- Repositório **público** no GitHub
- Commits até **22/05/2026 às 23:59**
- Todos os requisitos obrigatórios devem estar presentes

## Autor

Sabrina — projeto acadêmico 2° período.
