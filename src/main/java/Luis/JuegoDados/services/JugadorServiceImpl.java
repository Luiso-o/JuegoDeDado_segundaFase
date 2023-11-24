package Luis.JuegoDados.services;

import Luis.JuegoDados.dto.JugadorDto;
import Luis.JuegoDados.entity.JugadorEntity;
import Luis.JuegoDados.excepciones.EmptyPlayersListException;
import Luis.JuegoDados.excepciones.PlayerNotFoundException;
import Luis.JuegoDados.excepciones.PlayerNotSavedException;
import Luis.JuegoDados.helper.FromEntityToDtoConverter;
import Luis.JuegoDados.repository.JugadorRepository;
import Luis.JuegoDados.repository.PartidaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JugadorServiceImpl implements JugadorService{
    private static final Logger log = LoggerFactory.getLogger(JugadorServiceImpl.class);
    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private PartidaRepository partidaRepository;
    @Autowired
    private FromEntityToDtoConverter converter;

    @Override
    public JugadorDto createNewPLayer(String nombre) throws PlayerNotSavedException {
        try{
            log.info("Iniciando el método createNewPlayer con nombre: {}", nombre);
            JugadorEntity nuevo = JugadorEntity.builder()
                    .nombreJugador(nombre)
                    .porcentajeExito(0)
                    .build();

            jugadorRepository.save(nuevo);
            log.info("Jugador creado exitosamente: {}", nuevo);

            JugadorDto jugadorDto = converter.convertirJugadorEntityADto(nuevo);
            log.info("La entidad se ha convertido a Dto correctamente: {}", jugadorDto);
            return jugadorDto;

        }catch (Exception e){
            log.error("Error al momento de crear un nuevo jugador " + e.getMessage());
            throw new PlayerNotSavedException("No se pudo guardar el jugador. Detalles del error: " + e.getMessage());
        }
    }

    @Override
    public JugadorDto updatePLayer(Long id, String nombre) {
        log.info("Buscando un jugador el la base de datos con el id " + id);
        JugadorEntity updatedPLayer = findPLayerById(id);

        log.info("jugador encontrado en la base de datos");
        String oldName = updatedPLayer.getNombreJugador();
        updatedPLayer.setNombreJugador(nombre);
        jugadorRepository.save(updatedPLayer);
        log.info("Nombre de jugador actualizado correctamente: nombre antiguo: " + oldName + "/nuevo nombre: " + nombre);

        return converter.convertirJugadorEntityADto(updatedPLayer);

    }

    @Override
    public JugadorEntity findPLayerById(Long id) {
        log.info("Buscando un jugador el la base de datos con el id " + id);
        return jugadorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró ningún jugador con el ID " + id);
                    return new PlayerNotFoundException(id);
                });
    }

    @Override
    public List<JugadorDto> findAllPLayers() {
        try {
            List<JugadorEntity> jugadores = jugadorRepository.findAll();

            return jugadores.stream()
                    .map(jugador -> converter.convertirJugadorEntityADto(jugador))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error al recuperar la lista de jugadores: " + e.getMessage());
            throw new EmptyPlayersListException("Error al momento de recuperar la lista de jugadores " + e);
        }
    }

    /*

    public List<JugadorDto> listaJugadores() {
        List<JugadorEntity> jugadores = jugadorRepository.findAll();
        if (jugadores.isEmpty()) {
            throw new EmptyPlayersListException();
        }
        return jugadores.stream().map(this::pasarEntidadADto)
                .collect(Collectors.toList());
    }

    public int calculaPorcentajeVictoriasGlobales(){
        List<JugadorEntity> jugadores = jugadorRepository.findAll();
        if(jugadores.isEmpty()){return 0;}
        int porcentajeExitoGlobal = jugadores.stream()
                .mapToInt(JugadorEntity::getPorcentajeExito)
                .sum();
        if(porcentajeExitoGlobal == 0){return 0;}
        return porcentajeExitoGlobal/jugadores.size();
    }

    public List<JugadorDto> peoresJugadores(){
        List<JugadorEntity> todosLosJugadores = jugadorRepository.findAll();
        List<JugadorDto> peoresJugadores = new ArrayList<>();
        int porcentajeMasBajo = 100; //Partimos con el porcentaje más alto

        if (todosLosJugadores.isEmpty()) {
          throw  new EmptyPlayersListException();
        }

        for (JugadorEntity jugador : todosLosJugadores) {
            int miPorcentajeDeExito = jugador.getPorcentajeExito();

            if(miPorcentajeDeExito < porcentajeMasBajo){
                // Si encontramos un jugador con un porcentaje más bajo, limpiamos la lista anterior
                peoresJugadores.clear();
                porcentajeMasBajo = miPorcentajeDeExito;
            }
            if (miPorcentajeDeExito == porcentajeMasBajo) {
                JugadorDto jugadorDto = pasarEntidadADto(jugador);
                peoresJugadores.add(jugadorDto);
            }
        }
        return peoresJugadores;
    }

    public JugadorEntity buscarJugadorPorId(Long id) {
        Optional<JugadorEntity> jugadorOptional = jugadorRepository.findById(id);

        if (jugadorOptional.isPresent()) {
            return jugadorOptional.get();
        } else {
            throw new PlayerNotFoundException(id);
        }
    }
    public List<JugadorDto> mejoresJugadores() throws NotFoundException {
        List<JugadorEntity> todosLosJugadores = jugadorRepository.findAll();
        List<JugadorDto> mejoresJugadores = new ArrayList<>();
        int porcentajeMasAlto = 0; // Partimos con el porcentaje más bajo

        if (todosLosJugadores.isEmpty()) {
            throw new EmptyPlayersListException();
        }

        for (JugadorEntity jugador : todosLosJugadores) {
            int miPorcentajeDeExito = jugador.getPorcentajeExito();

            if (miPorcentajeDeExito > porcentajeMasAlto) {
                // Si encontramos un jugador con un porcentaje más alto, limpiamos la lista anterior
                mejoresJugadores.clear();
                porcentajeMasAlto = miPorcentajeDeExito;
            }
            if (miPorcentajeDeExito == porcentajeMasAlto) {
                JugadorDto jugadorDto = pasarEntidadADto(jugador);
                mejoresJugadores.add(jugadorDto);
            }
        }

        return mejoresJugadores;
    }

    //Metodos Privados----------------------------------------------------------------------->>

    private int calculaPorcentajeExitoDeUnJugador(JugadorEntity jugador){
        int porcentajeExito = 0, victorias;
        List<PartidaEntity> misPartidas = partidaRepository.findByJugador(jugador);
        int cantidadPartidas = misPartidas.size();

        victorias = misPartidas.stream().mapToInt(PartidaEntity::getVictorias).sum();

        if (cantidadPartidas > 0) {
            porcentajeExito = (victorias * 100) / cantidadPartidas;
        }
        return porcentajeExito;
    }
*/

}
