package cl.duoc.mineria.reportes.model;

import lombok.Data;

@Data
public class CicloResponseDTO {
    
    private Long id;
    private Long turnoId;
    private String estadoCiclo;
    private String destino;
    private Double toneladasCargadas;
}
