package Luis.JuegoDados.repository;

import Luis.JuegoDados.entity.JugadorEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JugadorRepositoryJpa extends JpaRepository<JugadorEntityJpa,Long> {
}
