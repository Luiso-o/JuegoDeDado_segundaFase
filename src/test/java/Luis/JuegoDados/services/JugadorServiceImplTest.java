package Luis.JuegoDados.services;

import Luis.JuegoDados.dto.JugadorDto;
import Luis.JuegoDados.dto.PromedioJugadorDto;
import Luis.JuegoDados.entity.JugadorEntity;
import Luis.JuegoDados.excepciones.EmptyPlayersListException;
import Luis.JuegoDados.excepciones.PlayerNotFoundException;
import Luis.JuegoDados.excepciones.PlayerNotSavedException;
import Luis.JuegoDados.helper.FromEntityToDtoConverter;
import Luis.JuegoDados.repository.JugadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class JugadorServiceImplTest {

    @Mock
    private JugadorRepository jugadorRepository;
    @Mock
    private FromEntityToDtoConverter converter;
    @InjectMocks
    private JugadorServiceImpl jugadorService;

    private JugadorEntity jugador1;
    private JugadorEntity jugador2;
    private JugadorEntity jugador3;
    private JugadorDto jugadorDto1;
    private JugadorDto jugadorDto2;
    private JugadorDto jugadorDto3;
    private PromedioJugadorDto promedio1;
    private PromedioJugadorDto promedio2;
    private PromedioJugadorDto promedio3;
    private List<JugadorEntity> myList;
    private List<PromedioJugadorDto> myAverageList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jugador1 = new JugadorEntity(1,"Juanito",0);
        jugador2 = new JugadorEntity(2,"Margarita",0);
        jugador3 = new JugadorEntity(3,"Pablito",0);

        jugadorDto1 = new JugadorDto(1L,"Juanito",0);
        jugadorDto2 = new JugadorDto(2L,"Margarita",0);
        jugadorDto3 = new JugadorDto(3L,"Pablito",0);

        promedio1 = new PromedioJugadorDto("Juanito","Promedio de victorias " + 0 + " %");
        promedio2 = new PromedioJugadorDto("Margarita","Promedio de victorias " + 0 + " %");
        promedio3 = new PromedioJugadorDto("Pablito","Promedio de victorias " + 0 + " %");

        myList = new ArrayList<>();
        myList.add(jugador1);
        myList.add(jugador2);
        myList.add(jugador3);

        myAverageList = new ArrayList<>();
        myAverageList.add(promedio1);
        myAverageList.add(promedio2);
        myAverageList.add(promedio3);

    }

    @Test
    void createNewPLayerTest() throws PlayerNotSavedException {
        when(jugadorRepository.save(any(JugadorEntity.class))).thenReturn(jugador1);
        when(converter.convertirJugadorEntityADto(any(JugadorEntity.class))).thenReturn(jugadorDto1);

        JugadorDto result = jugadorService.createNewPLayer("Juanito");
        assertEquals(jugadorDto1, result);
    }

    @Test
    void createNewPLayerErrorTest() {
        doThrow(new RuntimeException("Error simulado al guardar en el repositorio"))
                .when(jugadorRepository).save(any(JugadorEntity.class));

        assertThrows(PlayerNotSavedException.class, () -> jugadorService.createNewPLayer("Margarita"));
    }

    @Test
    void updatePLayerTest(){
        when(jugadorRepository.findById(1L)).thenReturn(Optional.ofNullable(jugador2));
        when(jugadorRepository.save(any(JugadorEntity.class))).thenReturn(jugador1);
        when(converter.convertirJugadorEntityADto(any(JugadorEntity.class))).thenReturn(jugadorDto2);

        JugadorDto result = jugadorService.updatePLayer(1L,"Margarita");
        assertNotNull(result);
        assertEquals(jugadorDto2.getId(),result.getId());
        assertEquals(jugadorDto2.getNombre(),result.getNombre());
        assertEquals(jugadorDto2.getPorcentajeExito(),result.getPorcentajeExito());
    }

    @Test
    void findPLayerByIdNotFoundTest(){
        when(jugadorRepository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(PlayerNotFoundException.class, () -> jugadorService.findPLayerById(4L));
        verify(jugadorRepository, times(1)).findById(4L);
    }

    @Test
    void findAllPLayersTest(){
        when(jugadorRepository.findAll()).thenReturn(myList);
        when(converter.convertirJugadorEntityAPromedioJugadorDto(any(JugadorEntity.class)))
                .thenAnswer(invocation -> {
                    JugadorEntity jugadorEntity = invocation.getArgument(0);
                    return new PromedioJugadorDto(
                            jugadorEntity.getNombreJugador(),
                            "Promedio de victorias " + jugadorEntity.getPorcentajeExito() + " %"
                    );
                });

        List<PromedioJugadorDto> result = jugadorService.findAllPLayers();

        assertEquals(myAverageList.size(), result.size());
        for (int i = 0; i < myAverageList.size(); i++) {
            assertEquals(myAverageList.get(i), result.get(i));
        }

    }

    @Test
    void NotFoundPLayersTest(){
        doThrow(new RuntimeException("Error simulado al recuperar la lista de jugadores"))
                .when(jugadorRepository).findAll();

        assertThrows(EmptyPlayersListException.class, () -> {
            jugadorService.findAllPLayers();
            verify(jugadorRepository, times(1)).findAll();
        });

    }

    @Test
    void  averagePlayerWinsTest(){
        myList.get(0).setPorcentajeExito(10);
        myList.get(1).setPorcentajeExito(10);
        myList.get(2).setPorcentajeExito(10);
        when(jugadorRepository.findAll()).thenReturn(myList);
        int result = jugadorService.averagePlayerWins();
        assertEquals(10,result);
    }

    @Test
    void  averagePlayerWinsTestWithEmptyList(){
        when(jugadorRepository.findAll()).thenReturn(new ArrayList<>());
        int result = jugadorService.averagePlayerWins();
        assertEquals(0,result);
    }

    @Test
    void averagePlayerWinsTestError(){
        doThrow(new RuntimeException("Error simulado al recuperar la lista de jugadores"))
                .when(jugadorRepository).findAll();

        assertThrows(EmptyPlayersListException.class, () -> {
            jugadorService.averagePlayerWins();
            verify(jugadorRepository, times(1)).findAll();
        });
    }

    @Test
    void lowestScoresTest(){
        promedio3.setPorcentajeExito("Promedio de victorias " + 3 + " %");
        myList.get(0).setPorcentajeExito(10);
        myList.get(1).setPorcentajeExito(5);
        myList.get(2).setPorcentajeExito(3);
        when(jugadorRepository.findAll()).thenReturn(myList);
        when(converter.convertirJugadorEntityAPromedioJugadorDto(any(JugadorEntity.class)))
                .thenAnswer(invocation -> {
                    JugadorEntity jugadorEntity = invocation.getArgument(0);
                    return new PromedioJugadorDto(
                            jugadorEntity.getNombreJugador(),
                            "Promedio de victorias " + jugadorEntity.getPorcentajeExito() + " %"
                    );
                });

        List<PromedioJugadorDto> averageList = jugadorService.lowestScores();
        assertNotNull(averageList);
        assertEquals(1, averageList.size());
        assertEquals(promedio3, averageList.get(0));
    }

    @Test
    void lowestScoresTestError(){
        when(jugadorRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EmptyPlayersListException.class, () -> {
            jugadorService.lowestScores();
            verify(jugadorRepository, times(1)).findAll();
        });
    }

    @Test
    void bestScoresTest(){
        promedio1.setPorcentajeExito("Promedio de victorias " + 10 + " %");
        myList.get(0).setPorcentajeExito(10);
        myList.get(1).setPorcentajeExito(5);
        myList.get(2).setPorcentajeExito(3);
        when(jugadorRepository.findAll()).thenReturn(myList);
        when(converter.convertirJugadorEntityAPromedioJugadorDto(any(JugadorEntity.class)))
                .thenAnswer(invocation -> {
                    JugadorEntity jugadorEntity = invocation.getArgument(0);
                    return new PromedioJugadorDto(
                            jugadorEntity.getNombreJugador(),
                            "Promedio de victorias " + jugadorEntity.getPorcentajeExito() + " %"
                    );
                });

        List<PromedioJugadorDto> averageList = jugadorService.bestScores();
        assertNotNull(averageList);
        assertEquals(1, averageList.size());
        assertEquals(promedio1, averageList.get(0));
    }

    @Test
    void bestScoresTestError(){
        when(jugadorRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EmptyPlayersListException.class, () -> {
            jugadorService.bestScores();
            verify(jugadorRepository, times(1)).findAll();
        });
    }
}