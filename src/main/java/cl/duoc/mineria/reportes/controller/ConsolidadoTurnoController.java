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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reportes")
public class ConsolidadoTurnoController {

    private final ConsolidadoTurnoService service;

    public ConsolidadoTurnoController(ConsolidadoTurnoService service) {
        this.service = service;
    }

    @PostMapping("/consolidar")
    public ResponseEntity<ConsolidadoTurno> consolidarTurno(@Valid @RequestBody GenerarConsolidadoDTO dto) {

        ConsolidadoTurno reporte = service.procesarYGuardarTurno(dto.getTurnoId());
        return new ResponseEntity<>(reporte, HttpStatus.CREATED);
    }

    @GetMapping("/historico")
    public ResponseEntity<List<ConsolidadoTurno>> verHistorico() {
        return ResponseEntity.ok(service.listarHistorico());
    }
}
