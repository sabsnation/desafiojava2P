package com.desafio.ui;

import com.desafio.api.ViaCepResponse;
import com.desafio.api.ViaCepService;
import com.desafio.dao.ClienteDAO;
import com.desafio.dao.PedidoDAO;
import com.desafio.model.Cliente;
import com.desafio.model.Pedido;
import com.desafio.util.Validador;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner = new Scanner(System.in);
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final ViaCepService viaCepService = new ViaCepService();

    public void executar() {
        boolean executando = true;

        while (executando) {
            System.out.println("\n======== SISTEMA CRUD - CLIENTE & PEDIDO ========");
            System.out.println("1 - Gerenciar Clientes");
            System.out.println("2 - Gerenciar Pedidos");
            System.out.println("3 - Consultar endereço por CEP (ViaCEP)");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            try {
                int opcao = lerOpcao();
                switch (opcao) {
                    case 1 -> menuClientes();
                    case 2 -> menuPedidos();
                    case 3 -> consultarCep();
                    case 0 -> executando = false;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }

        System.out.println("Encerrando sistema. Até logo!");
    }

    private void menuClientes() {
        boolean voltar = false;

        while (!voltar) {
            System.out.println("\n--- CLIENTES ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Atualizar");
            System.out.println("5 - Deletar");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            try {
                switch (lerOpcao()) {
                    case 1 -> cadastrarCliente();
                    case 2 -> listarClientes();
                    case 3 -> buscarCliente();
                    case 4 -> atualizarCliente();
                    case 5 -> deletarCliente();
                    case 0 -> voltar = true;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erro no banco: " + e.getMessage());
            }
        }
    }

    private void menuPedidos() {
        boolean voltar = false;

        while (!voltar) {
            System.out.println("\n--- PEDIDOS ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todos");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Atualizar");
            System.out.println("5 - Deletar");
            System.out.println("6 - Listar por cliente");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            try {
                switch (lerOpcao()) {
                    case 1 -> cadastrarPedido();
                    case 2 -> listarPedidos();
                    case 3 -> buscarPedido();
                    case 4 -> atualizarPedido();
                    case 5 -> deletarPedido();
                    case 6 -> listarPedidosPorCliente();
                    case 0 -> voltar = true;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erro no banco: " + e.getMessage());
            }
        }
    }

    private void cadastrarCliente() throws SQLException {
        System.out.println("\n>> Cadastro de Cliente");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("CEP (será consultado na ViaCEP): ");
        String cep = scanner.nextLine();

        ViaCepResponse endereco;
        try {
            endereco = buscarEnderecoApi(cep);
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao consultar ViaCEP: " + e.getMessage());
            return;
        }

        Cliente cliente = new Cliente(
                nome,
                email,
                Validador.normalizarCep(cep),
                endereco.getLogradouro(),
                endereco.getBairro(),
                endereco.getLocalidade(),
                endereco.getUf());

        clienteDAO.inserir(cliente);
        System.out.println("Cliente cadastrado com sucesso!");
        System.out.println(cliente);
    }

    private void listarClientes() throws SQLException {
        List<Cliente> clientes = clienteDAO.listarTodos();
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        System.out.println("\n>> Lista de Clientes");
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    private void buscarCliente() throws SQLException {
        System.out.print("ID do cliente: ");
        int id = Validador.lerInteiroPositivo(scanner.nextLine());

        Optional<Cliente> cliente = clienteDAO.buscarPorId(id);
        if (cliente.isPresent()) {
            System.out.println(cliente.get());
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    private void atualizarCliente() throws SQLException {
        System.out.print("ID do cliente para atualizar: ");
        int id = Validador.lerInteiroPositivo(scanner.nextLine());

        Optional<Cliente> existente = clienteDAO.buscarPorId(id);
        if (existente.isEmpty()) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        Cliente cliente = existente.get();
        System.out.println("Deixe em branco para manter o valor atual.");

        System.out.print("Nome [" + cliente.getNome() + "]: ");
        String nome = scanner.nextLine();
        if (!nome.isBlank()) {
            cliente.setNome(nome);
        }

        System.out.print("E-mail [" + cliente.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.isBlank()) {
            cliente.setEmail(email);
        }

        System.out.print("CEP [" + cliente.getCep() + "] (Enter para manter): ");
        String cep = scanner.nextLine();
        if (!cep.isBlank()) {
            ViaCepResponse endereco;
            try {
                endereco = buscarEnderecoApi(cep);
            } catch (IOException | InterruptedException e) {
                System.out.println("Erro ao consultar ViaCEP: " + e.getMessage());
                return;
            }
            cliente.setCep(Validador.normalizarCep(cep));
            cliente.setLogradouro(endereco.getLogradouro());
            cliente.setBairro(endereco.getBairro());
            cliente.setCidade(endereco.getLocalidade());
            cliente.setUf(endereco.getUf());
        }

        if (clienteDAO.atualizar(cliente)) {
            System.out.println("Cliente atualizado com sucesso!");
        } else {
            System.out.println("Não foi possível atualizar o cliente.");
        }
    }

    private void deletarCliente() throws SQLException {
        System.out.print("ID do cliente para deletar: ");
        int id = Validador.lerInteiroPositivo(scanner.nextLine());

        System.out.print("Confirma exclusão? (s/n): ");
        String confirma = scanner.nextLine();
        if (!confirma.equalsIgnoreCase("s")) {
            System.out.println("Exclusão cancelada.");
            return;
        }

        if (clienteDAO.deletar(id)) {
            System.out.println("Cliente removido (pedidos vinculados também foram excluídos).");
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    private void cadastrarPedido() throws SQLException {
        System.out.println("\n>> Cadastro de Pedido");
        listarClientesResumo();

        System.out.print("ID do cliente: ");
        int clienteId = Validador.lerInteiroPositivo(scanner.nextLine());

        System.out.print("Descrição do item: ");
        String descricao = scanner.nextLine();

        System.out.print("Valor unitário: ");
        double valor = Validador.lerDoublePositivo(scanner.nextLine());

        System.out.print("Quantidade: ");
        int quantidade = Validador.lerInteiroPositivo(scanner.nextLine());

        Pedido pedido = new Pedido(clienteId, descricao, valor, quantidade);
        pedidoDAO.inserir(pedido);
        System.out.println("Pedido cadastrado com sucesso!");
        System.out.println(pedido);
    }

    private void listarPedidos() throws SQLException {
        List<Pedido> pedidos = pedidoDAO.listarTodos();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }
        System.out.println("\n>> Lista de Pedidos");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }
    }

    private void listarPedidosPorCliente() throws SQLException {
        System.out.print("ID do cliente: ");
        int clienteId = Validador.lerInteiroPositivo(scanner.nextLine());

        List<Pedido> pedidos = pedidoDAO.listarPorCliente(clienteId);
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido encontrado para este cliente.");
            return;
        }

        System.out.println("\n>> Pedidos do cliente #" + clienteId);
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }
    }

    private void buscarPedido() throws SQLException {
        System.out.print("ID do pedido: ");
        int id = Validador.lerInteiroPositivo(scanner.nextLine());

        Optional<Pedido> pedido = pedidoDAO.buscarPorId(id);
        if (pedido.isPresent()) {
            System.out.println(pedido.get());
        } else {
            System.out.println("Pedido não encontrado.");
        }
    }

    private void atualizarPedido() throws SQLException {
        System.out.print("ID do pedido para atualizar: ");
        int id = Validador.lerInteiroPositivo(scanner.nextLine());

        Optional<Pedido> existente = pedidoDAO.buscarPorId(id);
        if (existente.isEmpty()) {
            System.out.println("Pedido não encontrado.");
            return;
        }

        Pedido pedido = existente.get();
        listarClientesResumo();

        System.out.print("ID do cliente [" + pedido.getClienteId() + "]: ");
        String clienteIdTxt = scanner.nextLine();
        if (!clienteIdTxt.isBlank()) {
            pedido.setClienteId(Validador.lerInteiroPositivo(clienteIdTxt));
        }

        System.out.print("Descrição [" + pedido.getDescricao() + "]: ");
        String descricao = scanner.nextLine();
        if (!descricao.isBlank()) {
            pedido.setDescricao(descricao);
        }

        System.out.print("Valor unitário [" + pedido.getValorUnitario() + "]: ");
        String valorTxt = scanner.nextLine();
        if (!valorTxt.isBlank()) {
            pedido.setValorUnitario(Validador.lerDoublePositivo(valorTxt));
        }

        System.out.print("Quantidade [" + pedido.getQuantidade() + "]: ");
        String qtdTxt = scanner.nextLine();
        if (!qtdTxt.isBlank()) {
            pedido.setQuantidade(Validador.lerInteiroPositivo(qtdTxt));
        }

        if (pedidoDAO.atualizar(pedido)) {
            System.out.println("Pedido atualizado com sucesso!");
        } else {
            System.out.println("Não foi possível atualizar o pedido.");
        }
    }

    private void deletarPedido() throws SQLException {
        System.out.print("ID do pedido para deletar: ");
        int id = Validador.lerInteiroPositivo(scanner.nextLine());

        if (pedidoDAO.deletar(id)) {
            System.out.println("Pedido removido com sucesso!");
        } else {
            System.out.println("Pedido não encontrado.");
        }
    }

    private void consultarCep() {
        System.out.println("\n>> Consulta ViaCEP");
        System.out.print("Digite o CEP: ");
        String cep = scanner.nextLine();

        try {
            ViaCepResponse endereco = buscarEnderecoApi(cep);
            System.out.println("CEP: " + endereco.getCep());
            System.out.println("Logradouro: " + endereco.getLogradouro());
            System.out.println("Bairro: " + endereco.getBairro());
            System.out.println("Cidade: " + endereco.getLocalidade());
            System.out.println("UF: " + endereco.getUf());
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private ViaCepResponse buscarEnderecoApi(String cep) throws IOException, InterruptedException {
        ViaCepResponse endereco = viaCepService.buscarPorCep(cep);
        System.out.println("Endereço encontrado: "
                + endereco.getLogradouro() + ", "
                + endereco.getBairro() + " - "
                + endereco.getLocalidade() + "/" + endereco.getUf());
        return endereco;
    }

    private void listarClientesResumo() throws SQLException {
        List<Cliente> clientes = clienteDAO.listarTodos();
        if (clientes.isEmpty()) {
            throw new IllegalArgumentException("Cadastre um cliente antes de criar pedidos.");
        }
        System.out.println("Clientes disponíveis:");
        for (Cliente c : clientes) {
            System.out.println("  #" + c.getId() + " - " + c.getNome());
        }
    }

    private int lerOpcao() {
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Digite um número válido para a opção.");
        }
    }
}
