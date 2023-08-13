package Luis.JuegoDados.model.services;

import Luis.JuegoDados.model.dto.JugadorDtoJpa;
import Luis.JuegoDados.model.entity.JugadorEntityJpa;
import Luis.JuegoDados.model.entity.PartidaEntityJpa;
import Luis.JuegoDados.model.repository.JugadorRepositoryJpa;
import Luis.JuegoDados.model.repository.PartidaRepositoryJpa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;


import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Builder
@Service
public class JugadorServiceJpa {

    private JugadorRepositoryJpa jugadorRepositoryJpa;

    private PartidaRepositoryJpa partidaRepositoryJpa;

    /**
     * Crea un nuevo jugador en el sistema.
     * <p>
     * Este método permite la creación de un nuevo jugador en el sistema con el nombre proporcionado.
     * Si el nombre es nulo o está en blanco, se asignará el nombre "Anónimo" al jugador creado.
     *
     * @param nombre El nombre del jugador. Puede ser nulo o en blanco.
     * @return Un objeto JugadorDtoJpa que representa al jugador recién creado.
     */
    public JugadorDtoJpa crearJugador(String nombre){
        JugadorEntityJpa jugador = JugadorEntityJpa.builder()
                .nombre(filtraNombre(nombre))
                .build();
       JugadorEntityJpa jugadorCreado = jugadorRepositoryJpa.save(jugador);
        return pasarEntidadADto(jugadorCreado);
    }

    /**
     * Busca un jugador en el sistema por su ID.
     * <p>
     * Este método permite la búsqueda de un jugador en el sistema utilizando su ID proporcionado.
     * Si no se encuentra un jugador con el ID dado, se lanzará una excepción.
     *
     * @param id El ID del jugador que se desea buscar.
     * @return Un objeto JugadorDtoJpa que representa al jugador encontrado.
     * @throws RuntimeException Si no se encuentra un jugador con el ID proporcionado.
     */
    public JugadorEntityJpa buscarJugadorPorId(Long id){
     return jugadorRepositoryJpa.findById(id)
             .orElseThrow(() -> new RuntimeException("No se encontró el jugador con el ID proporcionado."));
    }

    /**
     * Actualiza el nombre de un jugador en la base de datos y devuelve la información actualizada en forma de DTO.
     *
     * @param jugador El objeto de entidad de jugador que se desea actualizar.
     * @param nombre El nuevo nombre que se desea asignar al jugador.
     * @return Un objeto DTO que representa al jugador actualizado.
     */
    public JugadorDtoJpa actualizarNombreJugador(JugadorEntityJpa jugador, String nombre){
        String nombreFinal = filtraNombre(nombre);
        jugador.setNombre(nombreFinal);
        jugadorRepositoryJpa.save(jugador);
        return pasarEntidadADto(jugador);
    }

    /**
     * Actualiza el porcentaje de éxito de un jugador en la base de datos y devuelve la información actualizada en forma de DTO.
     *
     * @param jugador El objeto de entidad de jugador del cual se desea actualizar el porcentaje de éxito.
     * @return Un objeto DTO que representa al jugador actualizado.
     */
   @Transactional
    public void actualizarPorcentajeExitoJugador(JugadorEntityJpa jugador){
        int porcentajeExitoActualizado = calculaPorcentajeExitoDeUnJugador(jugador);
        jugador.setPorcentajeExito(porcentajeExitoActualizado);
        jugadorRepositoryJpa.save(jugador);
    }

    /**
     * Retorna una lista de todos los jugadores en forma de DTO.
     *
     * @return Una lista de objetos JugadorDtoJpa que representan a todos los jugadores.
     * @throws NotFoundException Si la lista de jugadores está vacía.
     */
    public List<JugadorDtoJpa> listaJugadores() {
        List<JugadorEntityJpa> jugadores = jugadorRepositoryJpa.findAll();
        if (jugadores.isEmpty()) {
            throw new NotFoundException("No se encontraron jugadores.");
        }
        return jugadores.stream().map(this::pasarEntidadADto)
                .collect(Collectors.toList());
    }

