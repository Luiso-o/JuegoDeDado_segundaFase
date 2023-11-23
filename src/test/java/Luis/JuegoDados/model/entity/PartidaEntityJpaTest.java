package Luis.JuegoDados.model.entity;

import Luis.JuegoDados.entity.JugadorEntityJpa;
import Luis.JuegoDados.entity.PartidaEntityJpa;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PartidaEntityJpaTest {

    @Test
    void testCrearPartidaEntity() {
        JugadorEntityJpa jugador = JugadorEntityJpa.builder()
                .id(1L)
                .nombre("Luis")
                .porcentajeExito(50)
                .build();

        PartidaEntityJpa partida = PartidaEntityJpa.builder()
                .id(1L)
                .fecha(LocalDate.of(2023, 8, 1))
                .victorias(3)
                .derrotas(2)
                .jugador(jugador)
                .build();

        assertEquals(1L, partida.getId());
        assertEquals(LocalDate.of(2023, 8, 1), partida.getFecha());
        assertEquals(3, partida.getVictorias());
        assertEquals(2, partida.getDerrotas());
        assertEquals(jugador, partida.getJugador());
    }

    @Test
    void testCrearPartidaEntityConBuilder() {
        PartidaEntityJpa partida = PartidaEntityJpa.builder()
                .id(2L)
                .fecha(LocalDate.of(2023, 8, 15))
                .victorias(1)
                .derrotas(0)
                .build();

        assertEquals(2L, partida.getId());
        assertEquals(LocalDate.of(2023, 8, 15), partida.getFecha());
        assertEquals(1, partida.getVictorias());
        assertEquals(0, partida.getDerrotas());
        // El jugador se establecerá en null debido a que no se proporciona en el builder
        assertNull(partida.getJugador());
    }

}