package hotelaria.borba.api.advice;

import hotelaria.borba.api.exceptions.ValidacaoExceptions;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

public class TratamentoDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> erro400(MethodArgumentNotValidException ex) {
        List<FieldError> erros = ex.getFieldErrors();

        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> erro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ValidacaoExceptions.class)
    public ResponseEntity erroRegraDeNegocio(ValidacaoExceptions ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    public record DadosErroValidacao(String campo, String mensagem) {
        public DadosErroValidacao(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
