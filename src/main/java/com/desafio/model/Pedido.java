package com.desafio.model;

public class Pedido {

    private int id;
    private int clienteId;
    private String descricao;
    private double valorUnitario;
    private int quantidade;
    private String nomeCliente;

    public Pedido() {
    }

    public Pedido(int clienteId, String descricao, double valorUnitario, int quantidade) {
        this.clienteId = clienteId;
        this.descricao = descricao;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public double calcularTotal() {
        return valorUnitario * quantidade;
    }

    @Override
    public String toString() {
        String clienteInfo = nomeCliente != null ? nomeCliente : "Cliente #" + clienteId;
        return String.format(
                "ID: %d | Cliente: %s | %s | Qtd: %d | Unit: R$ %.2f | Total: R$ %.2f",
                id, clienteInfo, descricao, quantidade, valorUnitario, calcularTotal());
    }
}
