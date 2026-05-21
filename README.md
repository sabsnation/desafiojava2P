# Sistema de Controle de Pedidos de Restaurante

Desafio de POO — Java | 2° Período

## Estrutura

| Classe | Responsabilidade |
|--------|------------------|
| `Cliente` | Dados do cliente e manipulação de `String` |
| `Pedido` | Itens do pedido e total por item |
| `ContaRestaurante` | Cálculos da conta e relatório |
| `Main` | Instanciação e execução |

## Como compilar e executar

```bash
cd src
javac *.java
java Main
```

## Regras de negócio

- **Taxa de serviço:** 10% se subtotal > R$ 100; senão 5%
- **Desconto:** R$ 15 se o primeiro nome começar com **A**
- **Valor final:** subtotal + taxa − desconto

## Entrega 

1. Criar repositório **público** no GitHub
2. Cada integrante deve ter pelo menos um commit relevante
3. Enviar o link do repositório até **08/04 às 19h**
