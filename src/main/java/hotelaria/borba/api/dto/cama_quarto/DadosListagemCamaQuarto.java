package hotelaria.borba.api.dto.cama_quarto;

import hotelaria.borba.api.domain.CamaQuarto;

public record DadosListagemCamaQuarto(Long id, Integer qt_cama, String tipoCama) {

    public DadosListagemCamaQuarto(CamaQuarto camaQuarto) {
        this(camaQuarto.getId(), camaQuarto.getQt_cama(), camaQuarto.getCama().getTipoCama().getNomeTipo());
    }
}
