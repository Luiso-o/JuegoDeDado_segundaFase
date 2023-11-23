package Luis.JuegoDados.model.entity;

import Luis.JuegoDados.entity.JugadorEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JugadorEntityJpaTest {

    @Test
    void testCrearJugadorEntity() {
        JugadorEntity jugador = new JugadorEntity(1L, "Luis", 0);

        assertEquals(1L, jugador.getId());
        assertEquals("Luis", jugador.getNombre());
        assertEquals(0, jugador.getPorcentajeExito());
    }

    @Test
    void testCrearJugadorEntityConBuilder() {
        JugadorEntity jugador = JugadorEntity.builder()
                .id(2L)
                .nombre("Ana")
                .porcentajeExito(75)
                .build();

        assertEquals(2L, jugador.getId());
        assertEquals("Ana", jugador.getNombre());
        assertEquals(75, jugador.getPorcentajeExito());
    }

}