package Luis.JuegoDados.model.repository;

import Luis.JuegoDados.entity.JugadorEntity;
import Luis.JuegoDados.entity.PartidaEntity;
import Luis.JuegoDados.repository.JugadorRepository;
import Luis.JuegoDados.repository.PartidaRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/*Para estas pruebas estoy usando H2 como base de datos
 * así estas pruebas no alteran la base de datos de producción
 */
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartidaRepositoryJpaTest {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Test
    @Order(1)
    @DirtiesContext
    public void testGuardarPartida() {
        JugadorEntity jugador = new JugadorEntity(1L, "Luis", 0);
        jugadorRepository.save(jugador);

        PartidaEntity partida = PartidaEntity.builder()
                .fecha(LocalDate.now())
                .victorias(1)
                .derrotas(0)
                .jugador(jugador)
                .build();

        partidaRepository.save(partida);

        assertNotNull(partida);
        assertThat(partida.getFecha()).isNotNull();
        assertThat(partida.getVictorias()).isEqualTo(1);
        assertThat(partida.getDerrotas()).isEqualTo(0);
        assertThat(partida.getJugador()).isEqualTo(jugador);
    }

    @Test
    @Order(2)
    @DirtiesContext
    public void testFindByJugador() {
        JugadorEntity jugador = new JugadorEntity(1L, "Luis", 0);
        jugadorRepository.save(jugador);

        PartidaEntity partida1 = PartidaEntity.builder()
                .fecha(LocalDate.now())
                .victorias(2)
                .derrotas(1)
                .jugador(jugador)
                .build();
        partidaRepository.save(partida1);

        PartidaEntity partida2 = PartidaEntity.builder()
                .fecha(LocalDate.now())
                .victorias(3)
                .derrotas(2)
                .jugador(jugador)
                .build();
        partidaRepository.save(partida2);

        List<PartidaEntity> partidasByJugador = partidaRepository.findByJugador(jugador);

        assertThat(partidasByJugador.size()).isEqualTo(2);

        PartidaEntity partidaRecuperada1 = partidasByJugador.get(0);
        assertThat(partidaRecuperada1.getFecha()).isEqualTo(partida1.getFecha());
        assertThat(partidaRecuperada1.getVictorias()).isEqualTo(partida1.getVictorias());
        assertThat(partidaRecuperada1.getDerrotas()).isEqualTo(partida1.getDerrotas());

        PartidaEntity partidaRecuperada2 = partidasByJugador.get(1);
        assertThat(partidaRecuperada2.getFecha()).isEqualTo(partida2.getFecha());
        assertThat(partidaRecuperada2.getVictorias()).isEqualTo(partida2.getVictorias());
        assertThat(partidaRecuperada2.getDerrotas()).isEqualTo(partida2.getDerrotas());
    }

    @Test
    @Order(3)
    @DirtiesContext
    public void testEliminarPartida() {
        JugadorEntity jugador = new JugadorEntity(1L, "Luis", 0);
        jugadorRepository.save(jugador);

        PartidaEntity partida = PartidaEntity.builder()
                .fecha(LocalDate.now())
                .victorias(2)
                .derrotas(1)
                .jugador(jugador)
                .build();
        partidaRepository.save(partida);

        List<PartidaEntity> partidasBeforeDeletion = partidaRepository.findByJugador(jugador);
        assertThat(partidasBeforeDeletion.size()).isEqualTo(1);

        partidaRepository.delete(partida);

        List<PartidaEntity> partidasAfterDeletion = partidaRepository.findByJugador(jugador);
        assertThat(partidasAfterDeletion.size()).isEqualTo(0);
    }

}