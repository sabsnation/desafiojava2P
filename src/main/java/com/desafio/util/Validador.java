package com.desafio.util;

public final class Validador {

    private Validador() {
    }

    public static void validarTextoNaoVazio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
    }

    public static void validarEmail(String email) {
        validarTextoNaoVazio(email, "E-mail");
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
    }

    public static void validarCep(String cep) {
        String apenasNumeros = cep.replaceAll("\\D", "");
        if (apenasNumeros.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos.");
        }
    }

    public static String normalizarCep(String cep) {
        return cep.replaceAll("\\D", "");
    }

    public static void validarValorPositivo(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor unitário deve ser maior que zero.");
        }
    }

    public static void validarQuantidadePositiva(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
    }

    public static int lerInteiroPositivo(String texto) {
        try {
            int valor = Integer.parseInt(texto.trim());
            if (valor <= 0) {
                throw new IllegalArgumentException("Informe um número inteiro maior que zero.");
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Informe um número inteiro válido.");
        }
    }

    public static double lerDoublePositivo(String texto) {
        try {
            double valor = Double.parseDouble(texto.trim().replace(",", "."));
            validarValorPositivo(valor);
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Informe um valor numérico válido.");
        }
    }
}
