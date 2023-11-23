package Luis.JuegoDados.services;

import Luis.JuegoDados.dto.PartidaDto;
import java.util.List;

public interface PartidaService {
     PartidaDto playNewGame(Long idPlayer);
     void deleteAllGamesOfPLayer(Long idPlayer);
     List<PartidaDto> findGamesOfAPLayerById(Long idPLayer);

}
