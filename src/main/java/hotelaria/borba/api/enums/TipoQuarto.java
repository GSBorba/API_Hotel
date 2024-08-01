package hotelaria.borba.api.enums;

public enum TipoQuarto {
    STANDARD("Quarto Básico"),
    DELUX("Quarto Delux"),
    SUITE("Suite"),
    PRESIDENCIAL("Suite Presidencial"),
    EXECUTIVO("Quarto Executivo"),
    LOFT("Loft"),
    VILLA("Villa"),
    ADAPTADO("Quarto com acessibilidades");

    private final String nomeTipo;

    TipoQuarto(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    public String getNomeTipo() {
        return this.nomeTipo;
    }
}
