package Luis.JuegoDados.model.dto;

import Luis.JuegoDados.dto.JugadorDtoJpa;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class JugadorDtoJpaTest {

    @Test
    void testCrearJugadorDto() {
        JugadorDtoJpa jugadorDto = JugadorDtoJpa.builder()
                .id(1L)
                .nombre("Luis")
                .porcentajeExito(50)
                .build();

        assertEquals(1L, jugadorDto.getId());
        assertEquals("Luis", jugadorDto.getNombre());
        assertEquals(50, jugadorDto.getPorcentajeExito());
    }

    @Test
    void testCrearJugadorDtoConBuilder() {
        JugadorDtoJpa jugadorDto = JugadorDtoJpa.builder()
                .id(2L)
                .nombre("Carlos")
                .porcentajeExito(75)
                .build();

        assertEquals(2L, jugadorDto.getId());
        assertEquals("Carlos", jugadorDto.getNombre());
        assertEquals(75, jugadorDto.getPorcentajeExito());
    }

}