    /**
     * Calcula y devuelve el porcentaje global de victorias entre todos los jugadores.
     *
     * @return El porcentaje global de victorias.
     */
    public int calculaPorcentajeVictoriasGlobales(){
        List<JugadorEntityJpa> jugadores = jugadorRepositoryJpa.findAll();
        if(jugadores.isEmpty()){return 0;}
        int porcentajeExitoGlobal = jugadores.stream()
                .mapToInt(JugadorEntityJpa::getPorcentajeExito)
                .sum();
        if(porcentajeExitoGlobal == 0){return 0;}
        return porcentajeExitoGlobal/jugadores.size();
    }



    //Metodos Privados----------------------------------------------------------------------->>
    /**
     * Calcula el porcentaje de éxito de un jugador basado en la cantidad de victorias en sus partidas.
     * <p>
     * Este método calcula el porcentaje de éxito de un jugador identificado por su ID. El porcentaje se calcula
     * dividiendo la suma de las victorias en las partidas del jugador por la cantidad total de partidas jugadas.
     * El resultado se multiplica por 100 para obtener el porcentaje.
     *
     * @param jugador La entidad del jugador para el que se va a calcular el porcentaje de éxito.
     * @return El porcentaje de éxito del jugador en sus partidas. Si no hay partidas registradas, devuelve 0.
     * @throws RuntimeException Si no se encuentra el jugador con el ID proporcionado.
     */
    private int calculaPorcentajeExitoDeUnJugador(JugadorEntityJpa jugador){
        int porcentajeExito = 0, victorias;
        List<PartidaEntityJpa> misPartidas = partidaRepositoryJpa.findByJugador(jugador);
        int cantidadPartidas = misPartidas.size();

        victorias = misPartidas.stream().mapToInt(PartidaEntityJpa::getVictorias).sum();

        if (cantidadPartidas > 0) {
            porcentajeExito = (victorias * 100) / cantidadPartidas;
        }
        return porcentajeExito;
    }

    /**

     * Convierte una entidad JugadorEntityJpa en un DTO JugadorDtoJpa.
     * <p>
     * Esta función realiza la conversión de una entidad JugadorEntityJpa a un DTO JugadorDtoJpa,
     * asignando las propiedades relevantes de la entidad al DTO resultante.
     *
     * @param jugador La entidad JugadorEntityJpa que se va a convertir.
     * @return Un objeto JugadorDtoJpa que representa la entidad convertida.
     */
    private JugadorDtoJpa pasarEntidadADto(JugadorEntityJpa jugador) {
        return JugadorDtoJpa.builder()
                .id(jugador.getId())
                .nombre(jugador.getNombre())

                .porcentajeExito(jugador.getPorcentajeExito())
                .build();
    }

   /**
    * Convierte un DTO JugadorDtoJpa en una entidad JugadorEntityJpa.
    * <p>
    * Esta función realiza la conversión de un DTO JugadorDtoJpa a una entidad JugadorEntityJpa,
    * asignando las propiedades relevantes del DTO a la entidad resultante.
    *
    *  @param jugadorDto El DTO JugadorDtoJpa que se va a convertir.
 * @return Una instancia de JugadorEntityJpa que representa el DTO convertido.
 */
    private JugadorEntityJpa pasarDtoAEntidad(JugadorDtoJpa jugadorDto) {
        return JugadorEntityJpa.builder()
                .id(jugadorDto.getId())
                .nombre(jugadorDto.getNombre())
                .porcentajeExito(jugadorDto.getPorcentajeExito())
                .build();
    }

    /**
     * Filtra y normaliza un nombre, retornando "Anónimo" si el nombre es nulo, vacío o contiene solo espacios en blanco.
     *
     * @param cadena La cadena de texto a filtrar y normalizar.
     * @return El nombre filtrado o "Anónimo" si la cadena es nula, vacía o contiene solo espacios en blanco.
     */
    private String filtraNombre(String cadena){
        return  (cadena != null && !cadena.isBlank()) ? cadena : "Anónimo";
    }


}
