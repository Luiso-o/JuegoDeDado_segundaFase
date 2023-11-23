package Luis.JuegoDados.model.services;

import Luis.JuegoDados.excepciones.EmptyPlayersListException;
import Luis.JuegoDados.excepciones.PlayerNotFoundException;
import Luis.JuegoDados.model.dto.JugadorDtoJpa;
import Luis.JuegoDados.model.entity.JugadorEntityJpa;
import Luis.JuegoDados.model.repository.JugadorRepositoryJpa;
import Luis.JuegoDados.model.repository.PartidaRepositoryJpa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

import java.lang.reflect.Method;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JugadorServiceJpaTest {

    @InjectMocks
    private JugadorServiceJpa jugadorServiceJpa;
    @Mock
    private JugadorRepositoryJpa jugadorRepositoryJpa;
    @Mock
    private PartidaRepositoryJpa partidaRepositoryJpa;

    @Test
    @DisplayName("Test para el metodo crearJugador, esperamos obtener un objeto JugadorDtoJpa")
    void crearJugador() {
        // Configurar el comportamiento del mock para que retorne una entidad simulada al guardar
        JugadorEntityJpa jugadorCreadoSimulado = new JugadorEntityJpa();
        when(jugadorRepositoryJpa.save(any(JugadorEntityJpa.class))).thenReturn(jugadorCreadoSimulado);

        // Llamar al método bajo prueba
        JugadorDtoJpa jugadorDto = jugadorServiceJpa.crearJugador("Sara");

        // Realizar aserciones para verificar el resultado
        assertNotNull(jugadorDto);
        // Realizar más aserciones según lo esperado

        // Verificar que los métodos del mock se llamaron según lo esperado
        verify(jugadorRepositoryJpa, times(1)).save(any(JugadorEntityJpa.class));
    }

    @Test
    @DisplayName("Test para filtraNombre (private Method)")
    void testFiltraNombre_MetodoPrivado() throws Exception {
        // Datos de prueba
        String nombreOriginal1 = "Nombre 123";
        String nombreOriginal2 = "";

        // Llamada al método privado usando reflexión
        Method method = JugadorServiceJpa.class.getDeclaredMethod("filtraNombre", String.class);
        method.setAccessible(true);
        String resultado1 = (String) method.invoke(jugadorServiceJpa, nombreOriginal1);
        String resultado2 = (String) method.invoke(jugadorServiceJpa, nombreOriginal2);

        // El nombre filtrado debe ser "Nombre"
        assertEquals("Nombre", resultado1);
        assertEquals("Anónimo", resultado2);
    }

    @Test
    @DisplayName("Test para pasarEntidadADto (private Method)")
    void testPasarEntidadADto_MetodoPrivado() throws Exception {
        // Datos de prueba
        Long jugadorId = 1L;
        String nombre = "Nombre";
        int porcentajeExito = 75;

        JugadorEntityJpa jugadorEntity = new JugadorEntityJpa();
        jugadorEntity.setId(jugadorId);
        jugadorEntity.setNombre(nombre);
        jugadorEntity.setPorcentajeExito(porcentajeExito);

        // Llamada al método privado usando reflexión
        Method method = JugadorServiceJpa.class.getDeclaredMethod("pasarEntidadADto", JugadorEntityJpa.class);
        method.setAccessible(true);
        JugadorDtoJpa resultado = (JugadorDtoJpa) method.invoke(jugadorServiceJpa, jugadorEntity);

        // Verificaciones
        assertEquals(jugadorId, resultado.getId());
        assertEquals(nombre, resultado.getNombre());
        assertEquals(porcentajeExito, resultado.getPorcentajeExito());
    }

    @Test
    @DisplayName("Test para buscarJugadorPorId cuando el jugador existe")
    void buscarJugadorPorId_CuandoJugadorExiste() {
        Long jugadorId = 1L;
        JugadorEntityJpa jugadorMock = new JugadorEntityJpa();
        jugadorMock.setId(jugadorId);

        when(jugadorRepositoryJpa.findById(jugadorId)).thenReturn(Optional.of(jugadorMock));

        JugadorEntityJpa resultado = jugadorServiceJpa.buscarJugadorPorId(jugadorId);

        assertNotNull(resultado);
        assertEquals(jugadorId, resultado.getId());

        verify(jugadorRepositoryJpa, times(1)).findById(jugadorId);
    }

    @Test
    @DisplayName("Test para buscarJugadorPorId cuando el jugador no existe")
    void buscarJugadorPorId_CuandoJugadorNoExiste() {
        Long jugadorId = 1L;

        when(jugadorRepositoryJpa.findById(jugadorId)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> jugadorServiceJpa.buscarJugadorPorId(jugadorId));

        verify(jugadorRepositoryJpa, times(1)).findById(jugadorId);
    }

    @Test
    @DisplayName("Test para actualizarNombreJugador")
    void actualizarNombreJugador() {
        // Datos de prueba
        long jugadorId = 1L;
        String nuevoNombre = "NuevoNombre";

        // Mock del jugador y configuración del repositorio
        JugadorEntityJpa jugadorMock = new JugadorEntityJpa();
        jugadorMock.setId(jugadorId);
        when(jugadorRepositoryJpa.save(any(JugadorEntityJpa.class))).thenReturn(jugadorMock);

        // Llamada al método bajo prueba
        JugadorDtoJpa resultado = jugadorServiceJpa.actualizarNombreJugador(jugadorMock, nuevoNombre);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(nuevoNombre, jugadorMock.getNombre());

        verify(jugadorRepositoryJpa, times(1)).save(jugadorMock);
    }

    @Test
    @DisplayName("Test para actualizarPorcentajeExitoJugador")
    void actualizarPorcentajeExitoJugador() {
        // Datos de prueba
        JugadorEntityJpa jugadorMock = new JugadorEntityJpa();

        // Llamada al método bajo prueba
        jugadorServiceJpa.actualizarPorcentajeExitoJugador(jugadorMock);

        // Verificar que el método save del repositorio se llamó una vez con el jugadorMock
        verify(jugadorRepositoryJpa, times(1)).save(jugadorMock);
    }

    @Test
    @DisplayName("Test para calculaPorcentajeExitoDeUnJugador (Private Method)")
    void testCalculaPorcentajeExito_MetodoPrivado() throws Exception {
            // Datos de prueba
            JugadorEntityJpa jugadorMock = new JugadorEntityJpa();

            // Simulamos que no hay partidas para el jugador
            when(partidaRepositoryJpa.findByJugador(jugadorMock)).thenReturn(List.of());

            // Simulamos la llamada al método privado usando reflexión
            Method method = JugadorServiceJpa.class.getDeclaredMethod("calculaPorcentajeExitoDeUnJugador", JugadorEntityJpa.class);
            method.setAccessible(true);
            int resultado = (int) method.invoke(jugadorServiceJpa, jugadorMock);

            // Verificaciones
            // El porcentaje de éxito debe ser 0 ya que no hay partidas
            assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Test para listaJugadores cuando la lista no está vacía")
    void testListaJugadores_NoVacia() {
        // Datos de prueba
        List<JugadorEntityJpa> jugadoresMock = Arrays.asList(
                new JugadorEntityJpa(1L, "Jugador1", 80),
                new JugadorEntityJpa(2L, "Jugador2", 65)
        );

        // Configurar el comportamiento del mock
        when(jugadorRepositoryJpa.findAll()).thenReturn(jugadoresMock);

        // Llamar al método bajo prueba
        List<JugadorDtoJpa> resultado = jugadorServiceJpa.listaJugadores();

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        // Verificar que el método del mock se llamó una vez
        verify(jugadorRepositoryJpa, times(1)).findAll();
    }

    @Test
    @DisplayName("Test para listaJugadores cuando la lista está vacía")
    void testListaJugadores_Vacia() {
        // Configurar el comportamiento del mock
        when(jugadorRepositoryJpa.findAll()).thenReturn(Collections.emptyList());

        // Llamar al método bajo prueba y verificar la excepción
        assertThrows(EmptyPlayersListException.class, () -> jugadorServiceJpa.listaJugadores());

        // Verificar que el método del mock se llamó una vez
        verify(jugadorRepositoryJpa, times(1)).findAll();
    }

    @Test
    @DisplayName("Test para calculaPorcentajeVictoriasGlobales")
    void calculaPorcentajeVictoriasGlobales() {
        // Crear jugadores de prueba con diferentes porcentajes de éxito
        JugadorEntityJpa jugador1 = new JugadorEntityJpa();
        jugador1.setPorcentajeExito(50);

        JugadorEntityJpa jugador2 = new JugadorEntityJpa();
        jugador2.setPorcentajeExito(75);

        JugadorEntityJpa jugador3 = new JugadorEntityJpa();
        jugador3.setPorcentajeExito(0);

        // Simular que el repositorio retorna los jugadores de prueba
        when(jugadorRepositoryJpa.findAll()).thenReturn(Arrays.asList(jugador1, jugador2, jugador3));

        // Llamada al método bajo prueba
        int porcentajeGlobal = jugadorServiceJpa.calculaPorcentajeVictoriasGlobales();

        // El porcentaje global debe ser el promedio de los porcentajes de éxito de los jugadores
        assertEquals(41, porcentajeGlobal); // (50 + 75 + 0) / 3 = 41.66, redondeado a 41
    }

    @Test
    @DisplayName("Test para peoresJugadores")
    void peoresJugadores() {
        // Crear jugadores de prueba con diferentes porcentajes de éxito
        JugadorEntityJpa jugador1 = new JugadorEntityJpa();
        jugador1.setPorcentajeExito(50);

        JugadorEntityJpa jugador2 = new JugadorEntityJpa();
        jugador2.setPorcentajeExito(25);

        JugadorEntityJpa jugador3 = new JugadorEntityJpa();
        jugador3.setPorcentajeExito(75);

        // Simular que el repositorio retorna los jugadores de prueba
        when(jugadorRepositoryJpa.findAll()).thenReturn(Arrays.asList(jugador1, jugador2, jugador3));

        // Llamada al método bajo prueba
        List<JugadorDtoJpa> peoresJugadores = jugadorServiceJpa.peoresJugadores();

        // Verificar el resultado esperado
        assertEquals(1, peoresJugadores.size()); // Debería haber solo un peor jugador (jugador2)
        assertEquals(25, peoresJugadores.get(0).getPorcentajeExito()); // Porcentaje de éxito del peor jugador
    }

    @Test
    @DisplayName("Test para mejoresJugadores")
    void mejoresJugadores() throws NotFoundException {
        // Crear jugadores de prueba con diferentes porcentajes de éxito
        JugadorEntityJpa jugador1 = new JugadorEntityJpa();
        jugador1.setPorcentajeExito(70);

        JugadorEntityJpa jugador2 = new JugadorEntityJpa();
        jugador2.setPorcentajeExito(90);

        JugadorEntityJpa jugador3 = new JugadorEntityJpa();
        jugador3.setPorcentajeExito(80);

        // Simular que el repositorio retorna los jugadores de prueba
        when(jugadorRepositoryJpa.findAll()).thenReturn(Arrays.asList(jugador1, jugador2, jugador3));

        // Llamada al método bajo prueba
        List<JugadorDtoJpa> mejoresJugadores = jugadorServiceJpa.mejoresJugadores();

        // Verificar el resultado esperado
        assertEquals(1, mejoresJugadores.size()); // Debería haber solo un mejor jugador (jugador2)
        assertEquals(90, mejoresJugadores.get(0).getPorcentajeExito()); // Porcentaje de éxito del mejor jugador
    }

}