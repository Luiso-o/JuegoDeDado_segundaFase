package Luis.JuegoDados.services;

import Luis.JuegoDados.excepciones.GamesNotFoundInThisPlayerException;
import Luis.JuegoDados.excepciones.ItemsNotFoundException;
import Luis.JuegoDados.dto.PartidaDtoJpa;
import Luis.JuegoDados.entity.JugadorEntityJpa;
import Luis.JuegoDados.entity.PartidaEntityJpa;
import Luis.JuegoDados.repository.JugadorRepositoryJpa;
import Luis.JuegoDados.repository.PartidaRepositoryJpa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Service
public class PartidaServiceJpa {

    private PartidaRepositoryJpa partidaRepositoryJpa;

    private JugadorRepositoryJpa jugadorRepositoryJpa;


    /**
     * Crea una nueva partida para un jugador.
     *
     * @param jugador Jugador para el cual se creará la partida.
     * @return Objeto PartidaDtoJpa que representa la partida creada.
     */
    public PartidaDtoJpa crearPartida(JugadorEntityJpa jugador){
        int lanzada = tirarDados();
        PartidaEntityJpa nuevaPartida = PartidaEntityJpa.builder()
                .fecha(LocalDate.now())
                .victorias(lanzada <= 7 ? 1 : 0)
                .derrotas(lanzada > 7 ? 1 : 0)
                .jugador(jugador)
                .build();
        partidaRepositoryJpa.save(nuevaPartida);
        return pasarEntityADto(nuevaPartida);
    }

    /**
     * Elimina todas las partidas asociadas a un jugador.
     *
     * @param jugador El jugador del cual se desean eliminar las partidas.
     * @throws RuntimeException Si el jugador no tiene partidas que eliminar.
     */
    public void eliminarPartidasDeJugador(JugadorEntityJpa jugador){
        List<PartidaEntityJpa> misPartidas = partidaRepositoryJpa.findByJugador(jugador);
        if (misPartidas.isEmpty()) {
            throw new ItemsNotFoundException();
        }
        misPartidas.forEach(partida -> partidaRepositoryJpa.delete(partida));
        jugadorRepositoryJpa.save(jugador);
    }

    /**
     * Encuentra y devuelve las partidas de un jugador en forma de DTO.
     *
     * @param jugador El jugador del cual se desean obtener las partidas.
     * @return Una lista de objetos PartidaDtoJpa que representan las partidas del jugador.
     * @throws NotFoundException Si no se encuentran partidas para el jugador.
     */
    public List<PartidaDtoJpa> encuentraPartidasJugador(JugadorEntityJpa jugador){
        List<PartidaEntityJpa> misPartidas = partidaRepositoryJpa.findByJugador(jugador);
        if(misPartidas.isEmpty()){
            throw new GamesNotFoundInThisPlayerException();
        }

        return misPartidas.stream()
                .map(this::pasarEntityADto)
                .collect(Collectors.toList());

    }

    //Metodos privados ---------------------------------------------------------------->>
    /**
     * Convierte una entidad PartidaEntityJpa en un DTO PartidaDtoJpa.
     *
     * @param partidaEntity Entidad PartidaEntityJpa a convertir.
     * @return Objeto PartidaDtoJpa resultante.
     */
    private PartidaDtoJpa pasarEntityADto(PartidaEntityJpa partidaEntity) {
        return PartidaDtoJpa.builder()
                .id(partidaEntity.getId())
                .fecha(partidaEntity.getFecha())
                .mensaje(partidaEntity.getVictorias() == 1 ? "Ganaste :D" : "Perdiste :V")
                .build();
    }

    /**
     * Simula el lanzamiento de dos dados y devuelve un número aleatorio entre 2 y 12 (ambos inclusive).
     *
     * @return Un número aleatorio entre 2 y 12.
     */
    private int tirarDados(){
        return (int)Math.floor(Math.random() * 11) + 2;
    }

}
