package Luis.JuegoDados.services;

import Luis.JuegoDados.dto.PartidaDto;
import Luis.JuegoDados.entity.JugadorEntity;
import Luis.JuegoDados.entity.PartidaEntity;
import Luis.JuegoDados.excepciones.EmptyPlayersListException;
import Luis.JuegoDados.excepciones.PlayerNotFoundException;
import Luis.JuegoDados.helper.FromEntityToDtoConverter;
import Luis.JuegoDados.repository.JugadorRepository;
import Luis.JuegoDados.repository.PartidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PartidaServiceImplTest {
    @Mock
    private PartidaRepository partidaRepository;
    @Mock
    private JugadorRepository jugadorRepository;
    @Mock
    private FromEntityToDtoConverter converter;
    @InjectMocks
    private PartidaServiceImpl partidaService;
    private JugadorEntity jugador1;
    private PartidaEntity partida1;
    private PartidaDto partidaDto1;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jugador1 = new JugadorEntity(1,"Juanito",0);
        partida1 = new PartidaEntity(1L, LocalDate.now(),0,0,jugador1);
        partidaDto1 = new PartidaDto(1L,LocalDate.now(),"Ganaste :D");
    }

    @Test
    void playNewGameTest(){
        when(jugadorRepository.findById(1L)).thenReturn(Optional.ofNullable(jugador1));
        when(partidaRepository.save(any(PartidaEntity.class))).thenReturn(partida1);
        when(jugadorRepository.save(any(JugadorEntity.class))).thenReturn(jugador1);
        when(converter.convertirPartidaEntityADto(any(PartidaEntity.class))).thenReturn(partidaDto1);

        PartidaDto result = partidaService.playNewGame(1L);
        assertNotNull(result);
        assertEquals(partidaDto1.getId(), result.getId());
        assertEquals(partidaDto1.getFecha(), result.getFecha());
        assertEquals(partidaDto1.getMensaje(), result.getMensaje());
    }

    @Test
    void deleteAllGamesOfPLayerTest(){
        List<PartidaEntity> myList = new ArrayList<>();
        myList.add(partida1);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.ofNullable(jugador1));
        when(partidaRepository.findByJugador(jugador1)).thenReturn(myList);
        when(jugadorRepository.save(eq(jugador1))).thenReturn(new JugadorEntity(1, "Juanito", 50));

        partidaService.deleteAllGamesOfPLayer(1L);

        verify(partidaRepository, times(2)).findByJugador(jugador1);
        verify(partidaRepository, times(1)).deleteAll(myList);
        verify(jugadorRepository, times(2)).save(eq(jugador1));

        JugadorEntity jugadorDespues = jugadorRepository.findById(1L).orElse(null);
        assertNotNull(jugadorDespues);
        assertEquals(0, jugadorDespues.getPorcentajeExito());
    }

    @Test
    void deleteAllGamesOfPLayer_PlayerNotFoundTest() {
        when(jugadorRepository.findById(1L)).thenReturn(Optional.empty());

        verify(partidaRepository, never()).findByJugador(any());
        verify(partidaRepository, never()).deleteAll(anyList());
        verify(jugadorRepository, never()).save(any(JugadorEntity.class));

        assertThrows(PlayerNotFoundException.class, () -> partidaService.deleteAllGamesOfPLayer(1L));
    }


    @Test
    void findGamesOfAPLayerByIdTest(){
        List<PartidaEntity> myGames = new ArrayList<>();
        myGames.add(partida1);

        when(jugadorRepository.findById(1L)).thenReturn(Optional.ofNullable(jugador1));
        when(partidaRepository.findByJugador(any(JugadorEntity.class))).thenReturn(myGames);
        when(converter.convertirPartidaEntityADto(any(PartidaEntity.class)))
                .thenAnswer(invocation -> {
                    invocation.getArgument(0);
                    return partidaDto1;
                });

        List<PartidaDto> result = partidaService.findGamesOfAPLayerById(1L);

        assertNotNull(result);
        assertEquals(1, result.size());

        PartidaDto partidaDto = result.get(0);
        assertEquals(partida1.getId(), partidaDto.getId());
        assertEquals(partida1.getFecha(), partidaDto.getFecha());

    }

}