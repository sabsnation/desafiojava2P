package com.desafio;

import com.desafio.config.DatabaseConfig;
import com.desafio.ui.MenuPrincipal;

public class Main {

    public static void main(String[] args) {
        try {
            DatabaseConfig.inicializar();
            new MenuPrincipal().executar();
        } catch (Exception e) {
            System.err.println("Falha ao iniciar aplicação: " + e.getMessage());
            System.err.println("Verifique o arquivo .env e se o MySQL está rodando.");
        } finally {
            DatabaseConfig.encerrar();
        }
    }
}
