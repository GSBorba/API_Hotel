package hotelaria.borba.api.enums;

public enum Categoria {
    UMA("1 Estrela"),
    DUAS("2 Estrelas"),
    TRES("3 Estrelas"),
    QUATRO("4 Estrelas"),
    CINCO("5 Estrelas"),
    BOUTIQUE("Boutique"),
    RESORT("Resort"),
    HOSTEL("Hostel");

    private final String nomeCategoria;

    Categoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getNomeCategoria() {
        return this.nomeCategoria;
    }
}
