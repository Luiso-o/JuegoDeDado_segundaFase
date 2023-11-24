package Luis.JuegoDados.controller;

import Luis.JuegoDados.dto.JugadorDto;
import Luis.JuegoDados.dto.PartidaDto;
import Luis.JuegoDados.excepciones.PlayerNotSavedException;
import Luis.JuegoDados.services.JugadorServiceImpl;
import Luis.JuegoDados.services.PartidaServiceImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Builder
@RequestMapping("jugador")
@OpenAPIDefinition(info = @Info(title = "Juego de Dados API",version = "6.0",description = "API para gestionar jugadores y partidas en el juego de dados"))
public class JuegoDeDadosController {

    @Autowired
    private final JugadorServiceImpl jugadorService;
    @Autowired
    private final PartidaServiceImpl partidaService;

    @Operation(summary = "Crea un nuevo jugador", description = "Devuelve un objeto jugador,recibirá un parametro de tipo String, si no recibe nada devolverá un Anónimo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Jugador guardado correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    })
    @PostMapping
    public ResponseEntity<JugadorDto> crearNuevoUsuario
            (@RequestParam (required = false, defaultValue = "Anonimo")
             @Pattern(regexp = "^[a-zA-Z]*$") String nombre) throws PlayerNotSavedException {
        JugadorDto newPLayer = jugadorService.createNewPLayer(nombre);
        return ResponseEntity.status(HttpStatus.OK).body(newPLayer);
    }

    @Operation(summary = "Actualiza el nombre de un Jugador", description = "Actualizará el nombre del jugador correspondiente al id introducido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugador actualizado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    })
    @PutMapping("/{id}")
    public ResponseEntity<JugadorDto> actualizarJugador
            (@PathVariable Long id,
             @RequestParam (required = false, defaultValue = "Anonimo")
             @Pattern(regexp = "^[a-zA-Z]*$") String nombre)
    {
       JugadorDto jugadorActualizado = jugadorService.updatePLayer(id,nombre);
       return ResponseEntity.status(HttpStatus.OK).body(jugadorActualizado);
    }

  @Operation(summary = "Juega una partida", description = "Lanza los dados y devuelve los resultados de la partida")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Partida realizada con éxito "),
          @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
  })
    @PostMapping("/{id}/juego")
    public ResponseEntity<PartidaDto> tirarDados(@PathVariable long id){
     PartidaDto game = partidaService.playNewGame(id);
     return ResponseEntity.status(HttpStatus.OK).body(game);
    }

    @Operation(summary = "Elimina las partidas de un Jugador", description = "recibe el id de un jugador y elimina sus partidas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partidas eliminadas con éxito "),
            @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    })
    @DeleteMapping("/{id}/partidas")
    public ResponseEntity<Void> eliminarPartidasDeUnJugador(@PathVariable long id) {
        partidaService.deleteAllGamesOfPLayer(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Ver lista de jugadores",description = "Devuelve la lista de los jugadores con su porcentaje éxito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jugadores encontrados con éxito "),
            @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    })
    @GetMapping()
    public List<JugadorDto> obtenerListaJugadoresConPorcentajeMedioExito() {
        List<JugadorDto> jugadores = jugadorService.findAllPLayers();
        return ResponseEntity.ok(jugadores).getBody();
    }

    @Operation(summary = "Busca Partidas de un jugador", description = "Encontrará las partidas de un jugador por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partidas encontradas con éxito "),
            @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    })
    @GetMapping("/{id}/partidas")
    public ResponseEntity<List<PartidaDto>> muestraPartidasDeUnJugador(@PathVariable long id) {
      List<PartidaDto>myGames = partidaService.findGamesOfAPLayerById(id);
      return ResponseEntity.status(HttpStatus.OK).body(myGames);
    }

 /*
   @Operation(summary = "Ranking de victorias",description = "Muestra el porcentaje total de victorias de todos los jugadores")
    @ApiResponse(responseCode = "200", description = "Porcentaje realizado con éxito")
   @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @GetMapping("/ranking")
    public ResponseEntity<Map<String, Object>> muestraPorcentajeVictorias(){
        int porcentaje = jugadorServiceJpa.calculaPorcentajeVictoriasGlobales();
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("porcentaje de victorias globales", porcentaje + "%");
        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Revisa peores jugadores",description = "Muestra los jugadores con el porcentaje más bajo de victorias")
    @ApiResponse(responseCode = "200", description = "Lista encontrada con éxito")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @GetMapping("/ranking/peores")
    public ResponseEntity<List<JugadorDto>> peoresPorcentajes() {
           List<JugadorDto> peoresJugadores = jugadorServiceJpa.peoresJugadores();
           return ResponseEntity.ok(peoresJugadores);
    }

    @Operation(summary = "Revisa mejores jugadores",description = "Muestra los jugadores con el porcentaje más alto de victorias")
    @ApiResponse(responseCode = "200", description = "Lista encontrada con éxito")
    @ApiResponse(responseCode = "500", description = "Error interno, Revise response status 500")
    @GetMapping("/ranking/mejores")
    public ResponseEntity<List<JugadorDto>> mejoresPorcentajes() {
        List<JugadorDto> peoresJugadores = jugadorServiceJpa.mejoresJugadores();
        return ResponseEntity.ok(peoresJugadores);
    }
     */
}