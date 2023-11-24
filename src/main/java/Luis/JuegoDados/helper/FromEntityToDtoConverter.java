package Luis.JuegoDados.helper;

import Luis.JuegoDados.dto.JugadorDto;
import Luis.JuegoDados.dto.PartidaDto;
import Luis.JuegoDados.entity.JugadorEntity;
import Luis.JuegoDados.entity.PartidaEntity;
import org.springframework.stereotype.Component;

@Component
public class FromEntityToDtoConverter {

    public JugadorDto convertirJugadorEntityADto(JugadorEntity jugador) {
        return JugadorDto.builder()
                .id(jugador.getId())
                .nombre(jugador.getNombreJugador())
                .porcentajeExito(jugador.getPorcentajeExito())
                .build();
    }

    public PartidaDto convertirPartidaEntityADto(PartidaEntity partidaEntity) {
        return PartidaDto.builder()
                .id(partidaEntity.getId())
                .fecha(partidaEntity.getFecha())
                .mensaje(partidaEntity.getVictorias() == 1 ? "Ganaste :D" : "Perdiste :V")
                .build();
    }

}
