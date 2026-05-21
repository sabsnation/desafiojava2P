package com.desafio.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConfig {

    private static HikariDataSource dataSource;

    private DatabaseConfig() {
    }

    public static void inicializar() {
        if (dataSource != null) {
            return;
        }

        String host = EnvConfig.get("DB_HOST");
        int port = EnvConfig.getInt("DB_PORT");
        String database = EnvConfig.get("DB_NAME");
        String user = EnvConfig.get("DB_USER");
        String password = EnvConfig.get("DB_PASSWORD");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo");
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);

        dataSource = new HikariDataSource(config);
        criarTabelas();
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("Banco de dados não foi inicializado.");
        }
        return dataSource.getConnection();
    }

    public static void encerrar() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    private static void criarTabelas() {
        String sqlClientes = """
                CREATE TABLE IF NOT EXISTS clientes (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL,
                    cep VARCHAR(8) NOT NULL,
                    logradouro VARCHAR(200),
                    bairro VARCHAR(100),
                    cidade VARCHAR(100),
                    uf VARCHAR(2)
                )
                """;

        String sqlPedidos = """
                CREATE TABLE IF NOT EXISTS pedidos (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    cliente_id INT NOT NULL,
                    descricao VARCHAR(200) NOT NULL,
                    valor_unitario DECIMAL(10, 2) NOT NULL,
                    quantidade INT NOT NULL,
                    CONSTRAINT fk_pedido_cliente
                        FOREIGN KEY (cliente_id) REFERENCES clientes(id)
                        ON DELETE CASCADE
                )
                """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlClientes);
            stmt.execute(sqlPedidos);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabelas: " + e.getMessage(), e);
        }
    }
}
