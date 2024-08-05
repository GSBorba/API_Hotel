package hotelaria.borba.api.repository;

import hotelaria.borba.api.domain.Quarto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface QuartoRepository extends JpaRepository<Quarto, Long> {

    @Query("""
            select  q
            from    Quarto q
            where   q.id not in (select     qr.quarto.id
                                from        QuartoReserva qr
                                inner join  Reserva r on qr.reserva.id = r.id
                                where       r.checkin < :checkout
                                and         r.checkout > :checkin)
            """)
    Page<Quarto> listaQuartosSemReservaNaDataEspecifica(@Param("checkin") LocalDate checkin,
                                                        @Param("checkout") LocalDate checkout,
                                                        Pageable pageable);
    @Query("""
            select  q
            from    Quarto q
            where   q.id not in (select     qr.quarto.id
                                from        QuartoReserva qr
                                inner join  Reserva r on qr.reserva.id = r.id
                                where       r.checkin < :checkout
                                and         r.checkout > :checkin)
            """)
    List<Quarto> listaTodosQuartosSemReservaNaDataEspecifica(@Param("checkin") LocalDate checkin,
                                                             @Param("checkout") LocalDate checkout);

    @Query("""
            SELECT (count(q) > 0)
            FROM Quarto q
            WHERE q.id IN (
                SELECT qr.quarto.id
                FROM QuartoReserva qr
                WHERE qr.reserva.id = :id_reserva)
            AND q.id NOT IN (
                SELECT qr.quarto.id
                FROM QuartoReserva qr
                INNER JOIN Reserva r ON qr.reserva.id = r.id
                WHERE r.id <> :id_reserva
                AND r.checkin < :checkout
                AND r.checkout > :checkin
            """)
    boolean retornaSePodeAtualizarDataDaReservaDoQuarto(@Param("checkin") LocalDate checkin,
                                                        @Param("checkout") LocalDate checkout,
                                                        @Param("id_reserva") Long id_reserva);
}
