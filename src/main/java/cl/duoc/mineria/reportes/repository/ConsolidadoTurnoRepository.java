package cl.duoc.mineria.reportes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.mineria.reportes.model.ConsolidadoTurno;

@Repository
public interface ConsolidadoTurnoRepository extends JpaRepository<ConsolidadoTurno, Long> {
    
    Optional<ConsolidadoTurno> findByTurnoId(Long turnoId);
}