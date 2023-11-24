package Luis.JuegoDados.services;

import Luis.JuegoDados.dto.JugadorDto;
import Luis.JuegoDados.dto.PromedioJugadorDto;
import Luis.JuegoDados.entity.JugadorEntity;
import Luis.JuegoDados.excepciones.PlayerNotSavedException;

import java.util.List;

public interface JugadorService {

    JugadorDto createNewPLayer(String nombre) throws PlayerNotSavedException;
    JugadorDto updatePLayer(Long id, String nombre);
    JugadorEntity findPLayerById(Long id);
    List<PromedioJugadorDto> findAllPLayers();
    int averagePlayerWins();
    List<PromedioJugadorDto> lowestScores();
    List<PromedioJugadorDto> bestScores();

}
