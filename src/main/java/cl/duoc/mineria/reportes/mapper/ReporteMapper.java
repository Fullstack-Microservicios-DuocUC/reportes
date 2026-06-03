package cl.duoc.mineria.reportes.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import cl.duoc.mineria.reportes.model.ConsolidadoTurno;

@Component
public class ReporteMapper {

    public ConsolidadoTurno buildEntity(Long turnoId, int ciclos, double toneladasChancador, double toneladasRelave, int incidentes) {
        ConsolidadoTurno consolidado = new ConsolidadoTurno();

        consolidado.setTurnoId(turnoId);
        consolidado.setTotalCiclosCompletados(ciclos);
        consolidado.setTotalToneladasChancador(toneladasChancador);
        consolidado.setTotalToneladasRelave(toneladasRelave);
        consolidado.setIncidentesReportados(incidentes);
        consolidado.setFechaGeneracion(LocalDateTime.now());
        
        return consolidado;
    }
}
