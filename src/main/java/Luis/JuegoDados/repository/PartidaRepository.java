package Luis.JuegoDados.repository;

import Luis.JuegoDados.entity.JugadorEntity;
import Luis.JuegoDados.entity.PartidaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<PartidaEntity,Long> {
    List<PartidaEntity> findByJugador(JugadorEntity jugador);
}
