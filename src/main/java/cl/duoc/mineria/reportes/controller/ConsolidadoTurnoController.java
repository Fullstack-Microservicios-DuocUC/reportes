package cl.duoc.mineria.reportes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.mineria.reportes.model.ConsolidadoTurno;
import cl.duoc.mineria.reportes.model.GenerarConsolidadoDTO;
import cl.duoc.mineria.reportes.service.ConsolidadoTurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reportes")
@Tag(name = "Gestión de Reportes", description = "Operaciones para generar y consultar reportes consolidados de turnos.")
public class ConsolidadoTurnoController {

    private final ConsolidadoTurnoService service;

    public ConsolidadoTurnoController(ConsolidadoTurnoService service) {
        this.service = service;
    }

    @PostMapping("/consolidar")
    @Operation(summary = "Generar un reporte consolidado de un turno", description = "Procesa los datos de un turno finalizado (ciclos, toneladas, incidentes) y genera un reporte consolidado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reporte consolidado generado y guardado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o el turno no existe"),
            @ApiResponse(responseCode = "409", description = "Conflicto: El reporte para este turno ya fue generado previamente")
    })
    public ResponseEntity<ConsolidadoTurno> consolidarTurno(@Valid @RequestBody GenerarConsolidadoDTO dto) {

        ConsolidadoTurno reporte = service.procesarYGuardarTurno(dto.getTurnoId());
        return new ResponseEntity<>(reporte, HttpStatus.CREATED);
    }

    @GetMapping("/historico")
    @Operation(summary = "Ver el historial de reportes", description = "Obtiene una lista de todos los reportes consolidados que han sido generados.")
    @ApiResponse(responseCode = "200", description = "Historial de reportes obtenido con éxito")
    public ResponseEntity<List<ConsolidadoTurno>> verHistorico() {
        return ResponseEntity.ok(service.listarHistorico());
    }
}
