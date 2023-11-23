package Luis.JuegoDados.controller;

import Luis.JuegoDados.repository.JugadorRepository;
import Luis.JuegoDados.repository.PartidaRepository;
import Luis.JuegoDados.services.JugadorService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerJpaTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JugadorRepository jugadorRepositoryJpa;
    @Autowired
    private PartidaRepository partidaRepositoryJpa;
    @Autowired
    private JugadorService jugadorServiceJpa;

    @Test
    public void cuandoSeCreaUnUsuario_ElResultadoSeraOk() throws Exception {
        String nombre = "Luis"; // Cambia esto al nombre que desees

        mockMvc.perform(post("/jugador")
                        .param("nombre", nombre)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value(nombre));
    }

    @Test
    public void alActualizarNombreDelJugador_deberiaRetornarOk() throws Exception {
        long idJugador = 1L;
        String nuevoNombre = "Pedro";

        mockMvc.perform(put("/jugador/{id}", idJugador)
                        .param("nombre", nuevoNombre)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value(nuevoNombre));
    }

    @Test
    public void alJugarPartida_deberiaRetornarOk() throws Exception {
        long idJugador = 1L;

        mockMvc.perform(post("/jugador/{id}/juego", idJugador)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value(
                        Matchers.either(Matchers.is("Ganaste :D"))
                                .or(Matchers.is("Perdiste :V"))
                ));
    }

    @Test
    public void alEliminarPartidas_deberiaRetornarNoContent() throws Exception {
        long idJugador = 1L;

        mockMvc.perform(delete("/jugador/{id}/partidas", idJugador))
                .andExpect(status().isNoContent())
                .andExpect(content().string("")); // No hay contenido en la respuesta
    }

    @Test
    public void alObtenerListaDeJugadores_deberiaRetornarListaProcesada() throws Exception {
        mockMvc.perform(get("/jugador"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray()); // La respuesta es un array (lista)
    }

    @Test
    public void testMuestraPartidasDeUnJugador() throws Exception {
        long jugadorId = 1L;

        mockMvc.perform(get("/jugador/{id}/partidas", jugadorId))
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testMuestraPorcentajeVictorias() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/jugador/ranking")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$['porcentaje de victorias globales']").exists());
    }

    @Test
    public void testPeoresPorcentajes() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/jugador/ranking/peores")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testMejoresPorcentajes() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/jugador/ranking/mejores")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

}