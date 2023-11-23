package Luis.JuegoDados.services;

import Luis.JuegoDados.excepciones.GamesNotFoundInThisPlayerException;
import Luis.JuegoDados.excepciones.ItemsNotFoundException;
import Luis.JuegoDados.dto.PartidaDtoJpa;
import Luis.JuegoDados.entity.JugadorEntityJpa;
import Luis.JuegoDados.entity.PartidaEntityJpa;
import Luis.JuegoDados.repository.PartidaRepositoryJpa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidaServiceJpaTest {

    @InjectMocks
    private PartidaServiceJpa partidaServiceJpa;
    @Mock
    private PartidaRepositoryJpa partidaRepositoryJpa;

    @Test
    @DisplayName("Crear partida exitosamente")
    void testCrearPartida() {
        JugadorEntityJpa jugador = new JugadorEntityJpa(1L, "Jugador1", 50);

        // Simular el comportamiento del repositorio
        when(partidaRepositoryJpa.save(any(PartidaEntityJpa.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Llamada al método a probar
        PartidaDtoJpa partidaDto = partidaServiceJpa.crearPartida(jugador);

        // Verificaciones
        assertNotNull(partidaDto);
        assertEquals(LocalDate.now(), partidaDto.getFecha());
        assertTrue(partidaDto.getMensaje().equals("Ganaste :D") || partidaDto.getMensaje().equals("Perdiste :V"));

        // Verificar que se llamó al método save en el repositorio
        verify(partidaRepositoryJpa).save(any(PartidaEntityJpa.class));
    }

    @Test
    @DisplayName("Test para tirarDados (private Method)")
    void testTirarDados_MetodoPrivado() throws Exception {
        // Llamada al método privado usando reflexión
        Method method = PartidaServiceJpa.class.getDeclaredMethod("tirarDados");
        method.setAccessible(true);
        int resultado = (int) method.invoke(partidaServiceJpa);

        // Verificaciones
        assertTrue(resultado >= 2 && resultado <= 12);
    }

    @Test
    @DisplayName("Test para convertir PartidaEntity a PartidaDto (private Method)")
    public void testPasarEntityADto_MetodoPrivado() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Crear una instancia de PartidaEntityJpa para simular entrada
        JugadorEntityJpa jugadorEntityJpa = new JugadorEntityJpa(1L,"Juan",36);
        PartidaEntityJpa partidaEntity = new PartidaEntityJpa(1L,LocalDate.now(),1,0,jugadorEntityJpa);

        // Llamar al método privado usando reflexión
        Method method = PartidaServiceJpa.class.getDeclaredMethod("pasarEntityADto", PartidaEntityJpa.class);
        method.setAccessible(true);

        PartidaDtoJpa result = (PartidaDtoJpa) method.invoke(partidaServiceJpa, partidaEntity);

        // Verificar el resultado
        assertEquals(1L, result.getId());
        assertEquals("Ganaste :D", result.getMensaje());
    }

    @Test
    @DisplayName("Test para eliminarPartidasDeJugador cuando no hay partidas")
    public void testEliminarPartidasDeJugadorSinPartidas() {
        // Crear un jugador sin partidas
        JugadorEntityJpa jugadorEntity = new JugadorEntityJpa();
        // Verificar si la excepción PartidasNoEncontradasException se lanza
        assertThrows(ItemsNotFoundException.class, () -> partidaServiceJpa.eliminarPartidasDeJugador(jugadorEntity));
    }

    @Test
    void testEncuentraPartidasJugador_NoPartidas() {
        // Mock del repositorio de partidas
        PartidaRepositoryJpa partidaRepositoryJpa = mock(PartidaRepositoryJpa.class);

        // Crear una instancia del servicio con el repositorio mock
        PartidaServiceJpa partidaServiceJpa = new PartidaServiceJpa(partidaRepositoryJpa, null);

        // Configurar el repositorio para devolver una lista vacía de partidas
        when(partidaRepositoryJpa.findByJugador(any())).thenReturn(Collections.emptyList());

        // Verificar que se lance la excepción GamesNotFoundInThisPlayerException
        assertThrows(GamesNotFoundInThisPlayerException.class, () -> partidaServiceJpa.encuentraPartidasJugador(new JugadorEntityJpa()));
    }

    @Test
    void testEncuentraPartidasJugador_ConPartidas() {
        // Mock del repositorio de partidas
        PartidaRepositoryJpa partidaRepositoryJpa = mock(PartidaRepositoryJpa.class);

        // Crear una partida simulada
        PartidaEntityJpa partidaEntity = new PartidaEntityJpa();
        partidaEntity.setId(1L);

        // Configurar el repositorio para devolver la lista de partidas simulada
        when(partidaRepositoryJpa.findByJugador(any())).thenReturn(Collections.singletonList(partidaEntity));

        // Crear una instancia del servicio con el repositorio mock
        PartidaServiceJpa partidaServiceJpa = new PartidaServiceJpa(partidaRepositoryJpa, null);

        // Llamar al método y verificar el resultado
        List<PartidaDtoJpa> partidas = partidaServiceJpa.encuentraPartidasJugador(new JugadorEntityJpa());
        assertFalse(partidas.isEmpty());
        assertEquals(partidaEntity.getId(), partidas.get(0).getId());
    }

}