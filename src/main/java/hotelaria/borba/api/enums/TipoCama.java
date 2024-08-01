package hotelaria.borba.api.enums;

public enum TipoCama {
    SOLTEIRO("Cama de Solteiro"),
    CASAL("Cama de Casal"),
    QUEEN("Cama de Casal Queen Size"),
    KING("Cama de Casal King Size"),
    SOLTEIROXL("Cama de Solteiro Longa");


    private final String nomeTipo;

    TipoCama(String nomeTipo) {
        this.nomeTipo = nomeTipo;
    }

    public String getNomeTipo() {
        return this.nomeTipo;
    }
}
