public class Main {

    public static void main(String[] args) {
        Cliente cliente = new Cliente("Ana Beatriz", 5);

        Pedido pedido1 = new Pedido("Pizza Grande", 45.00, 1);
        Pedido pedido2 = new Pedido("Refrigerante", 8.00, 2);
        Pedido pedido3 = new Pedido("Sobremesa", 18.00, 1);

        ContaRestaurante conta = new ContaRestaurante(cliente, pedido1, pedido2, pedido3);

        conta.exibirRelatorio();
    }
}
