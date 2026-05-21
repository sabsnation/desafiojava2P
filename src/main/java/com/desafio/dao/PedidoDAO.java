package com.desafio.dao;

import com.desafio.config.DatabaseConfig;
import com.desafio.model.Pedido;
import com.desafio.util.Validador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public Pedido inserir(Pedido pedido) throws SQLException {
        Validador.validarTextoNaoVazio(pedido.getDescricao(), "Descrição");
        Validador.validarValorPositivo(pedido.getValorUnitario());
        Validador.validarQuantidadePositiva(pedido.getQuantidade());

        if (!clienteDAO.existe(pedido.getClienteId())) {
            throw new IllegalArgumentException("Cliente não encontrado. Cadastre o cliente antes do pedido.");
        }

        String sql = """
                INSERT INTO pedidos (cliente_id, descricao, valor_unitario, quantidade)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pedido.getClienteId());
            stmt.setString(2, pedido.getDescricao().trim());
            stmt.setDouble(3, pedido.getValorUnitario());
            stmt.setInt(4, pedido.getQuantidade());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    pedido.setId(keys.getInt(1));
                }
            }
        }

        return pedido;
    }

    public List<Pedido> listarTodos() throws SQLException {
        String sql = """
                SELECT p.*, c.nome AS nome_cliente
                FROM pedidos p
                INNER JOIN clientes c ON c.id = p.cliente_id
                ORDER BY p.id
                """;

        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapear(rs));
            }
        }

        return pedidos;
    }

    public List<Pedido> listarPorCliente(int clienteId) throws SQLException {
        String sql = """
                SELECT p.*, c.nome AS nome_cliente
                FROM pedidos p
                INNER JOIN clientes c ON c.id = p.cliente_id
                WHERE p.cliente_id = ?
                ORDER BY p.id
                """;

        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clienteId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapear(rs));
                }
            }
        }

        return pedidos;
    }

    public Optional<Pedido> buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT p.*, c.nome AS nome_cliente
                FROM pedidos p
                INNER JOIN clientes c ON c.id = p.cliente_id
                WHERE p.id = ?
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }

        return Optional.empty();
    }

    public boolean atualizar(Pedido pedido) throws SQLException {
        Validador.validarTextoNaoVazio(pedido.getDescricao(), "Descrição");
        Validador.validarValorPositivo(pedido.getValorUnitario());
        Validador.validarQuantidadePositiva(pedido.getQuantidade());

        if (!clienteDAO.existe(pedido.getClienteId())) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        String sql = """
                UPDATE pedidos
                SET cliente_id = ?, descricao = ?, valor_unitario = ?, quantidade = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pedido.getClienteId());
            stmt.setString(2, pedido.getDescricao().trim());
            stmt.setDouble(3, pedido.getValorUnitario());
            stmt.setInt(4, pedido.getQuantidade());
            stmt.setInt(5, pedido.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id"));
        pedido.setClienteId(rs.getInt("cliente_id"));
        pedido.setDescricao(rs.getString("descricao"));
        pedido.setValorUnitario(rs.getDouble("valor_unitario"));
        pedido.setQuantidade(rs.getInt("quantidade"));
        pedido.setNomeCliente(rs.getString("nome_cliente"));
        return pedido;
    }
}
