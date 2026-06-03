package cl.duoc.mineria.reportes.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerarConsolidadoDTO {

    @NotNull(message = "El ID del turno a evaluar es obligatorio")
    @Min(value=1, message = "El ID del turno a evaluar debe ser un número entero mayor que 0")
    private Long turnoId;
}
