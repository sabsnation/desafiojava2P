public class Cliente {

    private String nomeCompleto;
    private int mesa;

    public Cliente(String nomeCompleto, int mesa) {
        this.nomeCompleto = nomeCompleto;
        this.mesa = mesa;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public int getMesa() {
        return mesa;
    }

    public String getPrimeiroNome() {
        int indiceEspaco = nomeCompleto.indexOf(" ");
        if (indiceEspaco == -1) {
            return nomeCompleto;
        }
        return nomeCompleto.substring(0, indiceEspaco);
    }

    public String nomeMaiusculo() {
        return nomeCompleto.toUpperCase();
    }
}
