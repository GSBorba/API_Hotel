package hotelaria.borba.api.dto.cama;

import hotelaria.borba.api.domain.Cama;

public record DadosListagemCama(Long id, String tipoCama) {

    public DadosListagemCama(Cama cama) {
        this(cama.getId(), cama.getTipoCama().getNomeTipo());
    }
}
