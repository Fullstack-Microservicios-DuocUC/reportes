package cl.duoc.mineria.reportes.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consolidados_turno")
public class ConsolidadoTurno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "turno_id", nullable = false)
    private Long turnoId;

    @Column(name = "total_ciclos_completados", nullable = false)
    private Integer totalCiclosCompletados;

    @Column(name = "total_toneladas_chancador", nullable = false)
    private Double totalToneladasChancador;

    @Column(name = "total_toneladas_relave", nullable = false)
    private Double totalToneladasRelave;

    @Column(name = "incidentes_reportados", nullable = false)
    private Integer incidentesReportados;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;
}
