package com.desafio.config;

import io.github.cdimascio.dotenv.Dotenv;

public final class EnvConfig {

    private static final Dotenv DOTENV = Dotenv.configure()
            .directory(System.getProperty("user.dir"))
            .filename(".env")
            .ignoreIfMissing()
            .load();

    private EnvConfig() {
    }

    public static String get(String key) {
        String value = DOTENV.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Variável de ambiente não configurada: " + key
                            + ". Copie .env.example para .env e preencha os valores.");
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
