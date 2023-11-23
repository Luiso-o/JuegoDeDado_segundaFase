package Luis.JuegoDados.model.repository;

import Luis.JuegoDados.entity.JugadorEntityJpa;
import Luis.JuegoDados.repository.JugadorRepositoryJpa;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/*Para estas pruebas estoy usando H2 como base de datos
* así estas pruebas no alteran la base de datos de producción
*/
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JugadorRepositoryJpaTest {

    @Autowired
    private JugadorRepositoryJpa jugadorRepository;

    @Test
    @Order(1)
    @DirtiesContext
    public void testGuardarJugador() {
        JugadorEntityJpa jugador = new JugadorEntityJpa(1L, "Luis", 0);
        jugadorRepository.save(jugador);

        assertNotNull(jugador);
        assertThat(jugador.getId()).isEqualTo(1);
        assertThat(jugador.getNombre()).isEqualTo("Luis");
        assertThat(jugador.getPorcentajeExito()).isEqualTo(0);
    }

    @Test
    @Order(2)
    @DirtiesContext
    public void testBuscarJugadorExistente() {
       JugadorEntityJpa jugador = new JugadorEntityJpa(1L, "Luis", 0);
       jugadorRepository.save(jugador);
        Optional<JugadorEntityJpa> jugadorEncontrado = jugadorRepository.findById(1L);
        assertThat(jugadorEncontrado).isPresent();
        jugadorEncontrado.ifPresent(j -> assertThat(j.getNombre()).isEqualTo("Luis"));
    }

  @Test
    @Order(3)
    public void testBuscarJugadorNoExistente() {
        Optional<JugadorEntityJpa> jugadorNoEncontrado = jugadorRepository.findById(2L);
        assertThat(jugadorNoEncontrado).isEmpty();
    }


   @Test
   @DirtiesContext
   @Order(4)
    public void testActualizarJugador() {
       JugadorEntityJpa jugador = new JugadorEntityJpa(1L, "Luis", 0);
       jugadorRepository.save(jugador);

        Optional<JugadorEntityJpa> jugadorGuardado = jugadorRepository.findById(1L);
        assertThat(jugadorGuardado).isPresent();
        jugadorGuardado.ifPresent(j -> {
            assertThat(j.getNombre()).isEqualTo("Luis");
            j.setNombre("Sandra");
            jugadorRepository.save(j);
        });

        Optional<JugadorEntityJpa> jugadorActualizado = jugadorRepository.findById(1L);
        assertThat(jugadorActualizado).isPresent();
        jugadorActualizado.ifPresent(j -> assertThat(j.getNombre()).isEqualTo("Sandra"));
    }

    @Test
    @DirtiesContext
    @Order(5)
    public void testEliminarJugador() {
        JugadorEntityJpa jugador = new JugadorEntityJpa(1L, "Luis", 0);
        jugadorRepository.save(jugador);

        Optional<JugadorEntityJpa> jugadorGuardado = jugadorRepository.findById(1L);
        assertThat(jugadorGuardado).isPresent();
        jugadorGuardado.ifPresent(j -> jugadorRepository.delete(j));

        Optional<JugadorEntityJpa> jugadorEliminado = jugadorRepository.findById(1L);
        assertThat(jugadorEliminado).isEmpty();
    }

}