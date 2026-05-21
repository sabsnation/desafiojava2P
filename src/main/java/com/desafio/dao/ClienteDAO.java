package com.desafio.dao;

import com.desafio.config.DatabaseConfig;
import com.desafio.model.Cliente;
import com.desafio.util.Validador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO {

    public Cliente inserir(Cliente cliente) throws SQLException {
        Validador.validarTextoNaoVazio(cliente.getNome(), "Nome");
        Validador.validarEmail(cliente.getEmail());
        Validador.validarCep(cliente.getCep());

        String sql = """
                INSERT INTO clientes (nome, email, cep, logradouro, bairro, cidade, uf)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome().trim());
            stmt.setString(2, cliente.getEmail().trim());
            stmt.setString(3, Validador.normalizarCep(cliente.getCep()));
            stmt.setString(4, cliente.getLogradouro());
            stmt.setString(5, cliente.getBairro());
            stmt.setString(6, cliente.getCidade());
            stmt.setString(7, cliente.getUf());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    cliente.setId(keys.getInt(1));
                }
            }
        }

        return cliente;
    }

    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT * FROM clientes ORDER BY id";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapear(rs));
            }
        }

        return clientes;
    }

    public Optional<Cliente> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";

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

    public boolean atualizar(Cliente cliente) throws SQLException {
        Validador.validarTextoNaoVazio(cliente.getNome(), "Nome");
        Validador.validarEmail(cliente.getEmail());
        Validador.validarCep(cliente.getCep());

        String sql = """
                UPDATE clientes
                SET nome = ?, email = ?, cep = ?, logradouro = ?, bairro = ?, cidade = ?, uf = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome().trim());
            stmt.setString(2, cliente.getEmail().trim());
            stmt.setString(3, Validador.normalizarCep(cliente.getCep()));
            stmt.setString(4, cliente.getLogradouro());
            stmt.setString(5, cliente.getBairro());
            stmt.setString(6, cliente.getCidade());
            stmt.setString(7, cliente.getUf());
            stmt.setInt(8, cliente.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean existe(int id) throws SQLException {
        return buscarPorId(id).isPresent();
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setCep(rs.getString("cep"));
        cliente.setLogradouro(rs.getString("logradouro"));
        cliente.setBairro(rs.getString("bairro"));
        cliente.setCidade(rs.getString("cidade"));
        cliente.setUf(rs.getString("uf"));
        return cliente;
    }
}
