package Luis.JuegoDados.services;

import Luis.JuegoDados.dto.PartidaDto;
import Luis.JuegoDados.entity.JugadorEntity;
import Luis.JuegoDados.entity.PartidaEntity;
import Luis.JuegoDados.excepciones.PlayerNotFoundException;
import Luis.JuegoDados.helper.FromEntityToDtoConverter;
import Luis.JuegoDados.repository.JugadorRepository;
import Luis.JuegoDados.repository.PartidaRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Service
public class PartidaServiceImpl implements PartidaService{

    private static final Logger log = LoggerFactory.getLogger(PartidaServiceImpl.class);

    @Autowired
    private PartidaRepository partidaRepository;
    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private FromEntityToDtoConverter converter;

    @Override
    public PartidaDto playNewGame(Long idPlayer) {
       JugadorEntity gaming = findPLayer(idPlayer);
        log.info("Jugador encontrado exitosamente: " + gaming);

        int lanzada = tirarDados();
        log.info("Resultado de la tirada: " + lanzada);

        PartidaEntity newGame = PartidaEntity.builder()
                .fecha(LocalDate.now())
                .victorias(lanzada <= 7 ? 1 : 0)
                .derrotas(lanzada > 7 ? 1 : 0)
                .jugador(gaming)
                .build();

        partidaRepository.save(newGame);
        log.info("Partida creada correctamente: " + newGame);

        guardarPorcentajeDelJugador(gaming);

        return converter.convertirPartidaEntityADto(newGame);
    }

    @Override
    public void deleteAllGamesOfPLayer(Long idPlayer) {
        JugadorEntity player = findPLayer(idPlayer);
        log.info("Jugador encontrado exitosamente: " + player);

        List<PartidaEntity> myGames = partidaRepository.findByJugador(player);

        partidaRepository.deleteAll(myGames);
        log.info("Partidas eliminadas exitosamente");

        jugadorRepository.save(player);
        log.info("Jugador actualizado exitosamente " + player);

        guardarPorcentajeDelJugador(player);
    }

    @Override
    public List<PartidaDto> findGamesOfAPLayerById(Long idPlayer) {
        JugadorEntity player = findPLayer(idPlayer);
        log.info("Jugador encontrado exitosamente: " + player);

        List<PartidaEntity> myGames = partidaRepository.findByJugador(player);

        log.info("Lista de partidas encontradas con éxito ");
        return myGames.stream().map(myGame -> converter.convertirPartidaEntityADto(myGame))
                .collect(Collectors.toList());
    }

    /*
    public List<PartidaDto> encuentraPartidasJugador(JugadorEntity jugador){
        List<PartidaEntity> misPartidas = partidaRepositoryJpa.findByJugador(jugador);
        if(misPartidas.isEmpty()){
            throw new GamesNotFoundInThisPlayerException();
        }

        return misPartidas.stream()
                .map(this::)
                .collect(Collectors.toList());

    }
    */

    //Metodos privados ---------------------------------------------------------------->>
    private int tirarDados(){
        return (int)Math.floor(Math.random() * 11) + 2;
    }

    private JugadorEntity findPLayer(Long idPlayer){
        log.info("buscando el jugador con el id " + idPlayer);
        return jugadorRepository.findById(idPlayer)
                .orElseThrow(() -> {
                    log.error("No se encontró ningún jugador con el ID " + idPlayer);
                    return new PlayerNotFoundException(idPlayer);
                });
    }

    private void guardarPorcentajeDelJugador(JugadorEntity player){
       int nuevoPorcentaje = actualizarPorcentajeDeExitoJugador(player);
       player.setPorcentajeExito(nuevoPorcentaje);
       jugadorRepository.save(player);
       log.info("Porcentaje actualizado guardado correctamente " + player);
    }

    private int actualizarPorcentajeDeExitoJugador(JugadorEntity player){
        log.info("Buscando lista de partidas del jugador " + player.getNombreJugador());
        List<PartidaEntity> games = partidaRepository.findByJugador(player);
        int total = games.size();
        int victorias = games.stream().mapToInt(PartidaEntity::getVictorias).sum();

        if(total == 0){
            log.info("Se a calculado el porcentaje con éxito 0 %");
            return 0;
        }

        int porcentajeActual = (victorias * 100) / total;

        log.info("Se a calculado el porcentaje con éxito " + porcentajeActual + " % ");
        return porcentajeActual;
    }


}
