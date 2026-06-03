package cl.duoc.mineria.reportes.model;

import lombok.Data;

@Data
public class OrdenMantencionResponseDTO {

    private Long id;
    private String estadoOrden;
    private Long turnoId;
}
