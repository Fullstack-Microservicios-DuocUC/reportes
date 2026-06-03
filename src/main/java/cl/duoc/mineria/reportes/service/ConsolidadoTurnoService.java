package cl.duoc.mineria.reportes.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.mineria.reportes.exception.ReportAnalysisException;
import cl.duoc.mineria.reportes.mapper.ReporteMapper;
import cl.duoc.mineria.reportes.model.CicloResponseDTO;
import cl.duoc.mineria.reportes.model.ConsolidadoTurno;
import cl.duoc.mineria.reportes.model.OrdenMantencionResponseDTO;
import cl.duoc.mineria.reportes.repository.ConsolidadoTurnoRepository;

@Service
public class ConsolidadoTurnoService {

    private final ConsolidadoTurnoRepository repository;
    private final ReporteMapper mapper;
    private final MetricRetrievalService retrievalService;

    public ConsolidadoTurnoService(ConsolidadoTurnoRepository repository, ReporteMapper mapper, MetricRetrievalService retrievalService) {
        this.repository = repository;
        this.mapper = mapper;
        this.retrievalService = retrievalService;
    }

    public ConsolidadoTurno procesarYGuardarTurno(Long turnoId) {
        // 1. Validar que el turno exista realmente en el microservicio de Turnos
        if (!retrievalService.verificarTurnoExiste(turnoId)) {
            throw new ReportAnalysisException("No se puede procesar el reporte: El ID de turno " + turnoId + " no existe en el sistema.");
        }

        // 2. Validar que no esté duplicado en reportes
        if (repository.findByTurnoId(turnoId).isPresent()) {
            throw new ReportAnalysisException("El turno con ID " + turnoId + " ya se encuentra cerrado y consolidado en la base de datos.");
        }

        List<CicloResponseDTO> todosLosCiclos = retrievalService.obtenerCiclosPorTurno(turnoId);
        List<OrdenMantencionResponseDTO> todasLasMantenciones = retrievalService.obtenerIncidentesTaller(turnoId);

        int ciclosCompletados = (int) todosLosCiclos.stream()
            .filter(c -> "COMPLETADO".equalsIgnoreCase(c.getEstadoCiclo()))
            .count();
        
        double toneladasChancador = todosLosCiclos.stream()
            .filter(c -> "COMPLETADO".equalsIgnoreCase(c.getEstadoCiclo()))
            .filter(c -> "CHANCADOR".equalsIgnoreCase(c.getDestino()))
            .mapToDouble(CicloResponseDTO::getToneladasCargadas)
            .sum();

        double toneladasRelave = todosLosCiclos.stream()
            .filter(c -> "COMPLETADO".equalsIgnoreCase(c.getEstadoCiclo()))
            .filter(c -> "RELAVE".equalsIgnoreCase(c.getDestino()))
            .mapToDouble(CicloResponseDTO::getToneladasCargadas)
            .sum();
        
        int incidentesReportados = todasLasMantenciones.size();

        ConsolidadoTurno consolidado = mapper.buildEntity(
            turnoId,
            ciclosCompletados,
            toneladasChancador,
            toneladasRelave,
            incidentesReportados
        );

        return repository.save(consolidado);
    }

    public List<ConsolidadoTurno> listarHistorico() {
        return repository.findAll();
    }
}
