package cl.duoc.mineria.reportes.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import cl.duoc.mineria.reportes.model.CicloResponseDTO;
import cl.duoc.mineria.reportes.model.OrdenMantencionResponseDTO;

@Service
public class MetricRetrievalService {

    private final WebClient webClient;

    public MetricRetrievalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    // Trae ÚNICAMENTE los ciclos de este turno específico (Puerto 8088)
    public List<CicloResponseDTO> obtenerCiclosPorTurno(Long turnoId) {
        try {
            // OPTIMIZADO: Ahora le pega al nuevo endpoint /turno/{id}
            CicloResponseDTO[] respuesta = this.webClient.get()
                .uri("http://127.0.0.1:8088/api/v1/ciclos-transporte/turno/{id}", turnoId)
                .retrieve()
                .bodyToMono(CicloResponseDTO[].class)
                .block();
            
            return respuesta != null ? Arrays.asList(respuesta) : List.of();
        } catch (Exception e) {
            System.out.println("[Reportes] Error al conectar con Ciclos Transporte (8088): " + e.getMessage());
            return List.of();
        }
    }

    // Trae ÚNICAMENTE las mantenciones de este turno específico (Puerto 8089)
    // Agregamos Long turnoId como parámetro al método
    public List<OrdenMantencionResponseDTO> obtenerIncidentesTaller(Long turnoId) {
        try {
            // OPTIMIZADO: Ahora le pega al nuevo endpoint /turno/{id} en mantenciones
            OrdenMantencionResponseDTO[] respuesta = this.webClient.get()
                .uri("http://127.0.0.1:8089/api/v1/mantenciones/turno/{turnoId}", turnoId)
                .retrieve()
                .bodyToMono(OrdenMantencionResponseDTO[].class)
                .block();
            
            return respuesta != null ? Arrays.asList(respuesta) : List.of();
        } catch (Exception e) {
            System.out.println("[Reportes] Error al conectar con Mantenciones (8089): " + e.getMessage());
            return List.of();
        }
    }

    // Verifica la existencia del Turno en el módulo Turnos (Puerto 8082)
    public boolean verificarTurnoExiste(Long turnoId) {
        try {
            Boolean existe = this.webClient.get()
                .uri("http://127.0.0.1:8082/api/v1/turnos/existe/{id}", turnoId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
            return existe != null && existe;
        } catch (Exception e) {
            System.out.println("[Reportes] Error al conectar con Turnos (8082): " + e.getMessage());
            return false;
        }
    }
}
