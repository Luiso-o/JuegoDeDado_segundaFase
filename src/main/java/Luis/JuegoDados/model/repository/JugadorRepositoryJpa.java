package Luis.JuegoDados.model.repository;

import Luis.JuegoDados.model.entity.JugadorEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadorRepositoryJpa extends JpaRepository<JugadorEntityJpa,Long> {
}
