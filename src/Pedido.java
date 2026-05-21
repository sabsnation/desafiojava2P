public class Pedido {

    private String descricao;
    private double valorUnitario;
    private int quantidade;

    public Pedido(String descricao, double valorUnitario, int quantidade) {
        this.descricao = descricao;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double calcularTotalItem() {
        return valorUnitario * quantidade;
    }
}
